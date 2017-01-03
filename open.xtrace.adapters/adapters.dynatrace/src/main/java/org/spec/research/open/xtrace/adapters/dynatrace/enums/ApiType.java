package org.spec.research.open.xtrace.adapters.dynatrace.enums;


public enum ApiType {

	JDBC("JDBC"), EXCEPTION("Exception"), SERVLET("Servlet"), LOG4J("Log4J");

	private String tag;

	private ApiType(String tagName) {

		tag = tagName;
	}

	public static ApiType fromName(String name) {

		for (ApiType tag : ApiType.values()) {
			if (tag.getName().equals(name)) {
				return tag;
			}
		}
		return null;
	}

	/**
	 * @return the tag
	 */
	public String getName() {

		return tag;
	}

	@Override
	public String toString() {

		return tag;
	}
}
