package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.MobileRemoteMeasurement;
import org.spec.research.open.xtrace.api.utils.StringUtils;


public class IITMobileRemoteMeasurement implements MobileRemoteMeasurement, Serializable {

	/** Serial version id. */
	private static final long serialVersionUID = -4546457266637845392L;
	
	/**
     * Wlan pattern for the connection of the network.
     */
    private static final String WLAN_PATTERN = "WLAN";
    
    private static final String URL = "url";
	private static final String SSID = "ssid";
	private static final String NETWORKCONNECTION = "networkConnection";
	private static final String NETWORKPROVIDER = "networkProvider";
	private static final String TIMEOUT = "timeout";
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String RESPONSECODE = "responseCode";
    
	private Map<String, String> remoteMeasurement;
	private Long timestamp;
	
	public IITMobileRemoteMeasurement(Long timestamp, Map<String, String> remoteMeasurement) {
		this.timestamp = timestamp;
		this.remoteMeasurement = remoteMeasurement;
	}

	@Override
	public Optional<Double> getLatitude() {
		if(!remoteMeasurement.containsKey(LATITUDE)){
			return Optional.empty();
		}
		return Optional.ofNullable(Double.parseDouble(remoteMeasurement.get(LATITUDE)));
	}

	@Override
	public Optional<Double> getLongitude() {
		if(!remoteMeasurement.containsKey(LONGITUDE)){
			return Optional.empty();
		}
		return Optional.ofNullable(Double.parseDouble(remoteMeasurement.get(LONGITUDE)));
	}

	@Override
	public Optional<String> getNetworkConnection() {
		return Optional.ofNullable(remoteMeasurement.get(NETWORKCONNECTION));
	}

	@Override
	public Optional<Long> getUseCaseID() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getUseCaseName() {
		return Optional.empty();
	}
	
	public Optional<String> getUrl() {
		if(!remoteMeasurement.containsKey(URL)){
			return Optional.empty();
		}
		return Optional.ofNullable(remoteMeasurement.get(URL));
	}

	@Override
	public Optional<Long> getTimestamp() {
		return Optional.ofNullable(timestamp);
	}

	@Override
	public Optional<Long> getResponseCode() {
		if(!remoteMeasurement.containsKey(RESPONSECODE)){
			return Optional.empty();
		}
		return Optional.ofNullable(Long.parseLong(remoteMeasurement.get(RESPONSECODE)));
	}

	@Override
	public Optional<String> getSsid() {
		return Optional.ofNullable(remoteMeasurement.get(SSID));
	}

	@Override
	public Optional<String> getNetworkProvider() {
		return Optional.ofNullable(remoteMeasurement.get(NETWORKPROVIDER));
	}

	@Override
	public Optional<Boolean> getTimeout() {
		if(!remoteMeasurement.containsKey(TIMEOUT)){
			return Optional.empty();
		}
		return Optional.ofNullable(Boolean.parseBoolean(remoteMeasurement.get(TIMEOUT)));
	}

	@Override
	public Optional<Boolean> isWlanActive() {
		if(getNetworkConnection().isPresent()){
			return Optional.of(getNetworkConnection().get().equalsIgnoreCase(WLAN_PATTERN));
		}
		return Optional.empty();
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}
