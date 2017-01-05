package org.diagnoseit.spike.inspectit.trace.importer;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.diagnoseit.spike.inspectit.trace.impl.IITTraceImpl;
import org.diagnoseit.spike.shared.TraceSink;
import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.api.core.Trace;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class InvocationSequenceFileImporter implements TraceSource {
	private static final String DATA_PATH = "inspectit.fileimporter.datapath";
	private static Iterator<InvocationSequenceData> isDataIterator = null;
	private static PlatformIdent pIdent = null;
	private TraceSink traceSink;
	private double responseTimeThreshold = -1;

	@Override
	public void initialize(Properties properties, TraceSink traceSink) {
		String dataPath = properties.getProperty(DATA_PATH);
		if (dataPath == null) {
			throw new IllegalArgumentException("Data path has not been specified for the inspectIT file importer trace source.");
		}
		this.traceSink = traceSink;
		try {
			SerializerWrapper serializer = new SerializerWrapper();
			InvocationSequences iSequences = serializer.readInvocationSequencesFromDir(dataPath);
			isDataIterator = iSequences.iterator();
			pIdent = iSequences.getPlatformIdent();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		if (!isDataIterator.hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		}
		InvocationSequenceData isd = null;

		do {
			isd = isDataIterator.next();
		} while (isDataIterator.hasNext() && isd.getDuration() * Trace.MILLIS_TO_NANOS_FACTOR < responseTimeThreshold);

		if (!isDataIterator.hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		} else {
			Trace trace = new IITTraceImpl(isd, pIdent);

			traceSink.appendTrace(trace);

			return trace;
		}

	}

	@Override
	public void startTraceGeneration() {
		throw new UnsupportedOperationException("This operation is NOT available for manual trace sources!");

	}

	@Override
	public void stopTraceGeneration() {
		throw new UnsupportedOperationException("This operation is NOT available for manual trace sources!");

	}

}
