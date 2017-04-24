package org.spec.research.open.xtrace.adapters.appdynamics.source;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

/**
 * 
 * Converts AppDynamics traces into 'OPEN.xtrace'.
 * 
 * @author Manuel Palenga
 * @since 27.09.2016
 *
 */
public class AppDynamicsTraceConverter implements Runnable, TraceConverter { // TraceSource

	private static final String DATA_PATHS_KEY = "appdynamics.fileimporter.datapath";
	private String strDataPath = null;

	@Override
	public void run() {
		this.convertTraces(strDataPath);
	}

	public void initialize(Properties properties) {
		strDataPath = properties.getProperty(DATA_PATHS_KEY);
		if (strDataPath == null) {
			throw new IllegalArgumentException("Data paths have not been specified for the AppDynamics file importer trace source. Key: " + DATA_PATHS_KEY);
		}
	}

	@Override
	public List<Trace> convertTraces(final String path) {
		
		List<Trace> listConvertedTraces = new LinkedList<Trace>();
		
		AppDynamicsTraceImporter importer = new AppDynamicsTraceImporter();

		try {
			Trace trace = importer.importTraceFromFile(path);
			listConvertedTraces.add(trace);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return listConvertedTraces;
	}

	@Override
	public Trace convertTrace(Object trace) {
		throw new UnsupportedOperationException();
	}
}