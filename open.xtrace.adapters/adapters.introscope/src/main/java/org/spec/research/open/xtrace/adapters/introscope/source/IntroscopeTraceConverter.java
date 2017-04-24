package org.spec.research.open.xtrace.adapters.introscope.source;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

public class IntroscopeTraceConverter implements Runnable, TraceConverter {

	private static final String DATA_PATHS_KEY = "caintroscope.fileimporter.datapath";
	private String strDataPath = null;

	@Override
	public void run() {

		this.convertTraces(strDataPath);
	}

	@Override
	public void initialize(Properties properties) {
		strDataPath = properties.getProperty(DATA_PATHS_KEY);
		if (strDataPath == null) {
			throw new IllegalArgumentException("Data paths have not been specified for the CA Introscope file importer trace source. Key: " + DATA_PATHS_KEY);
		}
	}

	@Override
	public List<Trace> convertTraces(final String path) {
		
		List<Trace> listConvertedTraces = new LinkedList<Trace>();
		
		IntroscopeTraceImporter importer = new IntroscopeTraceImporter();

		try {
			Trace trace = importer.importTraceFromFile(path);
			listConvertedTraces.add(trace);
		} catch (IllegalStateException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return listConvertedTraces;
	}

	@Override
	public Trace convertTrace(Object trace) {
		throw new UnsupportedOperationException();
	}
}