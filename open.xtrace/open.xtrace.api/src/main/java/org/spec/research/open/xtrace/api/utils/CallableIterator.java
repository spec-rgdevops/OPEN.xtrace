package org.spec.research.open.xtrace.api.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;

/**
 * Iterator over all {@link Callable}s on a SubTrace.
 * 
 * @author Alexander Wert
 *
 */
public class CallableIterator implements TreeIterator<Callable> {

	/**
	 * Stack of iterators to traverse the SubTrace structure.
	 */
	private Stack<Iterator<Callable>> iteratorStack = new Stack<Iterator<Callable>>();

	/**
	 * Current iterator on {@link Callable}s.
	 */
	private Iterator<Callable> currentIterator;

	/**
	 * Current depth of the iterator position.
	 */
	private int currentDepth = -1;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root node in the SubTrace tree
	 */
	public CallableIterator(Callable root) {
		List<Callable> rootList = new ArrayList<Callable>(1);
		if (root != null) {
			rootList.add(root);
		}
		currentIterator = rootList.iterator();
	}

	@Override
	public boolean hasNext() {
		while (!currentIterator.hasNext() && !iteratorStack.isEmpty()) {
			currentIterator = iteratorStack.pop();
		}
		return currentIterator.hasNext();
	}

	@Override
	public Callable next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		}

		Callable tmpCallable = currentIterator.next();
		currentDepth = iteratorStack.size();

		if (tmpCallable instanceof NestingCallable) {
			List<Callable> callees = ((NestingCallable) tmpCallable).getCallees();
			if (!callees.isEmpty()) {
				iteratorStack.push(currentIterator);
				currentIterator = callees.iterator();
			}
		}

		return tmpCallable;

	}

	/**
	 * 
	 * @return the current depth of the iterator position.
	 */
	@Override
	public int currentDepth() {
		return currentDepth;
	}
}
