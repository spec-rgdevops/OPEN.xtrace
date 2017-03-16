package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.util.Optional;
import java.util.UUID;

import org.spec.research.open.xtrace.api.core.callables.MobileMetadataMeasurement;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;


public class IITMobileMetaMeasurementCallable extends IITAbstractNestingCallable implements MobileMetadataMeasurement {

	/** Serial version id. */
	private static final long serialVersionUID = -4859642073428205883L;
	private MobilePeriodicMeasurement mobilePeriodicData;
	private Long usecaseID;
	private String usecaseName;
	
	public IITMobileMetaMeasurementCallable(IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent,
			Long usecaseID, String usecaseName, MobilePeriodicMeasurement periodicMeasurement, InvocationSequenceData data) {
		super(data, containingTrace, parent);
		this.setIdentifier(UUID.randomUUID());
		this.usecaseID = usecaseID;
		this.usecaseName = usecaseName;
		this.mobilePeriodicData = periodicMeasurement;
	}

	@Override
	public Optional<Double> getBatteryPower() {
		if(mobilePeriodicData == null){
			return Optional.empty();
		}
		return Optional.ofNullable(mobilePeriodicData.getBatteryPower());
	}

	@Override
	public Optional<Double> getCPUUsage() {
		if(mobilePeriodicData == null){
			return Optional.empty();
		}
		return Optional.ofNullable(mobilePeriodicData.getCpuUsage());
	}

	@Override
	public Optional<Double> getMemoryUsage() {
		if(mobilePeriodicData == null){
			return Optional.empty();
		}
		return Optional.ofNullable(mobilePeriodicData.getMemoryUsage());
	}
	
	@Override
	public Optional<Double> getStorageUsage() {
		if(mobilePeriodicData == null){
			return Optional.empty();
		}
		return Optional.ofNullable(mobilePeriodicData.getStorageUsage());
	}

	@Override
	public Optional<Long> getUseCaseID() {
		if(usecaseID == null){
			if(containingTrace.getRoot() instanceof MobileMetadataMeasurement){
				MobileMetadataMeasurement measurement = (MobileMetadataMeasurement) containingTrace.getRoot();
				return measurement.getUseCaseID();
			}
		}
		return Optional.ofNullable(usecaseID);
	}

	@Override
	public Optional<String> getUseCaseName() {
		if(usecaseID == null){
			if(containingTrace.getRoot() instanceof MobileMetadataMeasurement){
				MobileMetadataMeasurement measurement = (MobileMetadataMeasurement) containingTrace.getRoot();
				return measurement.getUseCaseName();
			}
		}
		return Optional.ofNullable(usecaseName);
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}
