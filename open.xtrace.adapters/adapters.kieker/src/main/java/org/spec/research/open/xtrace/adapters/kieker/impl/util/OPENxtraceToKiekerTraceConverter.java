package org.spec.research.open.xtrace.adapters.kieker.impl.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;

import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;

/**
 * This class helps with creating a kieker trace out of a 'OPEN.xtrace' trace.
 * 'OPEN.xtraces' are built up by a tree of @SubTrace#s containing trees of
 * 
 * @Callable#s, whereas kieker traces focuses on messages between callables.
 *              Because one callable can call another callable by an message,
 *              you have to save callables in a stack to access them after
 *              getting reply messages of child callables. This task is
 *              implemented in this class.
 * 
 * @author Dominic Parga Cacheiro
 *
 */
public class OPENxtraceToKiekerTraceConverter {
	// |============|
	// | attributes |
	// |============|
	private Queue<Callable> callables;
	private Stack<CallableStackElement> stack;

	// |================|
	// | initialization |
	// |================|
	/**
	 * This constructor takes the @Callable#s and pushes all of them into a
	 * queue for processing them later.
	 * 
	 * @param iterator
	 *            all callables of the trace sorted by their execution order
	 */
	public OPENxtraceToKiekerTraceConverter(TreeIterator<Callable> iterator) {
		super();
		callables = new LinkedList<Callable>();
		while (iterator.hasNext()) {
			callables.add(iterator.next());
		}
		stack = new Stack<CallableStackElement>();
	}

	// |===================|
	// | message production|
	// |===================|
	/**
	 * This method should return whether still any message can be produced.
	 * 
	 * @return true, if any message can be produced; false otherwise
	 */
	public boolean hasNextMessage() {
		// truth table:
		// |---------|---------------------|
		// | 1 2 3 4 | column -------------|
		// |---------|---------------------|
		// | 0 0 1 1 | stack.isEmpty() ----|
		// | 0 1 0 1 | callables.isEmpty() |
		// | 1 1 1 0 | return value -------|
		// |---------|---------------------|
		// PS: 3rd column shows initial state
		return !(stack.isEmpty() && callables.isEmpty());
	}

	/**
	 * For each call of this method, one message is produced and returned. This
	 * method is used like "next()" of an iterator. Therefore you have to check
	 * in a loop if this stack is empty before you call this method.
	 * 
	 * @return next message
	 */
	public AbstractMessage produceNextMessage() {
		// if there is any message missing => produce
		while (hasNextMessage()) {
			// if there is any callable => call messages
			if (!callables.isEmpty()) {
				if (!(callables.peek() instanceof RemoteInvocation)) {
					CallableStackElement nextElement = new CallableStackElement(callables.peek());

					// stack is empty inside while-loop <=> init state
					// (more details about the states => hasNextMessage())
					if (stack.isEmpty()) {
						stack.push(nextElement);
						callables.poll();
						return new SynchronousCallMessage(nextElement.getTimestamp(), nextElement.getEntryExecution(), nextElement.getExecution());
					} else { // not init state
						// now:
						// 1) nextElement.ess > stack.peek().ess
						// => return call message
						// 2) nextElement.ess <= stack.peek().ess
						// => return reply message
						if (nextElement.getEss() > stack.peek().getEss()) {
							return produceCallMessage(nextElement);
						} else {
							return produceReplyMessage();
						}
					}
				}
			} else {
				// stack is full but all callables are removed from iterator
				// => just return reply messages
				if (stack.size() > 1) {
					// reply messages until stack only contains the entry
					// callable
					return produceReplyMessage();
				} else {
					// => return last message
					CallableStackElement nextElement = stack.pop();
					return new SynchronousReplyMessage(nextElement.getExecution().getTout(), nextElement.getExecution(), nextElement.getEntryExecution());
				}
			}
		}

		throw new RuntimeException("OPENxtraceToKiekerTraceConverter.hasNextMessage() == false but .produceNextMessage() is called!");
	}

	private SynchronousCallMessage produceCallMessage(CallableStackElement nextElement) {
		// sending by last callable (current top of stack)
		Execution sendingExecution = stack.peek().getExecution();

		// add new callable on stack
		stack.push(nextElement);
		callables.poll();

		// receiving by new callable (current top of stack)
		Execution receivingExecution = nextElement.getExecution();

		return new SynchronousCallMessage(receivingExecution.getTin(), sendingExecution, receivingExecution);
	}

	private SynchronousReplyMessage produceReplyMessage() {
		// sending by old callable
		Execution sendingExecution = stack.pop().getExecution();

		// receiving by parent callable (current top of stack)
		Execution receivingExecution = stack.peek().getExecution();

		return new SynchronousReplyMessage(sendingExecution.getTout(), sendingExecution, receivingExecution);
	}
}