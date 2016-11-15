package org.spec.research.open.xtrace.dflt.impl.tranformer;

import java.util.Map;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.DatabaseInvocation;
import org.spec.research.open.xtrace.api.core.callables.ExceptionThrow;
import org.spec.research.open.xtrace.api.core.callables.HTTPRequestProcessing;
import org.spec.research.open.xtrace.api.core.callables.LoggingInvocation;
import org.spec.research.open.xtrace.api.core.callables.MethodInvocation;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
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
import org.spec.research.open.xtrace.dflt.impl.core.callables.RemoteInvocationImpl;

/**
 * The {@link DefaultOPENxtraceTransformer} transforms any representation of the
 * 'OPEN.xtrace' into the default 'OPEN.xtrace' implementation.
 * 
 * @author Alexander Wert
 *
 */
public class DefaultOPENxtraceTransformer {

	/**
	 * Transforms an entire {@link Trace} instance into a {@link TraceImpl}
	 * instance.
	 * 
	 * @param trace
	 *            {@link Trace} instance to transform
	 * @return corresponding trace instance in the default implementation format
	 */
	public TraceImpl transform(Trace trace) {
		TraceImpl dfltTrace = new TraceImpl(trace.getTraceId());
		SubTraceImpl rootSubTrace = transform(trace.getRoot(), null, dfltTrace);
		dfltTrace.setRoot(rootSubTrace);
		return dfltTrace;
	}

	/**
	 * Transforms a {@link SubTrace} instance into a {@link SubTraceImpl}
	 * instance.
	 * 
	 * @param subTrace
	 *            {@link SubTrace} instance to transform
	 * @param dfltParent
	 *            parent sub trace in the default implementation
	 * @param dfltTrace
	 *            containing trace in the default implementation
	 * @return corresponding sub trace instance in the default implementation
	 *         format
	 */
	public SubTraceImpl transform(SubTrace subTrace, SubTraceImpl dfltParent, TraceImpl dfltTrace) {
		SubTraceImpl dfltSubTrace = new SubTraceImpl(subTrace.getSubTraceId(), dfltParent, dfltTrace);

		// transform location
		LocationImpl dfltLocation = transform(subTrace.getLocation());
		dfltSubTrace.setLocation(dfltLocation);

		// transform callables
		AbstractCallableImpl dfltCallable = transform(subTrace.getRoot(), null, dfltSubTrace);
		dfltSubTrace.setRoot(dfltCallable);

		return dfltSubTrace;
	}

	/**
	 * Transforms a {@link Location} instance into a {@link LocationImpl}
	 * instance.
	 * 
	 * @param location
	 *            {@link Location} instance to transform
	 * @return corresponding location in the default implementation format
	 */
	public LocationImpl transform(Location location) {
		LocationImpl loc = new LocationImpl(location.getHost());

		location.getRuntimeEnvironment().ifPresent(s -> loc.setRunTimeEnvironment(s));
		location.getApplication().ifPresent(s -> loc.setApplication(s));
		location.getBusinessTransaction().ifPresent(s -> loc.setBusinessTransaction(s));

		return loc;
	}

	/**
	 * This is only a placeholder method. Actually, it should never be called!
	 * 
	 * @param callable
	 *            callable
	 * @param dfltParent
	 *            parent
	 * @param dfltSubTrace
	 *            subTrace
	 * @return nothing
	 */
	public AbstractCallableImpl transform(Callable callable, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		throw new IllegalStateException("This method should never be called!");
	}

