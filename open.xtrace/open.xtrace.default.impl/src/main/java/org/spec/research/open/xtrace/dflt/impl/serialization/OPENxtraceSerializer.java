package org.spec.research.open.xtrace.dflt.impl.serialization;

import java.io.OutputStream;

import org.spec.research.open.xtrace.api.core.Trace;

/**
 * Serializer interface for 'OPEN.xtraces'.
 * 
 * @author Alexander Wert
 *
 */
public interface OPENxtraceSerializer {

	/**
	 * Prepares serialization.
	 * 
	 * <b>Note:</b> after serializing the <b>close()</b> method needs to be
	 * called!
	 * 
	 * @param outStream
	 *            output stream to serialize the traces to
	 */
	void prepare(OutputStream outStream);

	/**
	 * Serializes a single trace.
	 * 
	 * @param trace
	 *            {@link Trace} instance to serialize.
	 */
	void writeTrace(Trace trace);

	/**
	 * Cleans up serializer.
	 */
	void close();
}
