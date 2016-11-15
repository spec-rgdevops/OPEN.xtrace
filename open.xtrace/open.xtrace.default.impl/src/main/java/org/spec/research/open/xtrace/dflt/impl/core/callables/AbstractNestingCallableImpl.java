package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

/**
 * Default implementation of the {@link NestingCallable} API element.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public abstract class AbstractNestingCallableImpl extends AbstractTimedCallableImpl
		implements NestingCallable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3329525574835616508L;

	/**
	 * {@link Callable} instances called by this {@link NestingCallable}
	 * instance.
	 */
	protected List<Callable> children;

	/**
	 * Number of elements below this {@link NestingCallable} instance.
	 */
	protected int childCount = 0;

	/**
	 * Default constructor for serialization. This constructor should not be
	 * used except for deserialization.
	 */
	public AbstractNestingCallableImpl() {
	}

	/**
	 * Constructor. Adds the newly created {@link Callable} instance to the
	 * passed parent if the parent is not null!
	 * 
	 * @param parent
	 *            {@link AbstractNestingCallableImpl} that called this Callable
	 * @param containingSubTrace
	 *            the SubTrace containing this Callable
	 */
	public AbstractNestingCallableImpl(AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {
		super(parent, containingSubTrace);
	}

	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIterator(this);
	}

	@Override
	public List<Callable> getCallees() {
		if (children == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(children);
		}
	}

	/**
	 * Adds a new child Callable.
	 * 
	 * @param callee
	 *            a Callables called by this Callable
	 */
	public void addCallee(Callable callee) {
		if (children == null) {
			children = new ArrayList<Callable>();
		}
		children.add(callee);
		int additionalNumChildren = 1;
		if (callee instanceof NestingCallable) {
			additionalNumChildren += ((NestingCallable) callee).getChildCount();
		}
		updateChildCount(additionalNumChildren);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Callable> List<T> getCallees(Class<T> type) {
		if (children == null) {
			return Collections.emptyList();
		} else {
			List<T> result = null;
			for (Callable callable : children) {
				if (type.isAssignableFrom(callable.getClass())) {
					if (result == null) {
						result = new ArrayList<T>();
					}
					result.add((T) callable);
				}
			}

			return result == null ? Collections.emptyList() : Collections.unmodifiableList(result);
		}

	}

	@Override
	public int getChildCount() {
		return childCount;
	}

	/**
	 * Updates the child count of this node by increasing the current child
	 * count by the passed childCountIncrease.
	 * 
	 * @param childCountIncrease
	 *            the childCount to increment the current child count by
	 */
	private void updateChildCount(int childCountIncrease) {
		this.childCount += childCountIncrease;
		if (parent != null) {
			parent.updateChildCount(childCountIncrease);
		}
	}

}
