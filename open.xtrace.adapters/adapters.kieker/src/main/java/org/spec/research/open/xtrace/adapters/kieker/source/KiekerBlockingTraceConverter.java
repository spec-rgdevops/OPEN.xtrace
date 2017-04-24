package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spec.research.open.xtrace.api.core.Trace;

import kieker.analysis.exception.AnalysisConfigurationException;

public class KiekerBlockingTraceConverter extends AbstractKiekerConverter {
	BlockingQueue<Trace> kiekerQueue = new LinkedBlockingQueue<Trace>(1);

	@Override
	public void initialize(Properties properties) {
		super.initialize(properties);
	}

	private class AnalysisThread extends Thread {
		private String[] traceFolders;

		public void setTraceFolders(String[] traceFolders) {
			this.traceFolders = traceFolders;
		}

		public void run() {
			try {
				TraceConversionBlocking.runAnalysis(kiekerQueue, traceFolders);
			} catch (AnalysisConfigurationException e) {
				Logger.getLogger(KiekerBlockingTraceConverter.class + "").log(Level.SEVERE, "Unable to convert traces.",
						e);
			}
		}
	}

	@Override
	public BlockingQueue<Trace> convertTraces(final String path) {
		AnalysisThread thread = new AnalysisThread();
		thread.setTraceFolders(path.split(","));
		thread.start();
		return kiekerQueue;
	}
}