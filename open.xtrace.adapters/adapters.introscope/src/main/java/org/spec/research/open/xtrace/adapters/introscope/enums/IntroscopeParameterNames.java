package org.spec.research.open.xtrace.adapters.introscope.enums;

public enum IntroscopeParameterNames {
	HTTP_METHOD("HTTP Method"), METHOD("Method"), CLASS("Class"), METHOD_DESCRIPTOR("Method Descriptor"), URL("URL"), THREAD_NAME("Thread Name");

	private String attribute;

	private IntroscopeParameterNames(String attributeName) {

		attribute = attributeName;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return attribute;
	}

}
