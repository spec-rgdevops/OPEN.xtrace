package org.spec.research.open.xtrace.dflt.impl.core;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.MobileRemoteMeasurement;
import org.spec.research.open.xtrace.api.utils.StringUtils;

public class MobileRemoteMeasurementImpl implements MobileRemoteMeasurement, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2227327843670400832L;

	/**
     * Wlan pattern for the connection of the network.
     */
    private static final String WLAN_PATTERN = "WLAN";
	
	/**
     * Use case name of the measurement.
     */
    private Optional<String> useCaseName = Optional.empty();
    
    /**
     * Use case ID of the measurement.
     */
    private Optional<Long> useCaseID = Optional.empty();
    
    /**
     * Timestamp of the measurement.
     */
    private Optional<Long> timestamp = Optional.empty();
    
    /**
     * Flag if the remote call had a timeout exception.
     */
    private Optional<Boolean> timeout = Optional.empty();
	
	/**
	 * Response code.
	 */
	private Optional<Long> responseCode = Optional.empty();
    
    /**
     * Longitude from the position of the mobile device at the time of the measurement.
     */
    private Optional<Double> longitude = Optional.empty();
    
    /**
     * Latitude from the position of the mobile device at the time of the measurement.
     */
    private Optional<Double> latitude = Optional.empty();
    
    /**
     * Url of the request.
     */
    private Optional<String> url = Optional.empty();
    
    /**
     * SSID of the network of the mobile device at the time of the measurement.
     */
    private Optional<String> ssid = Optional.empty();
    
    /**
     * Connection of the network of the mobile device at the time of the measurement.
     */
    private Optional<String> networkConnection = Optional.empty();
    
    /**
     * Provider of the network of the mobile device at the time of the measurement.
     */
    private Optional<String> networkProvider = Optional.empty();
	
    /**
     * Default constructor.
     */
    public MobileRemoteMeasurementImpl() {
	}
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Long> getTimestamp() {
		return this.timestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getNetworkProvider() {
		return this.networkProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Boolean> getTimeout() {
		return this.timeout;
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
	public Optional<Double> getLongitude() {
		return longitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Double> getLatitude() {
		return latitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getNetworkConnection() {
		return networkConnection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Long> getResponseCode() {
		return responseCode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getUrl() {
		return url;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getSsid() {
		return ssid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Boolean> isWlanActive() {
		if(networkConnection.isPresent()){
			return Optional.of(networkConnection.get().equalsIgnoreCase(WLAN_PATTERN));
		}
		return Optional.empty();
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
	 * Sets the timestamp of this measurement.
	 * 
	 * @param timestamp timestamp of the measurement
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = Optional.ofNullable(timestamp);
	}
	
	/**
	 * Sets the timeout for this measurement.
	 * 
	 * @param timeout timeout exception flag
	 */
	public void setTimeout(Boolean timeout) {
		this.timeout = Optional.ofNullable(timeout);
	}
	
	/**
	 * Sets the longitude of the mobile device.
	 * 
	 * @param longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = Optional.ofNullable(longitude);
	}
	
	/**
	 * Sets the latitude of the mobile device.
	 * 
	 * @param latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = Optional.ofNullable(latitude);
	}
	
	/**
	 * Sets the response code of the request.
	 * 
	 * @param responseCode
	 */
	public void setResponseCode(Long responseCode) {
		this.responseCode = Optional.ofNullable(responseCode);
	}
	
	/**
	 * Sets the network connection of this measurement.
	 * 
	 * @param networkConnection
	 */
	public void setNetworkConnection(String networkConnection) {
		this.networkConnection = Optional.ofNullable(networkConnection);
	}
	
	/**
	 * Sets the network provider of this measurement.
	 * 
	 * @param networkProvider
	 */
	public void setNetworkProvider(String networkProvider) {
		this.networkProvider = Optional.ofNullable(networkProvider);
	}
	
	/**
	 * Sets the network ssid of this measurement.
	 * 
	 * @param networkProvider
	 */
	public void setSsid(String ssid) {
		this.ssid = Optional.ofNullable(ssid);
	}
	
	/**
	 * Sets the url of this request.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = Optional.ofNullable(url);
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}
