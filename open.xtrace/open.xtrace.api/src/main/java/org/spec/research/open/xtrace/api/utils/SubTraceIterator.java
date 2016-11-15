package org.spec.research.open.xtrace.api.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;

/**
 * Iterator over {@link SubTrace} of a {@link Trace}.
 * 
 * @author Alexander Wert
 *
 */
public class SubTraceIterator implements TreeIterator<SubTrace> {

	/**
	 * Stack of iterators to traverse the Trace structure.
	 */
	private Stack<Iterator<SubTrace>> iteratorStack = new Stack<Iterator<SubTrace>>();

	/**
	 * Current iterator on SubTraces.
	 */
	private Iterator<SubTrace> currentIterator;

	/**
	 * current depth of the iterator position.
	 */
	private int currentDepth = -1;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            root node in the SubTrace tree
	 */
	public SubTraceIterator(SubTrace root) {
		List<SubTrace> rootList = new ArrayList<SubTrace>(1);
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
	public SubTrace next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Iterator reached the end!");
		}

		SubTrace tmpSubTrace = currentIterator.next();
		currentDepth = iteratorStack.size();

		List<SubTrace> subTraces = tmpSubTrace.getSubTraces();
		if (!subTraces.isEmpty()) {
			iteratorStack.push(currentIterator);
			currentIterator = subTraces.iterator();
		}
		
		return tmpSubTrace;

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
