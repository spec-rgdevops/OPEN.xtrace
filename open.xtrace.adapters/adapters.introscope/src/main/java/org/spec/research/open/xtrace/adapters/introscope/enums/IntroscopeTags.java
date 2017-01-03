package org.spec.research.open.xtrace.adapters.introscope.enums;


public enum IntroscopeTags {
	SUB_TRACE("TransactionTrace"), CALLED_COMPONENT_LIST("CalledComponents"), CALLED_COMPONENT("CalledComponent"), PARAMETER_LIST("Parameters"), PARAMETER(
			"Parameter"), TRANSACTION_TRACER_SESSION("TransactionTracerSession");

	private String tag;

	private IntroscopeTags(String tagName) {

		tag = tagName;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return tag;
	}

	public static IntroscopeTags fromName(String name) {

		for (IntroscopeTags tag : IntroscopeTags.values()) {
			if (tag.getName().equals(name)) {
				return tag;
			}
		}
		return null;
	}

	@Override
	public String toString() {

		return tag;
	}
}
