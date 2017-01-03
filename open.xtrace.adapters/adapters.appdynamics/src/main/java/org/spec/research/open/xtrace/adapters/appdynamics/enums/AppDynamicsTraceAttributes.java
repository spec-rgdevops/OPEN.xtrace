package org.spec.research.open.xtrace.adapters.appdynamics.enums;

public enum AppDynamicsTraceAttributes {
	METHOD("method"), TOTAL_TIME("total_time"), EXCLUSIVE_TIME("exclusive_time"), CLASS("class");

	private String attribute;

	private AppDynamicsTraceAttributes(String attribute) {

		this.attribute = attribute;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return attribute;
	}
}