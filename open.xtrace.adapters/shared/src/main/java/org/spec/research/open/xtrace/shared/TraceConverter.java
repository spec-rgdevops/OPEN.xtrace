package org.spec.research.open.xtrace.shared;

import java.util.Collection;
import java.util.Properties;

import org.spec.research.open.xtrace.api.core.Trace;

public interface TraceConverter {
	/**
	 * Initializes the adapter.
	 * @param properties properties for adapter configuration
	 */
	void initialize(Properties properties);

	/**
	 * Converts traces from the provided file into OPEN.xtrace.
	 * 
	 * @param filePath
	 *            - Path to file with traces
	 * @return Converted traces in OPEN.xtrace format.
	 */
	public Collection<Trace> convertTraces(final String path);

	/**
	 * Converts one trace. The input format depends on the APM tool that the adapter is for.
	 * @param trace trace that is going to be converted
	 * @return OPEN.xtrace representation of the trace
	 */
	Trace convertTrace(Object trace);

}
