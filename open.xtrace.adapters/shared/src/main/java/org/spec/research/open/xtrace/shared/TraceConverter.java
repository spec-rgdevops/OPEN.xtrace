package org.spec.research.open.xtrace.shared;

import java.util.List;

import org.diagnoseit.spike.shared.TraceSink;
import org.spec.research.open.xtrace.api.core.Trace;

public interface TraceConverter {

	/**
	 * Converts traces from the provided file into OPEN.xtrace. traceSink
	 * contains the converted traces.
	 * 
	 * @param filePath
	 *            - Path to file with traces
	 * @param traceSink
	 *            - Container for results
	 */
	public void convertTraces(final String path, final TraceSink traceSink);

	/**
	 * Converts traces from the provided file into OPEN.xtrace.
	 * 
	 * @param filePath
	 *            - Path to file with traces
	 * @return Converted traces in OPEN.xtrace format.
	 */
	public List<Trace> convertTraces(final String path);

}