	/**
	 * Transforms a {@link MethodInvocation} instance into a
	 * {@link MethodInvocationImpl} instance.
	 * 
	 * @param methodInvocation
	 *            {@link MethodInvocation} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link MethodInvocationImpl} instance in the
	 *         default implementation format
	 */
	public MethodInvocationImpl transform(MethodInvocation methodInvocation, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		MethodInvocationImpl dfltMethodInvocation = new MethodInvocationImpl(dfltParent, dfltSubTrace);
		dfltMethodInvocation.setCPUTime(methodInvocation.getCPUTime());

		// TODO: Eigene setter methode aufrufen
		// dfltMethodInvocation.setSignature(methodInvocation.getReturnType().orElse(""),
		// methodInvocation.getPackageName().orElse(""),
		// methodInvocation.getClassName().orElse(""),
		// methodInvocation.getMethodName().orElse(""),
		// methodInvocation.getParameterTypes().orElse(Collections.emptyList()));

		methodInvocation.getReturnType().ifPresent(v -> dfltMethodInvocation.setReturnType(v));
		methodInvocation.getPackageName().ifPresent(v -> dfltMethodInvocation.setPackageName(v));
		methodInvocation.getClassName().ifPresent(v -> dfltMethodInvocation.setClassName(v));
		methodInvocation.getMethodName().ifPresent(v -> dfltMethodInvocation.setMethodName(v));
		methodInvocation.getParameterTypes().ifPresent(v -> dfltMethodInvocation.setParameterTypes(v));

		Map<Integer, String> parameterValues = methodInvocation.getParameterValues().get();
		for (Integer key : parameterValues.keySet()) {
			dfltMethodInvocation.addParameterValue(key, parameterValues.get(key));
		}
		transformTimedCallableInfo(methodInvocation, dfltMethodInvocation);
		transformCallableInfo(methodInvocation, dfltMethodInvocation);
		return dfltMethodInvocation;
	}

	/**
	 * Transforms a {@link RemoteInvocation} instance into a
	 * {@link RemoteInvocationImpl} instance.
	 * 
	 * @param remoteInvocation
	 *            {@link RemoteInvocation} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link RemoteInvocationImpl} instance in the
	 *         default implementation format
	 */
	public RemoteInvocationImpl transform(RemoteInvocation remoteInvocation, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		RemoteInvocationImpl dfltRemoteInvocation = new RemoteInvocationImpl(dfltParent, dfltSubTrace);
		dfltRemoteInvocation.setTarget(remoteInvocation.getTarget());
		if (remoteInvocation.getTargetSubTrace().isPresent()) {
			SubTraceImpl dfltTargetSubTrace = transform(remoteInvocation.getTargetSubTrace().get(), null, (TraceImpl) dfltSubTrace.getContainingTrace());
			dfltRemoteInvocation.setTargetSubTrace(dfltTargetSubTrace);
		}

		transformTimedCallableInfo(remoteInvocation, dfltRemoteInvocation);
		transformCallableInfo(remoteInvocation, dfltRemoteInvocation);
		return dfltRemoteInvocation;
	}

	/**
	 * Transforms a {@link DatabaseInvocation} instance into a
	 * {@link DatabaseInvocationImpl} instance.
	 * 
	 * @param dbInvocation
	 *            {@link DatabaseInvocation} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link DatabaseInvocationImpl} instance in the
	 *         default implementation format
	 */
	public DatabaseInvocationImpl transform(DatabaseInvocation dbInvocation, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		DatabaseInvocationImpl dfltDBInvocation = new DatabaseInvocationImpl(dfltParent, dfltSubTrace);
		dfltDBInvocation.setDBProductName(dbInvocation.getDBProductName());
		dfltDBInvocation.setDBProductVersion(dbInvocation.getDBProductVersion());
		dfltDBInvocation.setDBUrl(dbInvocation.getDBUrl());
		dbInvocation.isPrepared().ifPresent(b -> dfltDBInvocation.setPrepared(b));
		dfltDBInvocation.setSQLStatement(dbInvocation.getSQLStatement());
		Map<Integer, String> parameterBindings = dbInvocation.getParameterBindings().get();
		for (Integer key : parameterBindings.keySet()) {
			dfltDBInvocation.addParameterBinding(key, parameterBindings.get(key));
		}

		transformTimedCallableInfo(dbInvocation, dfltDBInvocation);
		transformCallableInfo(dbInvocation, dfltDBInvocation);
		return dfltDBInvocation;
	}

