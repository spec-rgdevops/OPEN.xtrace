package org.spec.research.open.xtrace.adapters.kieker.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;

import org.spec.research.open.xtrace.adapters.kieker.impl.util.KiekerToOPENxtraceConverter;
import org.spec.research.open.xtrace.adapters.kieker.impl.util.OPENxtraceToKiekerTraceConverter;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.utils.CallableIteratorOnTrace;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.api.utils.SubTraceIterator;

/**
 * @author Okanovic
 *
 */
public class TraceImpl extends AbstractIdentifiableImpl implements Trace, Serializable {

	private static final long serialVersionUID = 81641878420161262L;

	private SubTraceImpl rootSubTrace;
	private long traceId;
	private transient int size = -1;

	private TraceImpl() {
		super();
	}

	// commented
	private void dusanTraceImpl() {
		// // Dusan
		// this.traceId = messageTrace.getTraceId();
		// List<AbstractMessage> messages = new ArrayList<AbstractMessage>();
		//
		// messages.addAll(messageTrace.getSequenceAsVector());
		//
		// System.out.println("TEST TEST 1 2 3 - start");
		// for (AbstractMessage message : messages) {
		// System.out.println(message);
		// }
		//
		// Location lastLocation = null;
		// SubTrace lastSubTrace = null;
		// Callable lastCallable = null;
		// AbstractCallableImpl newCallable = null;
		// // create first callable which is either method invocation or HTTP
		// // request
		// Execution firstReceivingExecution = messages.get(0)
		// .getReceivingExecution();
		// newCallable = generateCallable(firstReceivingExecution, null, null);
		// SubTraceImpl rootSubTrace = new SubTraceImpl(this, newCallable,
		// messages.get(0));
		// this.rootSubTrace = rootSubTrace;
		// newCallable.setContainingSubTrace(this.rootSubTrace);
		// lastSubTrace = rootSubTrace;
		// lastCallable = lastSubTrace.getRoot();
		// ((AbstractNestingCallableImpl) lastCallable)
		// .setContainingSubTrace((SubTraceImpl) lastSubTrace);
		// lastLocation = rootSubTrace.getLocation();
		//
		// messages.remove(0);
		// for (AbstractMessage abstractMessage : messages) {
		// // call message
		// if (abstractMessage instanceof SynchronousCallMessage) {
		// // if the location is the same as previous, add callable
		// Location newLocation = new LocationImpl(
		// abstractMessage.getReceivingExecution());
		// if (lastLocation.equals(newLocation)) {
		// newCallable = generateCallable(
		// abstractMessage.getReceivingExecution(),
		// (AbstractNestingCallableImpl) lastCallable,
		// lastSubTrace);
		// ((AbstractNestingCallableImpl) lastCallable)
		// .addCallee(newCallable);
		// } else { // else add subtrace
		// // first create remote invocation and add it to previous
		// // subtrace
		// ((SubTraceImpl) lastSubTrace).setLastCallable(lastCallable);
		// SubTraceImpl newSubTrace = new SubTraceImpl(this, null,
		// lastSubTrace, abstractMessage);
		// RemoteInvocation ri = new RemoteInvocationImpl(
		// (AbstractNestingCallableImpl) lastCallable,
		// abstractMessage.getTimestamp(), null, null,
		// lastSubTrace, newSubTrace);
		// ((AbstractNestingCallableImpl) lastCallable).addCallee(ri);
		// lastSubTrace.getSubTraces().add(newSubTrace);
		// lastLocation = newLocation;
		// lastSubTrace = newSubTrace;
		// newCallable = generateCallable(
		// abstractMessage.getReceivingExecution(), null,
		// lastSubTrace);
		// ((SubTraceImpl) lastSubTrace).setRoot(newCallable);
		// }
		// lastCallable = newCallable;
		// ((AbstractNestingCallableImpl) lastCallable)
		// .setContainingSubTrace((SubTraceImpl) lastSubTrace);
		// } else {
		// // reply message
		// if (lastCallable.getParent() != null)
		// lastCallable = lastCallable.getParent();
		// else if (lastSubTrace.getParent() != null) {
		// lastSubTrace = lastSubTrace.getParent();
		// lastCallable = ((SubTraceImpl) lastSubTrace)
		// .getLastCallable();
		// }
		// }
		// }
		//
		// System.out.println(iterator().hasNext());
		// System.out.println("TEST TEST 1 2 3 - end");
	}

