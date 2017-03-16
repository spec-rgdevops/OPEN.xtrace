package org.spec.research.open.xtrace.api.core;

import java.util.Optional;

/**
 * 
 * Represents a mobile remote measurement of metadata from a mobile device.
 * 
 * @author Manuel Palenga
 */
public interface MobileRemoteMeasurement {

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
     * Returns the timestamp of this mobile measurements.
     *
     * @return an {@link Optional} with timestamp. Empty {@link Optional} if not present.
     */
    Optional<Long> getTimestamp();
	
	/**
     * Return the longitude from the position of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with longitude. Empty {@link Optional} if not present.
     */
	Optional<Double> getLongitude();
	
	/**
     * Return the latitude from the position of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with latitude. Empty {@link Optional} if not present.
     */
	Optional<Double> getLatitude();
	
    /**
     * Returns the url of the request.
     *
     * @return an {@link Optional} with the url of the request.
     */
    Optional<String> getUrl();
	
    /**
     * Returns the response code of the request.
     *
     * @return an {@link Optional} with the response code of the request.
     */
    Optional<Long> getResponseCode();
	
	/**
     * Return the SSID of the network of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with the SSID of the network. Empty {@link Optional} if not present.
     */
	Optional<String> getSsid();
	    
	/**
     * Return the connection of the network of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with the connection of the network. Empty {@link Optional} if not present.
     */
	Optional<String> getNetworkConnection();
	
	/**
     * Return the provider of the network of the mobile device at the time of the measurement.
     *
     * @return an {@link Optional} with the provider of the network. Empty {@link Optional} if not present.
     */
	Optional<String> getNetworkProvider();
	
	/**
     * Return a flag of a timeout if this remote call had a timeout exception or not.
     *
     * @return an {@link Optional} with a flag of a timeout. Empty {@link Optional} if not present.
     */
	Optional<Boolean> getTimeout();
	
	/**
     * Returns <code>true</code> when the mobile device is in Wlan at the time of the measurement.
     *
     * @return an {@link Optional} with <code>true</code> as value if the mobile device was in Wlan at the time of the measurement, otherwise
     * <code>false</code>. Empty {@link Optional} if not present.
     */
	Optional<Boolean> isWlanActive();
	
}
