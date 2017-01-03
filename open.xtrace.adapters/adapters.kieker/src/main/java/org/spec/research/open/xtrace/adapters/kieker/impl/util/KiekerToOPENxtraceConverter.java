package org.spec.research.open.xtrace.adapters.kieker.impl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;

import org.spec.research.open.xtrace.adapters.kieker.impl.LocationImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.SubTraceImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.TraceImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.additionalInformation.CallableExecutionInformation;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.AbstractCallableImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.DatabaseInvocationImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.HttpRequestProcessingImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.MethodInvocationImpl;
import org.spec.research.open.xtrace.adapters.kieker.impl.callables.RemoteInvocationImpl;
import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;

/**
 * An instance of this class converts a @MessageTrace of kieker into a @TraceImpl
 * of 'OPEN.xtrace'. For this, it needs an iterator of the message trace. This
 * iterator should return messages in their execution order.
 * 
 * This class is used like an iterator. Calling hasNextCallable() returns
 * whether a new @Callable can be produced. Calling produceNextCallable()
 * creates the next callable, but does not return it. If hasNextCallable()
 * returns false, you can call getFinishedRootSubTrace() to get the finished @SubTraceImpl
 * containing all created callables in a tree order.
 * 
 * @author Dušan Okanović, Dominic Parga Cacheiro
 * 
 */
public class KiekerToOPENxtraceConverter {
	// |============|
	// | attributes |
	// |============|
	private TraceImpl senderTrace;
	private Iterator<AbstractMessage> messages;
	private SubTraceImpl rootSubTrace;
	private Location lastLocation;
	private SubTraceImpl lastSubTrace;
	private Callable lastCallable;

	// |================|
	// | initialization |
	// |================|
	/**
	 * This constructor prepares the iteration over all messages of a message
	 * trace, e.g. creating an empty root subtrace. If the message iterator is
	 * empty, nothing will be initialized.
	 * 
	 * @param sender
	 *            This is the instance of @TraceImpl that uses this converter to
	 *            create it's @SubTraceImpl.
	 * @param messages
	 *            An iterator over all messages ordered by their execution
	 *            order.
	 */
	public KiekerToOPENxtraceConverter(TraceImpl sender, Iterator<AbstractMessage> messages) {
		super();
		this.messages = messages;
		this.senderTrace = sender;

		// create rootSubTrace
		if (messages.hasNext()) {
			AbstractMessage entryMessage = messages.next();

			AbstractCallableImpl newCallable = generateEntryCallable(entryMessage.getSendingExecution(), entryMessage.getReceivingExecution(), null, null);

			rootSubTrace = new SubTraceImpl(sender, newCallable, entryMessage);
			newCallable.setContainingSubTrace(rootSubTrace);

			lastSubTrace = rootSubTrace;
			lastCallable = lastSubTrace.getRoot();
			lastLocation = rootSubTrace.getLocation();
		}
	}

	// |==================|
	// | create callables |
	// |==================|
	/**
	 * @return true if there's any message that has to be processed; if false:
	 *         you can call getFinishedRootSubTrace()
	 */
	public boolean hasNotFinished() {
		return messages.hasNext();
	}

	/**
	 * This method processes the next message and check its type. If it is an
	 * instance of @SynchronousCallMessage, a callable will be created.
	 * 
	 * @return true, if a callable has been created; false otherwise
	 */
	public boolean processNextMessage() {
		AbstractMessage abstractMessage = messages.next();

		if (abstractMessage instanceof SynchronousCallMessage) {
			useSynchronousCallMessage((SynchronousCallMessage) abstractMessage);
			return true;
		} else if (abstractMessage instanceof SynchronousReplyMessage) {
			if (lastCallable.getParent() != null)
				lastCallable = lastCallable.getParent();
			else if (lastSubTrace.getParent() != null) {
				lastSubTrace = (SubTraceImpl) lastSubTrace.getParent();
				lastCallable = lastSubTrace.getLastCallable();
			}
		}
		return false;
	}

	/**
	 * This method should be called if hasNotFinished() returns false.
	 * Otherwise, a @Callable could be missing.
	 * 
	 * @return finished root subtrace; null, if hasNotFinished() returns true
	 */
	public SubTraceImpl getFinishedRootSubTrace() {
		if (messages.hasNext()) {
			return null;
		}
		return rootSubTrace;
	}

	// |========================|
	// | use call/reply message |
	// |========================|
	private void useSynchronousCallMessage(SynchronousCallMessage message) {
		AbstractNestingCallableImpl nestingLastCallable = (AbstractNestingCallableImpl) lastCallable;
		Callable newCallable;

		// if the location is the same as previous, add callable
		Location newLocation = new LocationImpl(message.getReceivingExecution());
		if (lastLocation.equals(newLocation)) {
			newCallable = generateCallable(message.getReceivingExecution(), nestingLastCallable, lastSubTrace);
			nestingLastCallable.addCallee(newCallable);
		} else { // else add subtrace
			// first create remote invocation and add it to previous
			// subtrace
			lastSubTrace.setLastCallable(lastCallable);

			SubTraceImpl newSubTrace = new SubTraceImpl(senderTrace, null, lastSubTrace, message);

			RemoteInvocation ri = new RemoteInvocationImpl(nestingLastCallable, message.getTimestamp(), null, null, lastSubTrace, newSubTrace);
			nestingLastCallable.addCallee(ri);

			lastSubTrace.getSubTraces().add(newSubTrace);

			lastLocation = newLocation;
			lastSubTrace = newSubTrace;
			newCallable = generateCallable(message.getReceivingExecution(), null, lastSubTrace); // calls
																									// setContainingSubTrace(lastSubTrace)
																									// in
																									// constructor
																									// of
																									// @MethodInvocationImpl
			lastSubTrace.setRoot(newCallable);
		}

		lastCallable = newCallable;
	}

	// |======================|
	// | callable interaction |
	// |======================|
	private AbstractCallableImpl generateCallable(Execution receivingExecution, AbstractNestingCallableImpl parent, SubTrace containingSubTrace) {
		return generateEntryCallable(null, receivingExecution, parent, containingSubTrace);
	}

	private AbstractCallableImpl generateEntryCallable(Execution sendingEntryExecution, Execution receivingExecution, AbstractNestingCallableImpl parent,
			SubTrace containingSubTrace) {
		AbstractCallableImpl lastCallable = null;
		if (CallableResolver.isHttpCall(receivingExecution)) {
			lastCallable = new HttpRequestProcessingImpl();
		} else if (CallableResolver.isDbCall(receivingExecution)) {
			lastCallable = new DatabaseInvocationImpl();
		} else {
			CallableExecutionInformation info = new CallableExecutionInformation(sendingEntryExecution, receivingExecution);
			List<AdditionalInformation> infoList = new ArrayList<AdditionalInformation>(1);
			infoList.add(info);
			lastCallable = new MethodInvocationImpl(receivingExecution, parent, null, infoList, containingSubTrace);
		}

		return lastCallable;
	}
}