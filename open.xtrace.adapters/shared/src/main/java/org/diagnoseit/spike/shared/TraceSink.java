package org.diagnoseit.spike.shared;

import org.spec.research.open.xtrace.api.core.Trace;

public interface TraceSink {
	public void appendTrace(Trace inputTrace);
}
