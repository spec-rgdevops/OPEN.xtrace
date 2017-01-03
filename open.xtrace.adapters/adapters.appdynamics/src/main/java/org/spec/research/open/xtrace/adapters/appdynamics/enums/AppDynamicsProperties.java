package org.spec.research.open.xtrace.adapters.appdynamics.enums;

public enum AppDynamicsProperties {
	ON_TIER("On tier:"), ON_NODE("On node:"), START_TIME("Start time:");

	private String property;

	private AppDynamicsProperties(String property) {

		this.property = property;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return property;
	}
}