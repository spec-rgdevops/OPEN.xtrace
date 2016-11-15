package org.spec.research.open.xtrace.dflt.impl.serialization.realizations;

import org.spec.research.open.xtrace.dflt.impl.core.LocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.Signature;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractTimedCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.DatabaseInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.ExceptionThrowImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.HTTPRequestProcessingImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.LoggingInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.RemoteInvocationImpl;

import com.esotericsoftware.kryo.Kryo;

/**
 * The {@link KryoOPENxtraceSerializationBase} class is responsible for serializing and deserializing
 * {@link TraceImpl} instances.
 * 
 * @author Alexander Wert
 *
 */
public class KryoOPENxtraceSerializationBase {

	/**
	 * kryo instance.
	 */
	private Kryo kryo;

	/**
	 * Constructor. Initializes kryo.
	 */
	public KryoOPENxtraceSerializationBase() {
		kryo = new Kryo();

		kryo.register(TraceImpl.class);
		kryo.register(SubTraceImpl.class);
		kryo.register(LocationImpl.class);
		kryo.register(AbstractCallableImpl.class);
		kryo.register(AbstractTimedCallableImpl.class);
		kryo.register(AbstractNestingCallableImpl.class);
		kryo.register(RemoteInvocationImpl.class);
		kryo.register(DatabaseInvocationImpl.class);
		kryo.register(MethodInvocationImpl.class);
		kryo.register(Signature.class);
		kryo.register(HTTPRequestProcessingImpl.class);
		kryo.register(ExceptionThrowImpl.class);
		kryo.register(LoggingInvocationImpl.class);
	}

	/**
	 * 
	 * @return kryo instance
	 */
	protected Kryo getKryoInstance() {
		return kryo;
	}
}
