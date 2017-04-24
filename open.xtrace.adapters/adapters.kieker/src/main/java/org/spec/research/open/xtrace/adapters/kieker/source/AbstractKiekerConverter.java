package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.Properties;

import org.spec.research.open.xtrace.adapters.kieker.impl.TraceImpl;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

import kieker.tools.traceAnalysis.systemModel.MessageTrace;

public abstract class AbstractKiekerConverter implements TraceConverter {
	@Override
	public void initialize(Properties properties) {
	}

	@Override
	public Trace convertTrace(Object trace) {
		if (trace instanceof MessageTrace) {
			MessageTrace messageTrace = (MessageTrace) trace;
			return TraceImpl.createCallableTrace(messageTrace);
		} else
			// TODO maybe throw an exception?
			return null;
	}
}