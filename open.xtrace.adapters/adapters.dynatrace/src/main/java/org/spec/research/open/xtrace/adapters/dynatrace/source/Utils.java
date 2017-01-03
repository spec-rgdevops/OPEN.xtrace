package org.spec.research.open.xtrace.adapters.dynatrace.source;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spec.research.open.xtrace.adapters.dynatrace.enums.AttachmentType;
import org.spec.research.open.xtrace.adapters.dynatrace.enums.PropertyKey;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Attachment;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Node;

/**
 * Utility methods for processing the data.
 *
 * @author Christoph Heger, Manuel Palenga
 * @since 09.11.2015
 */
class Utils {

	/**
	 * Extracts the time value from the given {@link String} of the breakdown
	 * attribute and the provided measurement string.
	 *
	 * @param breakdown
	 *            the breakdown attribute value String
	 * @param measurement
	 *            Measurement string from the breakdown string
	 * 
	 * @return the time value of the provided measurement
	 */
	protected static long extractTime(final String breakdown, final String measurement) {

		Pattern pattern = Pattern.compile(measurement + " Total: (.*?) ms");
		Matcher matcher = pattern.matcher(breakdown);

		long value = -1;

		if (matcher.find()) {
			String strTime = matcher.group(1);
			strTime = strTime.replaceAll(",", "");
			value = (long) (Double.valueOf(strTime) * PurepathTransformer.MILLIS_TO_NANOS_FACTOR);
		}

		return value;
	}

	/**
	 * Return the attachment from the giving node with the giving type.
	 * 
	 * @param node
	 *            Get attachment of this node
	 * @param type
	 *            Type of the attachment
	 * @return Attachment
	 */
	protected static Attachment getAttachment(final Node node, final AttachmentType type) {

		for (Attachment attachment : node.getAttachment()) {
			if (attachment.getType().equals(type.toString())) {
				return attachment;
			}
		}

		return null;
	}

	/**
	 * Get the value of the attachment field.
	 * 
	 * @param attachment
	 *            Get value of this attachment
	 * @param key
	 *            Key of the field
	 * @return value of the attachment field
	 */
	protected static Optional<String> getValueOfAttachmentKey(final Attachment attachment, final String key) {

		if (attachment == null) {
			return Optional.empty();
		}

		for (Attachment.Property property : attachment.getProperty()) {
			if (property.getKey().equals(key)) {
				return Optional.of(property.getValue());
			}
		}
		return Optional.empty();
	}

	/**
	 * Get the value of the attachment with the giving key.
	 * 
	 * @param attachment
	 *            Get value of this attachment
	 * @param key
	 *            Key of the field
	 * @return value of the attachment field
	 */
	protected static Optional<String> getValueOfAttachmentKey(final Attachment attachment, final PropertyKey key) {

		return getValueOfAttachmentKey(attachment, key.toString());
	}

	/**
	 * Check attachment with the giving type and then return the value of the
	 * attachment with the giving key.
	 * 
	 * @param attachment
	 *            Get value of this attachment
	 * @param type
	 *            Type of the attachment
	 * @param key
	 *            Key of the field
	 * @return value of the attachment field
	 */
	protected static Optional<String> getValueOfAttachmentTypeAndKey(final Node node, final AttachmentType type, final PropertyKey key) {

		Attachment attachment = getAttachment(node, type);
		if (attachment != null) {
			return getValueOfAttachmentKey(attachment, key);
		}
		return Optional.empty();
	}

}
