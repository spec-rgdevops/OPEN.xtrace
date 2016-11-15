package org.spec.research.open.xtrace.api.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;

/**
 * Iterator over Callables on a Trace.
 * 
 * @author Alexander Wert
 *
 */
public class CallableIteratorOnTrace implements TreeIterator<Callable> {

	/**
	 * Stack of iterators to traverse the SubTrace structure.
	 */
	private Stack<Iterator<Callable>> iteratorStack = new Stack<Iterator<Callable>>();

	/**
	 * Current iterator on Callables. This changes when a new SubTrace is entered during traversing.
	 */
	private Iterator<Callable> currentIterator;

	/**
	 * depth of all the iterators on stack.
	 */
	private int stackedDepth = 0;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root node in the SubTrace tree
	 */
	public CallableIteratorOnTrace(SubTrace root) {
		if (root == null) {
			currentIterator = Collections.<Callable>emptyList().iterator();
		} else {
			currentIterator = new CallableIterator(root.getRoot());
			iteratorStack.push(currentIterator);
		}
	}

	@Override
	public boolean hasNext() {
		while (!currentIterator.hasNext() && !iteratorStack.isEmpty()) {
			currentIterator = iteratorStack.pop();
			stackedDepth -= (((CallableIterator) currentIterator).currentDepth() + 1);
		}

		if (iteratorStack.isEmpty()) {
			return false;
		}

		return currentIterator.hasNext();
	}

	@Override
	public Callable next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		}

		Callable tmpCallable = currentIterator.next();

		if (tmpCallable instanceof RemoteInvocation && ((RemoteInvocation) tmpCallable).getTargetSubTrace().isPresent()) {
			stackedDepth += ((CallableIterator) currentIterator).currentDepth() + 1;
			iteratorStack.push(currentIterator);

			currentIterator = new CallableIterator(((RemoteInvocation) tmpCallable).getTargetSubTrace().get().getRoot());
			tmpCallable = currentIterator.next();
		}

		return tmpCallable;

	}

	/**
	 * 
	 * @return the current depth of the iterator position.
	 */
	@Override
	public int currentDepth() {
		return stackedDepth + ((CallableIterator) currentIterator).currentDepth();
	}

}