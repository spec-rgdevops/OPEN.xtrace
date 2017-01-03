package org.diagnoseit.spike.inspectit.bridge;
/*
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.diagnoseit.spike.inspectit.trace.impl.IITTraceImpl;
import org.diagnoseit.spike.shared.TraceSink;
import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.api.core.Trace;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.cmr.AgentStatusData;
*/
public class InspectITBridge {

	/*
	 public class InspectITBridge implements Runnable, TraceSource {
	 
	private static final String HOST_KEY = "inspectit.bridge.host";
	private static final String PORT_KEY = "inspectit.bridge.port";

	private CmrRepositoryDefinition cmrRepository;
	private boolean run = false;
	private TraceSink traceSink;
	private double responseTimeThreshold = -1;

	private void connectToCMR(String host, int port) {

		cmrRepository = new CmrRepositoryDefinition(host, port);
		cmrRepository.refreshOnlineStatus();
		if (cmrRepository.getOnlineStatus() != OnlineStatus.ONLINE) {
			throw new RuntimeException("CMR is offline!");
		}
		System.out.println("inspectIT Bridge successfully connected to CMR!");
	}

	@Override
	public void run() {
		run = true;

		Map<Long, Date> fromDates = new HashMap<Long, Date>();
		IInvocationDataAccessService invocationDataService = cmrRepository.getInvocationDataAccessService();
		while (run) {
			Map<PlatformIdent, AgentStatusData> agents = cmrRepository.getGlobalDataAccessService().getAgentsOverview();

			for (PlatformIdent pIdent : agents.keySet()) {
				Date fromDate = pollTraces(fromDates.get(pIdent.getId()), pIdent.getId(), invocationDataService);
				fromDates.put(pIdent.getId(), fromDate);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}

		}

	}

	private Date pollTraces(Date fromDate, long pId, IInvocationDataAccessService invocationDataService) {
		long sumTraceCreation = 0;
		long sumISOverview = 0;
		long sumISDetail = 0;
		long startISOverview = System.currentTimeMillis();
		List<InvocationSequenceData> isdOverview = invocationDataService.getInvocationSequenceOverview(pId, 50, fromDate, null, null);
		sumISOverview += System.currentTimeMillis() - startISOverview;
		int remainedCount = 0;

		for (InvocationSequenceData isdStub : isdOverview) {

			if (isdStub.getDuration() * Trace.MILLIS_TO_NANOS_FACTOR >= responseTimeThreshold) {
				long startISDetail = System.currentTimeMillis();
				InvocationSequenceData isd = cmrRepository.getInvocationDataAccessService().getInvocationSequenceDetail(isdStub);
				sumISDetail += System.currentTimeMillis() - startISDetail;
				long startTraceCreation = System.currentTimeMillis();
				Trace trace = new IITTraceImpl(isd, cmrRepository.getCachedDataService());
				sumTraceCreation += System.currentTimeMillis() - startTraceCreation;
				// System.out.println(trace);
				traceSink.appendTrace(trace);
				remainedCount++;
			}

			Timestamp timestamp = isdStub.getTimeStamp();
			if (fromDate == null || timestamp.getTime() >= fromDate.getTime()) {
				fromDate = new Date(timestamp.getTime() + 1);
			}

		}
		if (!isdOverview.isEmpty()) {
			System.out.println("Polled " + isdOverview.size() + " traces. After filtering remained " + remainedCount + " traces.");
		}
		if (sumISOverview + sumISDetail + sumTraceCreation > 500) {
			System.out.println("Retrieve Overview: " + sumISOverview + "ms");
			System.out.println("Retrieve Detail: " + sumISDetail + "ms");
			System.out.println("Create Trace: " + sumTraceCreation + "ms");
		}

		return fromDate;
	}

	@Override
	public void initialize(Properties properties, TraceSink traceSink) {
		this.traceSink = traceSink;
		String host = properties.getProperty(HOST_KEY);
		if (host == null) {
			throw new IllegalArgumentException("CMR host has not been specified for the inspectIT Bridge trace source!");
		}
		String strPort = properties.getProperty(PORT_KEY);
		if (strPort == null) {
			throw new IllegalArgumentException("CMR host has not been specified for the inspectIT Bridge trace source!");
		}
		int port = Integer.parseInt(strPort);
		connectToCMR(host, port);

		String rtThresholdStr = properties.getProperty(RESPONSETIME_THRESHOLD);
		if (rtThresholdStr == null) {
			rtThresholdStr = String.valueOf(RESPONSETIME_THRESHOLD_DEFAULT);
			System.out.println("No response time threshold specified. Using default value: 1000ms!");
		}
		responseTimeThreshold = Double.parseDouble(rtThresholdStr);
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
		run = false;
	}
	*/
}
