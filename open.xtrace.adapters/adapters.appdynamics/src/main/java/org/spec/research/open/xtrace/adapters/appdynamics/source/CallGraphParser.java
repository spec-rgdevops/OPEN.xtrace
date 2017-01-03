package org.spec.research.open.xtrace.adapters.appdynamics.source;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spec.research.open.xtrace.adapters.appdynamics.enums.AppDynamicsProperties;
import org.spec.research.open.xtrace.adapters.appdynamics.enums.AppDynamicsTraceAttributes;
import org.spec.research.open.xtrace.api.core.TimedElement;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.dflt.impl.core.LocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.DatabaseInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;

/**
 * 
 * Converts AppDynamics traces into 'OPEN.xtrace'.
 * 
 * @author Manuel Palenga
 * @since 27.09.2016
 *
 */
class CallGraphParser {

	private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss aa");
	private TraceImpl traceImpl = null;
	private long startTraceTimestamp = 0;
	private static final long MILLIS_TO_NANOS_FACTOR = 1000000;
	private int numberOfTabs = 0;
	private Map<String, String> mapSQLQueries = new HashMap<String, String>();

	protected Trace getOPEN_xtrace(final String path) {

		String content = Utils.getContentOfFile(path);
		if (content == null || content.isEmpty()) {
			throw new IllegalArgumentException("No content received.");
		}

		this.createTrace(content);
		this.buildTrace(content);

		return traceImpl;
	}

