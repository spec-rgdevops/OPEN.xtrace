package org.spec.research.open.xtrace.dflt.impl.serialization;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.dflt.impl.serialization.realizations.KryoOPENxtraceDeserializer;
import org.spec.research.open.xtrace.dflt.impl.serialization.realizations.KryoOPENxtraceSerializer;

/**
 * Factory to create serializers and deserializers for 'OPEN.xtrace'
 * {@link Trace} instances.
 * 
 * @author Alexander Wert
 *
 */
public final class OPENxtraceSerializationFactory {

	/**
	 * Singleton instance.
	 */
	private static OPENxtraceSerializationFactory instance;

	/**
	 * 
	 * @return the singleton instance
	 */
	public static OPENxtraceSerializationFactory getInstance() {
		if (instance == null) {
			instance = new OPENxtraceSerializationFactory();
		}
		return instance;
	}

	/**
	 * Private constructor for singleton.
	 */
	private OPENxtraceSerializationFactory() {
	}

	/**
	 * Returns a serializer instance for the given serialization format.
	 * 
	 * @param format
	 *            target format
	 * @return serializer instance
	 */
	public OPENxtraceSerializer getSerializer(OPENxtraceSerializationFormat format) {
		switch (format) {
		case BINARY:
			return new KryoOPENxtraceSerializer();
		case JSON:
		default:
			throw new IllegalArgumentException("Unsupported format: " + format);
		}
	}

	/**
	 * Returns a deserializer instance for the given serialization format.
	 * 
	 * @param format
	 *            target format
	 * @return deserializer instance
	 */
	public OPENxtraceDeserializer getDeserializer(OPENxtraceSerializationFormat format) {
		switch (format) {
		case BINARY:
			return new KryoOPENxtraceDeserializer();
		case JSON:
		default:
			throw new IllegalArgumentException("Unsupported format: " + format);
		}
	}

}
