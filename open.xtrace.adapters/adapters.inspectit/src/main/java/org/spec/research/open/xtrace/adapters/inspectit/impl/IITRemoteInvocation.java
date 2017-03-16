package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.MobileRemoteMeasurement;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITRemoteInvocation extends IITAbstractTimedCallable implements RemoteInvocation {

	/** Serial version id. */
	private static final long serialVersionUID = -4775875155831584749L;
	private IITSubTraceImpl targetSubTrace;
	private String target;
	private MobileRemoteMeasurement requestMeasurement;
	private MobileRemoteMeasurement responseMeasurement;

	public IITRemoteInvocation(InvocationSequenceData isData, IITSubTraceImpl containingSubTrace, IITAbstractNestingCallable parent) {
		super(isData, containingSubTrace, parent);
	}

	public void setTargetSubTrace(IITSubTraceImpl targetSubTrace) {
		this.targetSubTrace = targetSubTrace;
	}
	
	@Override
	public Optional<SubTrace> getTargetSubTrace() {
		return Optional.ofNullable(targetSubTrace);
	}

	@Override
	public Optional<Location> getTargetLocation() {
		return Optional.ofNullable(targetSubTrace.getLocation());
	}

	@Override
	public String getTarget() {
		if (targetSubTrace != null && target == null) {
			Location targetLocation = targetSubTrace.getLocation();
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(targetLocation.getHost());
			strBuilder.append(" | ");
			strBuilder.append(targetLocation.getRuntimeEnvironment());
			strBuilder.append(" | ");
			strBuilder.append(targetLocation.getApplication());
			strBuilder.append(" | ");
			strBuilder.append(targetLocation.getBusinessTransaction());
			return strBuilder.toString();
		}
		return target;
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public Optional<MobileRemoteMeasurement> getRequestMeasurement() {
		return Optional.ofNullable(requestMeasurement);
	}

	@Override
	public Optional<MobileRemoteMeasurement> getResponseMeasurement() {
		return Optional.ofNullable(responseMeasurement);
	}
	
	protected void setRequestMeasurement(MobileRemoteMeasurement requestMeasurement){
		this.requestMeasurement = requestMeasurement;
	}
	
	protected void setResponseMeasurement(MobileRemoteMeasurement responseMeasurement){
		this.responseMeasurement = responseMeasurement;
	}

}
