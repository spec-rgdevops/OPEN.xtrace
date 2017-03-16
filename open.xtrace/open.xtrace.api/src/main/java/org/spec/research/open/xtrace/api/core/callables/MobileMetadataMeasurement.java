package org.spec.research.open.xtrace.api.core.callables;

import java.util.Optional;

/**
 * 
 * Represents a mobile measurement of metadata from a mobile device in a trace / sub-trace.
 * 
 * @author Manuel Palenga
 */
public interface MobileMetadataMeasurement extends NestingCallable {

	/**
     * Return the use case name of this mobile measurement.
     *
     * @return an {@link Optional} with use case name. Empty {@link Optional} if not present.
     */
	Optional<String> getUseCaseName();
	
	/**
     * Return the use case id of this mobile measurement.
     *
     * @return an {@link Optional} with use case id. Empty {@link Optional} if not present.
     */
	Optional<Long> getUseCaseID();

	/**
     * Return the cpu usage of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with cpu usage in percent as value. Empty {@link Optional} if not present.
     */
	Optional<Double> getCPUUsage();
	
	/**
     * Return the memory usage of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with memory usage in percent as value. Empty {@link Optional} if not present.
     */
	Optional<Double> getMemoryUsage();
	
	/**
     * Return the storage usage of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with storage usage in percent as value. Empty {@link Optional} if not present.
     */
	Optional<Double> getStorageUsage();
	
	/**
     * Return the battery power of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with battery power in percent as value. Empty {@link Optional} if not present.
     */
	Optional<Double> getBatteryPower();
	
}
