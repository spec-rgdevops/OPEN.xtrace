package org.diagnoseit.spike.shared;

import java.util.Properties;

import org.spec.research.open.xtrace.api.core.Trace;

public interface TraceSource {

	static final String RESPONSETIME_THRESHOLD = "diagnoseit.spike.rtthreshold";
	static final long RESPONSETIME_THRESHOLD_DEFAULT = 1000;

	void initialize(Properties properties, TraceSink traceSink);

	boolean isManualSource();

	Trace submitNextTrace();

	void startTraceGeneration();

	void stopTraceGeneration();
}
