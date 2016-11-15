package org.spec.research.open.xtrace.api.utils;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.DatabaseInvocation;
import org.spec.research.open.xtrace.api.core.callables.ExceptionThrow;
import org.spec.research.open.xtrace.api.core.callables.HTTPRequestProcessing;
import org.spec.research.open.xtrace.api.core.callables.LoggingInvocation;
import org.spec.research.open.xtrace.api.core.callables.MethodInvocation;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;

/**
 * Provides utility functionality related to String representations of CTA elements.
 * 
 * @author Alexander Wert
 *
 */
public final class StringUtils {
	/**
	 * Double String formatter.
	 */
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	/**
	 * Nano to milli seconds transformer factor.
	 */
	private static final double NANOS_TO_MILLIS_FACTOR = 0.000001;

	/**
	 * Start and end line of a String representation of a {@link SubTrace}.
	 */
	private static final String SUBTRACE_DELIMITER = "-------------------------------------------------------------------------------------------------------------------------";

	/**
	 * Start and end line of a String representation of a {@link Trace}.
	 */
	private static final String TRACE_DELIMITER = "#########################################################################################################################";

	/**
	 * Private constructor for utility class.
	 */
	private StringUtils() {
	}

	/**
	 * Creates a common String representation for the passed {@link MethodInvocation} instance.
	 * 
	 * @param methodInvocation
	 *            {@link MethodInvocation} instance to provide the String representation for
	 * @return common string representation for {@link MethodInvocation}
	 */
	public static String getStringRepresentation(MethodInvocation methodInvocation) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(methodInvocation.getClassName());
		strBuilder.append(".");
		strBuilder.append(methodInvocation.getMethodName());
		strBuilder.append(" [ ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) methodInvocation.getResponseTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" | ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) methodInvocation.getExclusiveTime()) * NANOS_TO_MILLIS_FACTOR));
		if (methodInvocation.getCPUTime().isPresent()) {
			strBuilder.append(" | ");
			strBuilder.append(DECIMAL_FORMAT.format(((double) methodInvocation.getCPUTime().get()) * NANOS_TO_MILLIS_FACTOR));
		}
		strBuilder.append(" ]");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link DatabaseInvocation} instance.
	 * 
	 * @param dbInvocation
	 *            {@link DatabaseInvocation} instance to provide the String representation for
	 * @return common string representation for {@link DatabaseInvocation}
	 */
	public static String getStringRepresentation(DatabaseInvocation dbInvocation) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("# SQL (");
		String sql = dbInvocation.getSQLStatement();
		strBuilder.append(sql.substring(0, Math.min(20, sql.length())));
		strBuilder.append(")");
		strBuilder.append(" [ ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) dbInvocation.getResponseTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" | ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) dbInvocation.getExclusiveTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" ]");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link DatabaseInvocation} instance.
	 * 
	 * @param httpInvocation
	 *            {@link DatabaseInvocation} instance to provide the String representation for
	 * @return common string representation for {@link DatabaseInvocation}
	 */
	public static String getStringRepresentation(HTTPRequestProcessing httpInvocation) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("# HTTP ");
		strBuilder.append(httpInvocation.getRequestMethod());
		strBuilder.append(" (");
		strBuilder.append(httpInvocation.getUri());
		strBuilder.append(")");
		strBuilder.append(" [ ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) httpInvocation.getResponseTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" | ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) httpInvocation.getExclusiveTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" ]");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link RemoteInvocation} instance.
	 * 
	 * @param remoteInvocation
	 *            {@link RemoteInvocation} instance to provide the String representation for
	 * @return common string representation for {@link RemoteInvocation}
	 */
	public static String getStringRepresentation(RemoteInvocation remoteInvocation) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("# REMOTE ");
		strBuilder.append(" (");
		strBuilder.append(remoteInvocation.getTarget());
		strBuilder.append(")");
		strBuilder.append(" [ ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) remoteInvocation.getResponseTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" | ");
		strBuilder.append(DECIMAL_FORMAT.format(((double) remoteInvocation.getExclusiveTime()) * NANOS_TO_MILLIS_FACTOR));
		strBuilder.append(" ]");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link ExceptionThrow} instance.
	 * 
	 * @param exceptionThrow
	 *            {@link ExceptionThrow} instance to provide the String representation for
	 * @return common string representation for {@link ExceptionThrow}
	 */
	public static String getStringRepresentation(ExceptionThrow exceptionThrow) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("# EXCEPTION ");
		strBuilder.append(" (");
		strBuilder.append(exceptionThrow.getThrowableType());
		strBuilder.append(")");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link LoggingInvocation} instance.
	 * 
	 * @param loggingInvocation
	 *            {@link LoggingInvocation} instance to provide the String representation for
	 * @return common string representation for {@link LoggingInvocation}
	 */
	public static String getStringRepresentation(LoggingInvocation loggingInvocation) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("# LOGGING ");
		strBuilder.append(" (");
		strBuilder.append(loggingInvocation.getLoggingLevel());
		strBuilder.append(")");
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link SubTrace} instance.
	 * 
	 * @param subTrace
	 *            {@link SubTrace} instance to provide the String representation for
	 * @return common string representation for {@link SubTrace}
	 */
	public static String getStringRepresentation(SubTrace subTrace) {
		return getStringRepresentation(subTrace, Integer.MAX_VALUE);
	}

	/**
	 * Creates a common String representation for the passed {@link SubTrace} instance. Outputs a
	 * maximum number of {@link SubTrace} elements as specified in by the maxNumberLines property.
	 * 
	 * @param subTrace
	 *            {@link SubTrace} instance to provide the String representation for
	 * @param maxNumberLines
	 *            limit for the number of lines to output
	 * @return common string representation for {@link SubTrace}
	 */
	public static String getStringRepresentation(SubTrace subTrace, int maxNumberLines) {
		StringBuilder strBuilder = new StringBuilder();
		String indent = "   ";
		String header = " SubTrace (" + subTrace.getSubTraceId() + ") : [ Response Time (ms)| Execution Time (ms)| CPU Time (ms)] ";
		int diff = SUBTRACE_DELIMITER.length() - header.length();
		String firstLine = SUBTRACE_DELIMITER.substring(0, diff / 2 + 1) + header + SUBTRACE_DELIMITER.substring(0, SUBTRACE_DELIMITER.length() - (diff / 2 + 1 + header.length()));
		strBuilder.append(firstLine);
		strBuilder.append(System.lineSeparator());
		TreeIterator<Callable> iterator = subTrace.iterator();
		int counter = 0;
		while (iterator.hasNext()) {

			Callable callable = iterator.next();
			for (int i = 0; i < iterator.currentDepth(); i++) {
				strBuilder.append(indent);
			}
			if (counter >= maxNumberLines) {
				strBuilder.append("[...]");
				strBuilder.append(System.lineSeparator());
				break;
			} else {
				strBuilder.append(callable);
				strBuilder.append(System.lineSeparator());
			}

			counter++;
		}
		strBuilder.append(SUBTRACE_DELIMITER);
		strBuilder.append(System.lineSeparator());
		strBuilder.append(System.lineSeparator());
		return strBuilder.toString();
	}

	/**
	 * Creates a common String representation for the passed {@link Trace} instance.
	 * 
	 * @param trace
	 *            {@link Trace} instance to provide the String representation for
	 * @return common string representation for {@link Trace}
	 */
	public static String getStringRepresentation(Trace trace) {
		StringBuilder strBuilder = new StringBuilder();
		String header = " Trace (" + trace.getTraceId() + ") ";
		int diff = TRACE_DELIMITER.length() - header.length();
		String firstLine = TRACE_DELIMITER.substring(0, diff / 2 + 1) + header + TRACE_DELIMITER.substring(0, TRACE_DELIMITER.length() - (diff / 2 + 1 + header.length()));
		strBuilder.append(firstLine);
		strBuilder.append(System.lineSeparator());
		if (trace.getRoot() == null) {
			strBuilder.append("EMPTY TRACE");
			strBuilder.append(System.lineSeparator());
		} else if (!trace.getRoot().getSubTraces().isEmpty()) {
			strBuilder.append(getStringRepresentation(trace.getRoot()));
		} else {
			String indent = "   ";
			SubTraceIterator iterator = (SubTraceIterator) trace.subTraceIterator();
			while (iterator.hasNext()) {
				SubTrace subTrace = iterator.next();
				for (int i = 0; i < iterator.currentDepth(); i++) {
					strBuilder.append(indent);
				}
				strBuilder.append("SubTrace-");
				strBuilder.append(subTrace.getSubTraceId());
				strBuilder.append(" (");
				strBuilder.append(getStringRepresentation(subTrace.getLocation()));
				strBuilder.append(")");
				strBuilder.append(System.lineSeparator());
			}

		}
		strBuilder.append(TRACE_DELIMITER);
		strBuilder.append(System.lineSeparator());
		strBuilder.append(System.lineSeparator());
		return strBuilder.toString();

	}

	/**
	 * Creates a common String representation for the passed {@link Location} instance.
	 * 
	 * @param location
	 *            {@link Location} instance to provide the String representation for
	 * @return common string representation for {@link Location}
	 */
	public static String getStringRepresentation(Location location) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(location.getHost());
		strBuilder.append(" | ");
		strBuilder.append(location.getRuntimeEnvironment());
		strBuilder.append(" | ");
		strBuilder.append(location.getApplication());
		strBuilder.append(" | ");
		strBuilder.append(location.getBusinessTransaction());
		return strBuilder.toString();
	}

	/**
	 * Constructs the String representation for path from the passed {@link Callable} instance to
	 * its root in the corresponding {@link SubTrace}.
	 * 
	 * @param callable
	 *            {@link Callable} instance defining the path
	 * @return string representation of the path
	 */
	public static String getPathFromRootAsString(Callable callable) {
		return getPathFromRootAsString(callable, new AtomicInteger(0));
	}

	/**
	 * Recursively constructs the String representation for path from the passed {@link Callable}
	 * instance to its root in the corresponding {@link SubTrace}.
	 * 
	 * @param callable
	 *            {@link Callable} instance defining the path
	 * @param depth
	 *            recursive depth value
	 * @return string representation of the path
	 */
	private static String getPathFromRootAsString(Callable callable, AtomicInteger depth) {
		String result = null;
		if (callable.getParent() == null) {
			result = callable.toString();
		} else {
			String parentStr = getPathFromRootAsString(callable.getParent(), depth);
			String indent = "";
			for (int i = 0; i < depth.get(); i++) {
				indent += "   ";
			}
			result = parentStr + System.lineSeparator() + indent + callable.toString();
		}
		depth.incrementAndGet();
		return result;
	}
}
