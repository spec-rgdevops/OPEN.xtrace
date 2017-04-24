package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.spec.research.open.xtrace.api.core.Trace;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

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
	/**
	 * Converts traces from Monitoring Log in specified folder and stores them into a list.
	 * 
	 * @param traceList
	 *            a list into which the traces are stored
	 * @param traceFolders
	 *            folder with Monitoring Log
	 * @throws AnalysisConfigurationException
	 *             if there was an error when configuring filters
	 */
	public static void runAnalysis(List<Trace> traceList, String[] traceFolders)
			throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		// Initialize and register the list reader
		Configuration fsReaderConfig = new Configuration();
		fsReaderConfig.setStringArrayProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, traceFolders);
		final FSReader reader = new FSReader(fsReaderConfig, analysisController);

		// Initialize and register the system model repository
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(),
				analysisController);

		// Initialize, register and connect the execution record transformation
		// filter
		final ExecutionRecordTransformationFilter executionRecordTransformationFilter = new ExecutionRecordTransformationFilter(
				new Configuration(), analysisController);
		analysisController.connect(executionRecordTransformationFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, executionRecordTransformationFilter,
				ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		// Initialize, register and connect the trace reconstruction filter
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration(),
				analysisController);
		analysisController.connect(traceReconstructionFilter,
				AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(executionRecordTransformationFilter,
				ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, traceReconstructionFilter,
				TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);

		// Initialize, register and connect the session reconstruction filter
		final Configuration bareSessionReconstructionFilterConfiguration = new Configuration();
		bareSessionReconstructionFilterConfiguration.setProperty(
				SessionReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_THINK_TIME,
				SessionReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_THINK_TIME);

		// use OPENxtraceFilter to reconstruct trace into 'OPEN.xtrace'
		final OPENxtraceFilter filter = new OPENxtraceFilter(new Configuration(), analysisController);
		filter.setTraceQueue(traceList);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				filter, OPENxtraceFilter.INPUT_PORT_NAME_EVENTS);

		analysisController.run();
	}
}