	// |===============|
	// | kieker to CTA |
	// |===============|
	/**
	 * This method creates a @TraceImpl out of the given @MessageTrace. For
	 * this, it converts all @AbstractMessage#s into @Callable#s. The messages
	 * of the given @MessageTrace has to be ordered in their execution order.
	 * 
	 * @param messageTrace
	 *            This trace contains all @AbstractMessage#s.
	 * @return an instance of @TraceImpl containing @Callable#s out of the given
	 *         trace's @AbstractMessage#s
	 * 
	 * @author Dominic Parga Cacheiro
	 */
	public static TraceImpl createCallableTrace(MessageTrace messageTrace) {
		TraceImpl trace = new TraceImpl();
		trace.traceId = messageTrace.getTraceId();

		List<AbstractMessage> messages = new ArrayList<AbstractMessage>();
		messages.addAll(messageTrace.getSequenceAsVector());

		KiekerToOPENxtraceConverter traceConverter = new KiekerToOPENxtraceConverter(trace, messages.iterator());

		while (traceConverter.hasNotFinished()) {
			traceConverter.processNextMessage();
		}

		trace.rootSubTrace = traceConverter.getFinishedRootSubTrace();

		return trace;
	}

	// |===============|
	// | CTA to kieker |
	// |===============|
	/**
	 * This method creates a @MessageTrace out of the given @TraceImpl. For
	 * this, it converts all @Callable#s into @AbstractMessage#s by calling an
	 * iterator of the given trace. The iterator has to iterate over the
	 * callables in their execution order.
	 * 
	 * @param trace
	 *            This trace contains all @Callable#s.
	 * @return an instance of @MessageTrace containing @AbstractMessage#s out of
	 *         the given trace's @Callable#s
	 * 
	 * @author Dominic Parga Cacheiro
	 */
	public static MessageTrace createMessageTrace(TraceImpl trace) {
		OPENxtraceToKiekerTraceConverter traceConverter = new OPENxtraceToKiekerTraceConverter(trace.iterator());

		List<AbstractMessage> messages = new ArrayList<AbstractMessage>();
		while (traceConverter.hasNextMessage()) {
			messages.add(traceConverter.produceNextMessage());
		}

		// return
		MessageTrace createdMessageTrace = new MessageTrace(trace.getTraceId(), messages);
		return createdMessageTrace;
	}

	// |======================|
	// | callable interaction |
	// |======================|
	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIteratorOnTrace(getRoot());
	}

	// |=========================|
	// | (sub-)trace interaction |
	// |=========================|
	@Override
	public SubTrace getRoot() {
		return rootSubTrace;
	}

	public void setRoot(SubTraceImpl root) {
		this.rootSubTrace = root;
	}

	@Override
	public TreeIterator<SubTrace> subTraceIterator() {
		return new SubTraceIterator(getRoot());
	}

	@Override
	public long getTraceId() {
		return traceId;
	}

	// |===============|
	// | getter/setter |
	// |===============|
	@Override
	public int size() {
		if (size < 0) {
			int count = 0;
			Iterator<SubTrace> iterator = this.subTraceIterator();
			while (iterator.hasNext()) {
				SubTrace sTrace = iterator.next();
				count += sTrace.size();
			}
			size = count;
		}

		return size;
	}

	@Override
	public long getExclusiveTime() {
		return getResponseTime();
	}

	@Override
	public long getResponseTime() {
		if (rootSubTrace == null) {
			return 0;
		} else {
			return rootSubTrace.getResponseTime();
		}
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}