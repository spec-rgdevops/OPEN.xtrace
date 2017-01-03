package org.spec.research.open.xtrace.adapters.dynatrace.enums;

public enum PropertyKey {
	RESPONSE_STATUS("responsestatus"), REQUEST_METHOD("requestmethod"), URI("uri"), URL("url"), DURATION("duration"), REASON("reason");

	private String key;

	private PropertyKey(String key) {

		this.key = key;
	}

	@Override
	public String toString() {

		return key;
	}
}
