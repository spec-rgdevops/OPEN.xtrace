package org.spec.research.open.xtrace.adapters.dynatrace.source;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.spec.research.open.xtrace.adapters.dynatrace.enums.ApiType;
import org.spec.research.open.xtrace.adapters.dynatrace.enums.AttachmentType;
import org.spec.research.open.xtrace.adapters.dynatrace.enums.PropertyKey;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Attachment;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Node;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Purepathsdashlet.Purepaths.Purepath;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.HTTPMethod;
import org.spec.research.open.xtrace.api.core.callables.TimedCallable;
import org.spec.research.open.xtrace.dflt.impl.core.LocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractTimedCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.DatabaseInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.ExceptionThrowImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.HTTPRequestProcessingImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.LoggingInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;

/**
 * Importer for Dynatrace purepaths.
 *
 * @author Manuel Palenga
 * @since 13.08.2016
 */
class PurepathTransformer {

	public static final double MILLIS_TO_NANOS_FACTOR = 1000000.0;
	private static final AtomicInteger traceId = new AtomicInteger(1);
	private final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
	private long startTraceTimestamp = 0;

	protected static final String VALUE_NOT_AVAILABLE = "-";
	protected static final String EXECUTE_QUERY_METHOD = "executeQuery()";
	protected static final String DO_POST_METHOD = "doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)";
	protected static final String URL_OF_DB_NOT_SET = "nichtgesetzt";
	protected static final String GARBAGE_COLLECTION_REASON = "Garbage Collection";

