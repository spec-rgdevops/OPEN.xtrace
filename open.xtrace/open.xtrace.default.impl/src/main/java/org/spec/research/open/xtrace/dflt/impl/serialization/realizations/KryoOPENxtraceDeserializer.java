package org.spec.research.open.xtrace.dflt.impl.serialization.realizations;

import java.io.InputStream;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.serialization.OPENxtraceDeserializer;

import com.esotericsoftware.kryo.io.Input;

/**
 * Serializer from binary format using Kryo.
 * 
 * @author Alexander Wert
 *
 */
public class KryoOPENxtraceDeserializer extends KryoOPENxtraceSerializationBase implements OPENxtraceDeserializer {

	/**
	 * Source for deserialization.
	 */
	private Input input;

	/**
	 * Constructor.
	 */
	public KryoOPENxtraceDeserializer() {
		super();
	}

	@Override
	public void setSource(InputStream inStream) {
		input = new Input(inStream);

	}

	@Override
	public Trace readNext() {
		if (input.eof()) {
			return null;
		}

		return getKryoInstance().readObject(input, TraceImpl.class);
	}

	@Override
	public void close() {
		input.close();

	}

}
