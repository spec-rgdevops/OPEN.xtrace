package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.concurrent.LinkedBlockingQueue;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.spec.research.open.xtrace.api.core.Trace;

/**
 * Uses Kieker's analysis component and {@link OPENxtraceFilter} to convert
 * Kieker traces into 'OPEN.xtrace'.
 * 
 * Constructed traces are than stored into list ({@link LinkedBlockingQueue})
 * which is provided to runAnalysis method.
 * 
 * @author Okanovic
 *
 */
public class TraceConversion {
	// list of folders containing traces - for testing
	private static final String folderPath_KiekerLogs = "F:/KiekerLogs";
	private static final String[] trace_folders = { 
		//	folderPath_KiekerLogs + "/kieker-20150730-135300977-UTC-lb.rssreader.rssperf.emulab.net-KIEKER", // Keine Methoden und Klassen in der .map
		//	folderPath_KiekerLogs + "/kieker-20150730-135301325-UTC-rssserver.rssreader.rssperf.emulab.net-KIEKER", // Keine Methoden und Klassen in der .map
		//	folderPath_KiekerLogs + "/kieker-20150730-135301407-UTC-rssserver2.rssreader.rssperf.emulab.net-KIEKER", // Keine Methoden und Klassen in der .map
		//	folderPath_KiekerLogs + "/kieker-20150730-135301705-UTC-db.rssreader.rssperf.emulab.net-KIEKER", // Keine Methoden und Klassen in der .map
		//	folderPath_KiekerLogs + "/kieker-20150730-135805914-UTC-client.rssreader.rssperf.emulab.net-KIEKER" , // Keine Methoden und Klassen in der .map
			folderPath_KiekerLogs + "/kieker-20150730-135303897-UTC-edge.rssreader.rssperf.emulab.net-KIEKER",
		//  folderPath_KiekerLogs + "/kieker-20150730-135303927-UTC-edge2.rssreader.rssperf.emulab.net-KIEKER",
		//	folderPath_KiekerLogs + "/kieker-20150730-135316692-UTC-middletier.rssreader.rssperf.emulab.net-KIEKER",
		//	folderPath_KiekerLogs + "/kieker-20150730-135316914-UTC-middletier2.rssreader.rssperf.emulab.net-KIEKER",
		//	folderPath_KiekerLogs + "/kieker-20150730-135316975-UTC-middletier3.rssreader.rssperf.emulab.net-KIEKER",
		//	folderPath_KiekerLogs + "/kieker-20150730-135327483-UTC-eureka.rssreader.rssperf.emulab.net-KIEKER",
		//folderPath_KiekerLogs + "/kieker-20161202-181144967-UTC-KIEKER-SINGLETON-Thread-1"
			folderPath_KiekerLogs + "/kieker-20161202-171200365-UTC-KIEKER"	
	};

	// for testing
	public static void main(final String[] args) throws IllegalStateException, AnalysisConfigurationException {
		LinkedBlockingQueue<Trace> traceList = new LinkedBlockingQueue<Trace>();
		runAnalysis(traceList, trace_folders);
		System.out.println("Traces: "  + traceList.size());
	}

	public static void runAnalysis(LinkedBlockingQueue<Trace> traceList) throws AnalysisConfigurationException {
		runAnalysis(traceList, trace_folders);
	}

	public static void runAnalysis(LinkedBlockingQueue<Trace> traceList, String[] traceFolders) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the list reader
		Configuration fsReaderConfig = new Configuration();
		fsReaderConfig.setStringArrayProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, traceFolders);
		final FSReader reader = new FSReader(fsReaderConfig, analysisController);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(), analysisController);

		// Initialize, register and connect the execution record transformation
		// filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(new Configuration(),
				analysisController);
		analysisController.connect(executionRecordTransformationFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, executionRecordTransformationFilter,
				ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register and connect the trace reconstruction filter
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration(), analysisController);
		analysisController.connect(traceReconstructionFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordTransformationFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		// Initialize, register and connect the session reconstruction filter
		final Configuration bareSessionReconstructionFilterConfiguration = new Configuration();
		bareSessionReconstructionFilterConfiguration.setProperty(SessionReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_THINK_TIME,
				SessionReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_THINK_TIME);
		
		// use OPENxtraceFilter to reconstruct trace into 'OPEN.xtrace'
		final OPENxtraceFilter filter = new OPENxtraceFilter(new Configuration(), analysisController);
		filter.setTraceQueue(traceList);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, filter,
				OPENxtraceFilter.INPUT_PORT_NAME_EVENTS);

		analysisController.run();
	}
}