	protected Trace transform(Purepath purepath) {

		TraceImpl trace = new TraceImpl(traceId.getAndIncrement());
		SubTraceImpl subTrace = new SubTraceImpl(traceId.getAndIncrement(), null, trace);
		trace.setRoot(subTrace);

		LocationImpl location = new LocationImpl(purepath.getAgent(), purepath.getAgent(), purepath.getApplication(), "");
		subTrace.setLocation(location);

		try {
			startTraceTimestamp = dateFormat.parse(purepath.getStart()).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		if (!purepath.getNode().isEmpty()) {

			Node rootNode = purepath.getNode().get(0);
			AbstractCallableImpl rootCallable = transform(rootNode, null, subTrace);
			subTrace.setRoot(rootCallable);
		}
		return trace;
	}

	private boolean checkFragmentedNode(final Node node) {

		return (node.getMethod() == null) && (node.getClazz() == null) && (node.getApi() == null) && (node.getAgent() == null) && (node.getBreakdown() == null);
	}

	private AbstractCallableImpl transform(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		if (this.checkFragmentedNode(node)) {
			return null;
		}

		AbstractCallableImpl callable = this.createCallable(node, parent, containingSubTrace);

		callable.setTimestamp(startTraceTimestamp + getRelativeTimestamp(node));

		if (callable instanceof TimedCallable) {
			AbstractTimedCallableImpl callableImpl = (AbstractTimedCallableImpl) callable;

			callableImpl.setResponseTime(getDurationNanos(node));
		}

		if (callable instanceof AbstractNestingCallableImpl) {
			AbstractNestingCallableImpl nestingCallable = (AbstractNestingCallableImpl) callable;

			for (Node childNode : node.getNode()) {

				transform(childNode, nestingCallable, containingSubTrace);
			}
		}

		return callable;
	}

	private AbstractCallableImpl createCallable(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		ApiType type = ApiType.fromName(node.getApi());

		if (type != null) {
			switch (type) {
			case SERVLET:
				if (node.getMethod().equals(DO_POST_METHOD)) {
					return createHTTPRequestProcessing(node, parent, containingSubTrace);
				}
				break;
			case JDBC:
				if (node.getMethod().equals(EXECUTE_QUERY_METHOD)) {
					return createDatabaseInvocation(node, parent, containingSubTrace);
				}
				break;
			case EXCEPTION:
				return createExceptionThrow(node, parent, containingSubTrace);

			case LOG4J:
				return createLoggingInvocation(node, parent, containingSubTrace);
			}
		} else {

			// e.g. D3, Exception
			if (node.getApi().contains(ApiType.EXCEPTION.getName())) {
				return createExceptionThrow(node, parent, containingSubTrace);
			}
		}

		return createMethodInvocation(node, parent, containingSubTrace);
	}

	private LoggingInvocationImpl createLoggingInvocation(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		LoggingInvocationImpl loggingImpl = new LoggingInvocationImpl(parent, containingSubTrace);

		String loggingLevel = node.getMethod().substring(7, node.getMethod().length() - 1);
		loggingImpl.setLoggingLevel(loggingLevel);
		loggingImpl.setMessage(node.getArgument());

		return loggingImpl;
	}

	private DatabaseInvocationImpl createDatabaseInvocation(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		DatabaseInvocationImpl databaseImpl = new DatabaseInvocationImpl(parent, containingSubTrace);

		databaseImpl.setSQLStatement(node.getArgument());
		Attachment attachment = Utils.getAttachment(node, AttachmentType.SQLNodeAttachment);

		if (attachment != null) {

			// Get all parameter binding values
			if (databaseImpl.getSQLStatement().contains("?")) {
				Optional<String> result = Optional.empty();
				int keyNumber = 0;
				while (true) {
					keyNumber++;
					result = Utils.getValueOfAttachmentKey(attachment, String.valueOf(keyNumber));
					if (!result.isPresent()) {
						break;
					}
					databaseImpl.addParameterBinding(keyNumber, result.get());
				}

				// One parameter is required for prepared statement
				if (keyNumber > 1) {
					databaseImpl.setPrepared(true);
				}
			}
		}

		// Only validate url strings
		Optional<String> url = Utils.getValueOfAttachmentTypeAndKey(node, AttachmentType.ConnectionPoolNodeAttachment, PropertyKey.URL);
		if (url.isPresent() && !url.get().equals(URL_OF_DB_NOT_SET)) {
			databaseImpl.setDBUrl(url);
		}

		return databaseImpl;
	}

	private ExceptionThrowImpl createExceptionThrow(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		ExceptionThrowImpl exceptionImpl = new ExceptionThrowImpl(parent, containingSubTrace);

		exceptionImpl.setErrorMessage(node.getArgument());
		exceptionImpl.setThrowableType(node.getClazz());

		return exceptionImpl;
	}

	private HTTPRequestProcessingImpl createHTTPRequestProcessing(Node node, AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

		HTTPRequestProcessingImpl httpProcessing = new HTTPRequestProcessingImpl(parent, containingSubTrace);

		Attachment servletAttachment = Utils.getAttachment(node, AttachmentType.StoredServletNodeAttachment);

		Optional<String> requestMethod = Utils.getValueOfAttachmentKey(servletAttachment, PropertyKey.REQUEST_METHOD);
		Optional<String> uri = Utils.getValueOfAttachmentKey(servletAttachment, PropertyKey.URI);
		Optional<String> responseStatus = Utils.getValueOfAttachmentKey(servletAttachment, PropertyKey.RESPONSE_STATUS);

		if (responseStatus.isPresent()) {
			try {
				httpProcessing.setResponseCode(Long.parseLong(responseStatus.get()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (uri.isPresent()) {
			httpProcessing.setUri(uri.get());
		}

		if (requestMethod.isPresent()) {
			httpProcessing.setRequestMethod(HTTPMethod.valueOf(requestMethod.get()));
		}

		return httpProcessing;
	}

	private MethodInvocationImpl createMethodInvocation(final Node node, final AbstractNestingCallableImpl parent, final SubTraceImpl containingSubTrace) {

		MethodInvocationStrategy strategy = new MethodInvocationStrategy();
		return strategy.initMethodInvocation(node, parent, containingSubTrace);
	}

	private long getDurationNanos(Node node) {

		return (long) (node.getTotaltime() * MILLIS_TO_NANOS_FACTOR);
	}

	private long getRelativeTimestamp(Node node) {

		if (node.getRelativestart() == null) {
			return 0;
		}
		return node.getRelativestart().longValue();
	}

}
