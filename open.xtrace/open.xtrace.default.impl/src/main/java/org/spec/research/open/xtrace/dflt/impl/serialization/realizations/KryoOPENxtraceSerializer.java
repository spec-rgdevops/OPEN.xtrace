package org.spec.research.open.xtrace.dflt.impl.serialization.realizations;

import java.io.OutputStream;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.serialization.OPENxtraceSerializer;

import com.esotericsoftware.kryo.io.Output;

/**
 * Serializer to binary format using Kryo.
 * 
 * @author Alexander Wert
 *
 */
public class KryoOPENxtraceSerializer extends KryoOPENxtraceSerializationBase implements OPENxtraceSerializer {

	/**
	 * Sink of serialization.
	 */
	private Output output;

	/**
	 * Constructor.
	 */
	public KryoOPENxtraceSerializer() {
		super();
	}

	@Override
	public void prepare(OutputStream outStream) {
		output = new Output(outStream);

	}

	@Override
	public void writeTrace(Trace trace) {
		if (!(trace instanceof TraceImpl)) {
			throw new IllegalArgumentException("THis serializer can only serialize instances of " + TraceImpl.class.getName());
		}
		getKryoInstance().writeObject(output, trace);
	}

	@Override
	public void close() {
		output.close();

	}

}
