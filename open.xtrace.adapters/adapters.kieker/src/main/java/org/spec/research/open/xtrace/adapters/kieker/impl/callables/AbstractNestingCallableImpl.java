package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;

/**
 * @author Okanovic
 *
 */
public abstract class AbstractNestingCallableImpl extends AbstractTimedCallableImpl implements NestingCallable, Serializable {

	private static final long serialVersionUID = -5263454158084783592L;
	private int childCount = 0;
	private List<Callable> callees = new ArrayList<Callable>();

	public AbstractNestingCallableImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, long responseTime, long exclusiveTime) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace, responseTime, exclusiveTime);
		this.callees.addAll(callees);
		this.childCount = callees.size();
	}

	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIterator(this);
	}

	@Override
	public List<Callable> getCallees() {
		if (callees == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(callees);
		}
	}

	public void addCallee(Callable callee) {
		if (callees == null) {
			callees = new ArrayList<Callable>();
		}
		callees.add(callee);
		int additionalNumChildren = 1;
		if (callee instanceof NestingCallable) {
			additionalNumChildren += ((NestingCallable) callee).getChildCount();
		}
		updateChildCount(additionalNumChildren);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Callable> List<T> getCallees(Class<T> type) {
		if (callees == null) {
			return Collections.emptyList();
		} else {
			List<T> result = null;
			for (Callable callable : callees) {
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

	protected void updateChildCount(int childCountIncrease) {
		this.childCount += childCountIncrease;
		if (parent != null) {
			parent.updateChildCount(childCountIncrease);
		}
	}
}