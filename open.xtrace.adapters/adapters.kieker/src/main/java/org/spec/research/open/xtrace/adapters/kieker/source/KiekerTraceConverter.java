package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.analysis.exception.AnalysisConfigurationException;

import org.diagnoseit.spike.shared.TraceSink;
import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

public class KiekerTraceConverter implements TraceSource, TraceConverter {

	private static final String DATA_PATHS_KEY = "kieker.fileimporter.datapaths";
	private TraceSink traceSink;
	private LinkedBlockingQueue<Trace> kiekerQueue;

	@Override
	public void initialize(Properties properties, TraceSink traceSink) {
		this.traceSink = traceSink;
		kiekerQueue = new LinkedBlockingQueue<Trace>(1);

		String dataPathsStr = properties.getProperty(DATA_PATHS_KEY);
		if (dataPathsStr == null) {
			throw new IllegalArgumentException(
					"Data paths have not been specified for the Kieker file importer trace source.");
		}
		String[] traceFolders = dataPathsStr.split(",");

		AnalysisThread thread = new AnalysisThread();
		thread.setTraceFolders(traceFolders);
		thread.start();
	}

	@Override
	public boolean isManualSource() {
		return true;
	}

	@Override
	public Trace submitNextTrace() {
		Trace trace;

		try {
			trace = kiekerQueue.poll(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new IllegalStateException();
		}

		if (trace != null) {
			traceSink.appendTrace(trace);
		}

		return trace;
	}

	@Override
	public void startTraceGeneration() {
		throw new UnsupportedOperationException(
				"This operation is NOT available for manual trace sources!");
	}

	@Override
	public void stopTraceGeneration() {
		throw new UnsupportedOperationException(
				"This operation is NOT available for manual trace sources!");
	}

	private class AnalysisThread extends Thread {
		private String[] traceFolders;

		public void setTraceFolders(String[] traceFolders) {
			this.traceFolders = traceFolders;
		}

		public void run() {
			try {
				TraceConversion.runAnalysis(kiekerQueue, traceFolders);
			} catch (AnalysisConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void convertTraces(String path, TraceSink traceSink) {

		List<Trace> listConvertedTraces = convertTraces(path);
		for (Trace trace : listConvertedTraces) {
			traceSink.appendTrace(trace);
		}
	}

	@Override
	public List<Trace> convertTraces(final String path) {

		List<Trace> listConvertedTraces = new LinkedList<Trace>();

		String[] traceFolders = { path };

		kiekerQueue = new LinkedBlockingQueue<Trace>(1);

		try {
			TraceConversion.runAnalysis(kiekerQueue, traceFolders);
		} catch (AnalysisConfigurationException e) {
			e.printStackTrace();
		}

		try {
			Trace trace = kiekerQueue.poll(10, TimeUnit.SECONDS);
			if (trace != null) {
				listConvertedTraces.add(trace);
			}
		} catch (InterruptedException e) {
			throw new IllegalStateException(e.getMessage());
		}
		return listConvertedTraces;
	}
}