	/**
	 * Transforms a {@link HTTPRequestProcessing} instance into a
	 * {@link HTTPRequestProcessingImpl} instance.
	 * 
	 * @param httpRequest
	 *            {@link HTTPRequestProcessing} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link HTTPRequestProcessingImpl} instance in the
	 *         default implementation format
	 */
	public HTTPRequestProcessingImpl transform(HTTPRequestProcessing httpRequest, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		HTTPRequestProcessingImpl dfltHTTPRequest = new HTTPRequestProcessingImpl(dfltParent, dfltSubTrace);
		dfltHTTPRequest.setUri(httpRequest.getUri());
		httpRequest.getRequestMethod().ifPresent(v -> dfltHTTPRequest.setRequestMethod(v));
		dfltHTTPRequest.setHTTPParameters(httpRequest.getHTTPParameters().get());
		dfltHTTPRequest.setHTTPAttributes(httpRequest.getHTTPAttributes().get());
		dfltHTTPRequest.setHTTPSessionAttributes(httpRequest.getHTTPSessionAttributes().get());
		dfltHTTPRequest.setHTTPHeaders(httpRequest.getHTTPHeaders().get());

		transformTimedCallableInfo(httpRequest, dfltHTTPRequest);
		transformCallableInfo(httpRequest, dfltHTTPRequest);
		return dfltHTTPRequest;
	}

	/**
	 * Transforms a {@link ExceptionThrow} instance into a
	 * {@link ExceptionThrowImpl} instance.
	 * 
	 * @param exceptionThrow
	 *            {@link ExceptionThrow} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link ExceptionThrowImpl} instance in the default
	 *         implementation format
	 */
	public ExceptionThrowImpl transform(ExceptionThrow exceptionThrow, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		ExceptionThrowImpl dfltExceptionThrow = new ExceptionThrowImpl(dfltParent, dfltSubTrace);
		dfltExceptionThrow.setErrorMessage(exceptionThrow.getErrorMessage());
		exceptionThrow.getCause().ifPresent(v -> dfltExceptionThrow.setCause(v));
		exceptionThrow.getStackTrace().ifPresent(v -> dfltExceptionThrow.setStackTrace(v));
		exceptionThrow.getThrowableType().ifPresent(v -> dfltExceptionThrow.setThrowableType(v));

		transformCallableInfo(exceptionThrow, dfltExceptionThrow);
		return dfltExceptionThrow;
	}

	/**
	 * Transforms a {@link LoggingInvocation} instance into a
	 * {@link LoggingInvocationImpl} instance.
	 * 
	 * @param loggingInvocation
	 *            {@link LoggingInvocation} instance to transform
	 * @param dfltParent
	 *            parent nesting callable in the default implementation
	 * @param dfltSubTrace
	 *            containing sub trace in the default implementation
	 * @return corresponding {@link LoggingInvocationImpl} instance in the
	 *         default implementation format
	 */
	public LoggingInvocationImpl transform(LoggingInvocation loggingInvocation, AbstractNestingCallableImpl dfltParent, SubTraceImpl dfltSubTrace) {
		LoggingInvocationImpl dfltLoggingInvocation = new LoggingInvocationImpl(dfltParent, dfltSubTrace);
		dfltLoggingInvocation.setMessage(loggingInvocation.getMessage());
		loggingInvocation.getLoggingLevel().ifPresent(v -> dfltLoggingInvocation.setLoggingLevel(v));

		transformCallableInfo(loggingInvocation, dfltLoggingInvocation);
		return dfltLoggingInvocation;
	}

	/**
	 * Transforms information that is specific to {@link TimedCallable}.
	 * 
	 * @param timedCallable
	 *            source {@link TimedCallable} instance
	 * @param dfltTimedCallable
	 *            target {@link AbstractTimedCallableImpl} instance
	 */
	private void transformTimedCallableInfo(TimedCallable timedCallable, AbstractTimedCallableImpl dfltTimedCallable) {
		dfltTimedCallable.setResponseTime(timedCallable.getResponseTime());
	}

	/**
	 * Transforms information that is specific to {@link Callable}.
	 * 
	 * @param callable
	 *            source {@link Callable} instance
	 * @param dfltCallable
	 *            target {@link AbstractCallableImpl} instance
	 */
	private void transformCallableInfo(Callable callable, AbstractCallableImpl dfltCallable) {
		dfltCallable.setTimestamp(callable.getTimestamp());
		for (String label : callable.getLabels().get()) {
			dfltCallable.addLabel(label);
		}
	}
}
