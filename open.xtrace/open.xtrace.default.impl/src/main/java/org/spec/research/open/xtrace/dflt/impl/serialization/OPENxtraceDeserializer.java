package org.spec.research.open.xtrace.dflt.impl.serialization;

import java.io.InputStream;

import org.spec.research.open.xtrace.api.core.Trace;

/**
 * Deserializer interface for 'OPEN.xtrace'.
 * 
 * @author Alexander Wert
 *
 */
public interface OPENxtraceDeserializer {

	/**
	 * Sets the source to read from.
	 * 
	 * <b>Note:</b> after deserializing the <b>close()</b> method needs to be
	 * called!
	 * 
	 * @param inStream
	 *            input stream to read from
	 */
	void setSource(InputStream inStream);

	/**
	 * Reads the next trace from the source. If the deserializer has reached the
	 * end of the stream, this method returns null.
	 * 
	 * @return next {@link Trace} instance. If next trace is not available this
	 *         method returns null.
	 */
	Trace readNext();

	/**
	 * Cleans up deserializer.
	 */
	void close();
}
