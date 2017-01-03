package org.spec.research.open.xtrace.adapters.introscope.enums;

public enum IntroscopeAttributes {
	SUB_TRACE_TIMESTAMP("StartDate"), LOCATION_HOST("Host"), LOCATION_RUNTIME("Process"), REL_TIMESTAMP("RelativeTimestamp"), CALLABLE_COMPONENT_NAME(
			"ComponentName"), INFORMATION_NAME("Name"), INFORMATION_VALUE("Value"), DURATION("Duration"), TIME_UNITS("TimeUnits");

	private String attribute;

	private IntroscopeAttributes(String attributeName) {

		attribute = attributeName;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return attribute;
	}
}
