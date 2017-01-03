package org.spec.research.open.xtrace.adapters.dynatrace.source;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.diagnoseit.spike.shared.TraceSink;
import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Purepathsdashlet.Purepaths.Purepath;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

/**
 * 
 * Creates MethodInvocation with all available measurements from the provided
 * trace.
 * 
 * @author Manuel Palenga
 * @since 30.09.2016
 */
public class DynatraceTraceConverter implements TraceSource, Runnable, TraceConverter {

	private TraceSink traceSink = null;
	private static final String DATA_PATHS_KEY = "dynatrace.fileimporter.datapath";
	private String strDataPath = null;

	@Override
	public void run() {
		this.convertTraces(strDataPath, traceSink);
	}

	@Override
	public void initialize(Properties properties, TraceSink traceSink) {

		this.traceSink = traceSink;

		strDataPath = properties.getProperty(DATA_PATHS_KEY);
		if (strDataPath == null) {
			throw new IllegalArgumentException("Data paths have not been specified for the Dynatrace file importer trace source. Key: " + DATA_PATHS_KEY);
		}
	}

	@Override
	public boolean isManualSource() {
		return false;
	}

	@Override
	public Trace submitNextTrace() {
		throw new UnsupportedOperationException("This operation is only available for manual trace sources!");
	}

	@Override
	public void startTraceGeneration() {
		new Thread(this).start();
	}

	@Override
	public void stopTraceGeneration() {
	}

	@Override
	public void convertTraces(final String path, final TraceSink traceSink) {

		List<Trace> listConvertedTraces = convertTraces(path);
		for (Trace trace : listConvertedTraces) {
			traceSink.appendTrace(trace);
		}
	}

	@Override
	public List<Trace> convertTraces(final String path) {
		
		List<Trace> listConvertedTraces = new LinkedList<Trace>();
		
		DynatraceTraceImporter importer = new DynatraceTraceImporter();

		try {
			importer.checkPath(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (Purepath purepath : importer.getPurePaths(path).getPurepath()) {
			Trace trace = importer.convertPurepathToTrace(purepath);
			listConvertedTraces.add(trace);
		}
		
		return listConvertedTraces;
	}
}