	private void createTrace(final String content) {

		traceImpl = new TraceImpl(System.nanoTime());

		String tier = Utils.getProperty(content, AppDynamicsProperties.ON_TIER);
		String node = Utils.getProperty(content, AppDynamicsProperties.ON_NODE);
		String startTime = Utils.getProperty(content, AppDynamicsProperties.START_TIME);

		try {
			startTraceTimestamp = dateFormat.parse(startTime).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		SubTraceImpl subTrace = new SubTraceImpl(System.nanoTime(), null, traceImpl);
		traceImpl.setRoot(subTrace);

		LocationImpl location = new LocationImpl();
		location.setNodeType(node);
		location.setApplication(tier);
		subTrace.setLocation(location);
	}

	private void buildTrace(final String content) {

		List<String> listTraceLines = Utils.getTraceLines(content);

		String sqlKey = null;
		MethodInvocationImpl invocation = null;

		// Get all sql statements
		for (int i = 0; i < listTraceLines.size(); i++) {

			String line = listTraceLines.get(i);

			if (line.isEmpty()) {
			} else if (sqlKey != null) {
				mapSQLQueries.put(sqlKey, line);
				sqlKey = null;
			} else if (line.matches("\\[\\d*\\]")) {
				sqlKey = line;
			} else {
				continue;
			}
			listTraceLines.remove(i);
			i--;
		}

		// Parse trace
		for (String line : listTraceLines) {

			if (line.contains("JDBC(")) {
				this.getDatabaseInvocation(line, invocation);
			} else if (!line.isEmpty()) {
				invocation = this.getMethodInvocation(line, invocation);
			}
		}
	}

	private MethodInvocationImpl getMethodInvocation(final String traceLine, final AbstractNestingCallableImpl lastNestingCallable) {

		int currentNumberOfTabs = this.countMatchesAtTheBeginning(traceLine, "\t");

		AbstractNestingCallableImpl parent = null;
		if (lastNestingCallable == null) {
			parent = null;
		} else if (currentNumberOfTabs == numberOfTabs + 1) {
			parent = lastNestingCallable;
		} else if (currentNumberOfTabs == numberOfTabs) {
			parent = (AbstractNestingCallableImpl) lastNestingCallable.getParent();
		} else if (currentNumberOfTabs < numberOfTabs) {
			parent = (AbstractNestingCallableImpl) lastNestingCallable.getParent();
			for (int i = currentNumberOfTabs; i < numberOfTabs; i++) {
				parent = (AbstractNestingCallableImpl) parent.getParent();
			}
		}

		numberOfTabs = currentNumberOfTabs;

		MethodInvocationImpl methodInvocationImpl = new MethodInvocationImpl(parent, (SubTraceImpl) traceImpl.getRoot());
		if (traceImpl.getRoot().getRoot() == null) {
			((SubTraceImpl) traceImpl.getRoot()).setRoot(methodInvocationImpl);
		}

		String methodName = Utils.getTraceAttribute(traceLine, AppDynamicsTraceAttributes.METHOD);
		String classAndPackageName = Utils.getTraceAttribute(traceLine, AppDynamicsTraceAttributes.CLASS);
		String totalTime = Utils.getTraceAttribute(traceLine, AppDynamicsTraceAttributes.TOTAL_TIME);

		methodInvocationImpl.setMethodName(methodName);

		if (totalTime != null) {
			methodInvocationImpl.setResponseTime(Long.parseLong(totalTime) * MILLIS_TO_NANOS_FACTOR);
		}

		methodInvocationImpl.setTimestamp(getTimestamp(parent));

		if (classAndPackageName != null) {

			// Split package and class name
			int posLastPoint = classAndPackageName.lastIndexOf('.');
			String className = classAndPackageName.substring(posLastPoint + 1);
			String packageName = classAndPackageName.substring(0, posLastPoint);

			methodInvocationImpl.setPackageName(packageName);
			methodInvocationImpl.setClassName(className);
		}

		this.setSignature(methodInvocationImpl);

		return methodInvocationImpl;
	}

	private void getDatabaseInvocation(final String traceLine, final AbstractNestingCallableImpl parent) {

		int startQuote = traceLine.indexOf("(");
		int endQuote = traceLine.lastIndexOf("ms)");
		String strResponeTime = traceLine.substring(startQuote + 1, endQuote).trim();
		long responseTime = Long.parseLong(strResponeTime) * MILLIS_TO_NANOS_FACTOR;

		DatabaseInvocationImpl invocation = new DatabaseInvocationImpl(parent, (SubTraceImpl) traceImpl.getRoot());
		invocation.setResponseTime(responseTime);
		invocation.setTimestamp(getTimestamp(parent));

		Matcher matcher = Pattern.compile("\\[\\d*\\]").matcher(traceLine);
		while (matcher.find()) {
			String result = matcher.group();
			String query = mapSQLQueries.get(result);
			invocation.setSQLStatement(query);
		}
	}

	private long getTimestamp(final AbstractNestingCallableImpl parent) {
		if (parent != null) {
			long timestamp = parent.getTimestamp();

			// Without last
			for (int i = 0; i < parent.getCallees().size() - 1; i++) {

				Callable callable = parent.getCallees().get(i);
				if (callable instanceof TimedElement) {
					TimedElement timedElement = (TimedElement) callable;
					timestamp += (timedElement.getResponseTime() / MILLIS_TO_NANOS_FACTOR);
				}
			}
			return timestamp;
		} else {
			return startTraceTimestamp;
		}
	}

	private void setSignature(final MethodInvocationImpl invocationImpl) {

		if (invocationImpl.getClassName().isPresent() && invocationImpl.getPackageName().isPresent() && invocationImpl.getMethodName().isPresent()) {
			String signature = String.format("%s.%s.%s()", invocationImpl.getPackageName().get(), invocationImpl.getClassName().get(), invocationImpl
					.getMethodName().get());

			invocationImpl.setSignature(signature);
		}
	}
	
	private int countMatchesAtTheBeginning(final String content, final String searchFor){
		
		if(content == null || searchFor == null){
			return 0;
		}
		
		String searchForContent = searchFor;
		int count = 0;
		while(content.startsWith(searchForContent)){
			count++;
			searchForContent += searchFor;
		}
		
		return count;
	}
}
