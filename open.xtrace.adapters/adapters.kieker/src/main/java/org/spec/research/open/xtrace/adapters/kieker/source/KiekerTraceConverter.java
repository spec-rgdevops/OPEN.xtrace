package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.spec.research.open.xtrace.api.core.Trace;

import kieker.analysis.exception.AnalysisConfigurationException;

public class KiekerTraceConverter extends AbstractKiekerConverter {
	// Vector<> is used to make it thread safe
	// TODO check if this is needed
	private List<Trace> kiekerList = new Vector<Trace>();

	@Override
	public Collection<Trace> convertTraces(final String path) {
		String[] traceFolders = path.split(",");

		try {
			TraceConversion.runAnalysis(kiekerList, traceFolders);
		} catch (AnalysisConfigurationException e) {
			e.printStackTrace();
		}
		return kiekerList;
	}
}