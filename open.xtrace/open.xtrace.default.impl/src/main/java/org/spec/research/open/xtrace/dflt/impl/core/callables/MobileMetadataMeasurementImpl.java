package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.MobileMetadataMeasurement;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

public class MobileMetadataMeasurementImpl extends AbstractNestingCallableImpl implements MobileMetadataMeasurement, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3176266322090859669L;
	
	/**
     * Use case name of the measurement.
     */
    private Optional<String> useCaseName = Optional.empty();
    
    /**
     * Use case ID of the measurement.
     */
    private Optional<Long> useCaseID = Optional.empty();
    
    /**
     * Cpu usage of the mobile device at the time of the measurement.
     */
    private Optional<Double> cpuUsage = Optional.empty();
    
    /**
     * Memory usage of the mobile device at the time of the measurement.
     */
    private Optional<Double> memoryUsage = Optional.empty();
    
    /**
     * Storage usage of the mobile device at the time of the measurement.
     */
    private Optional<Double> storageUsage = Optional.empty();
    
    /**
     * Battery power of the mobile device at the time of the measurement.
     */
    private Optional<Double> batteryPower = Optional.empty();
    	
    public MobileMetadataMeasurementImpl() {
	}
    
    /**
     * Constructor. Adds the newly created {@link Callable} instance to the passed parent if the parent is not null!
     * 
     * @param parent
     *            {@link AbstractNestingCallableImpl} that called this Callable
     * @param containingSubTrace
     *            the SubTrace containing this Callable
     */
    public MobileMetadataMeasurementImpl(AbstractNestingCallableImpl parent,
            SubTraceImpl containingSubTrace) {

        super(parent, containingSubTrace);
    }
	
    /**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getUseCaseName() {
		return useCaseName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Long> getUseCaseID() {
		return useCaseID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Double> getCPUUsage() {
		return cpuUsage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Double> getMemoryUsage() {
		return memoryUsage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Double> getStorageUsage() {
		return storageUsage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Double> getBatteryPower() {
		return batteryPower;
	}
	
	/**
	 * Sets the use case name for this measurement.
	 * 
	 * @param useCaseName use case name
	 */
	public void setUseCaseName(String useCaseName) {
		this.useCaseName = Optional.ofNullable(useCaseName);
	}
	
	/**
	 * Sets the use case id for this measurement.
	 * 
	 * @param useCaseID use case id
	 */
	public void setUseCaseID(Long useCaseID) {
		this.useCaseID = Optional.ofNullable(useCaseID);
	}

	/**
	 * Sets the cpu usage of this measurement.
	 * 
	 * @param cpuUsage
	 */
	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = Optional.ofNullable(cpuUsage);
	}
	
	/**
	 * Sets the memory usage of this measurement.
	 * 
	 * @param memoryUsage
	 */
	public void setMemoryUsage(Double memoryUsage) {
		this.memoryUsage = Optional.ofNullable(memoryUsage);
	}
	
	/**
	 * Sets the storage usage of this measurement.
	 * 
	 * @param storageUsage
	 */
	public void setStorageUsage(Double storageUsage) {
		this.storageUsage = Optional.ofNullable(storageUsage);
	}
	
	/**
	 * Sets the battery power of this measurement.
	 * 
	 * @param batteryPower
	 */
	public void setBatteryPower(Double batteryPower) {
		this.batteryPower = Optional.ofNullable(batteryPower);
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}
