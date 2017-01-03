package org.spec.research.open.xtrace.adapters.inspectit.source;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.diagnoseit.spike.inspectit.trace.impl.IITTraceImpl;
import org.diagnoseit.spike.inspectit.trace.importer.InvocationSequences;
import org.diagnoseit.spike.inspectit.trace.importer.SerializerWrapper;
import org.diagnoseit.spike.shared.TraceSink;
import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.shared.TraceConverter;

import info.novatec.inspectit.cmr.model.PlatformIdent;
import info.novatec.inspectit.communication.data.InvocationSequenceData;

public class InspectITTraceConverter implements TraceSource, TraceConverter {
	private static final String DATA_PATH = "inspectit.fileimporter.datapath";
	private static Iterator<InvocationSequenceData> isDataIterator = null;
	private static PlatformIdent pIdent = null;
	private TraceSink traceSink;
	private double responseTimeThreshold = -1;

	private void readInvocations(final String path){
		try {
			SerializerWrapper serializer = new SerializerWrapper();
			InvocationSequences iSequences = serializer.readInvocationSequencesFromDir(path);
			isDataIterator = iSequences.iterator();
			pIdent = iSequences.getPlatformIdent();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Trace getNextTrace(){
		if (!isDataIterator.hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		}
		InvocationSequenceData isd = null;

		do {
			isd = isDataIterator.next();
		} while (isDataIterator.hasNext() && isd.getDuration() < responseTimeThreshold);

		if (isd != null) {
			return (new IITTraceImpl(isd, pIdent));
		}
		throw new NoSuchElementException("Iterator reached the end!");
	}
	
	@Override
	public void initialize(Properties properties, TraceSink traceSink) {
		String dataPath = properties.getProperty(DATA_PATH);
		if (dataPath == null) {
			throw new IllegalArgumentException("Data path has not been specified for the inspectIT file importer trace source.");
		}
		this.traceSink = traceSink;
		this.readInvocations(dataPath);

		String rtThresholdStr = properties.getProperty(RESPONSETIME_THRESHOLD);
		if (rtThresholdStr == null) {
			rtThresholdStr = String.valueOf(RESPONSETIME_THRESHOLD_DEFAULT);
			System.out.println("No response time threshold specified. Using default value: 1000ms!");
		}
		responseTimeThreshold = Double.parseDouble(rtThresholdStr);

	}

	@Override
	public boolean isManualSource() {
		return true;
	}

	@Override
	public Trace submitNextTrace() {
		Trace trace = getNextTrace();
		traceSink.appendTrace(trace);
		return trace;
	}

	@Override
	public void startTraceGeneration() {
		throw new UnsupportedOperationException("This operation is NOT available for manual trace sources!");

	}

	@Override
	public void stopTraceGeneration() {
		throw new UnsupportedOperationException("This operation is NOT available for manual trace sources!");

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
		
		if (path == null) {
			throw new IllegalArgumentException("Data path has not been specified for the inspectIT file importer trace source.");
		}

		List<Trace> listConvertedTraces = new LinkedList<Trace>();
		
		this.readInvocations(path);

		responseTimeThreshold = RESPONSETIME_THRESHOLD_DEFAULT;

		Trace trace = getNextTrace();
		if(trace != null){
			listConvertedTraces.add(trace);
		}
		
		return listConvertedTraces;
	}

}
