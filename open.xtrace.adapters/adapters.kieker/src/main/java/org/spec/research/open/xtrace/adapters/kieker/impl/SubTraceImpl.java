package org.spec.research.open.xtrace.adapters.kieker.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.api.utils.StringUtils;

/**
 * @author Okanovic
 *
 */
public class SubTraceImpl extends AbstractIdentifiableImpl implements SubTrace, Serializable {
	private static final long serialVersionUID = 8520603674813053640L;

	// TODO check
	private Callable root;
	private Location location;
	private Trace containingTrace;
	private SubTrace parent;
	private List<SubTrace> subTraces = new ArrayList<SubTrace>();
	private long subTraceId;
	private transient int maxDepth = -1;
	private transient int size = -1;

	public SubTraceImpl(Callable root, Location location, Trace containingTrace, SubTrace parent, List<SubTrace> subTraces, long subTraceId) {
		super();
		this.root = root;
		this.location = location;
		this.containingTrace = containingTrace;
		this.parent = parent;
		if (subTraces != null)
			this.subTraces.addAll(subTraces);
		this.subTraceId = subTraceId;
	}

	public SubTraceImpl(TraceImpl traceImpl, Callable root, AbstractMessage abstractMessage) {
		this(root, new LocationImpl(abstractMessage.getReceivingExecution()), traceImpl, null, null, traceImpl.getTraceId());
	}

	public SubTraceImpl(TraceImpl traceImpl, Callable root, SubTrace parent, AbstractMessage abstractMessage) {
		this(root, new LocationImpl(abstractMessage.getReceivingExecution()), traceImpl, parent, null, traceImpl.getTraceId());
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIterator(getRoot());
	}

	@Override
	public Trace getContainingTrace() {
		return containingTrace;
	}

	@Override
	public long getSubTraceId() {
		return subTraceId;
	}

	@Override
	public SubTrace getParent() {
		return parent;
	}

	public Callable getRoot() {
		return root;
	}

	@Override
	public List<SubTrace> getSubTraces() {
		return subTraces;
	}

	@Override
	public long getExclusiveTime() {
		long exclTime = getResponseTime();
		for (SubTrace child : getSubTraces()) {
			exclTime -= child.getResponseTime();
		}
		return exclTime;
	}

	@Override
	public long getResponseTime() {
		if (root instanceof NestingCallable) {
			return ((NestingCallable) root).getResponseTime();
		} else {
			return 0;
		}
	}

	@Override
	public int size() {
		int count = 0;
		for (@SuppressWarnings("unused")
		Callable cbl : this) {
			count++;
		}
		size = count;

		return size;
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (subTraceId ^ (subTraceId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SubTraceImpl other = (SubTraceImpl) obj;
		if (subTraceId != other.subTraceId) {
			return false;
		}
		return true;
	}

	// for coming back from subtrace
	transient Callable lastCallable;

	public Callable getLastCallable() {
		return lastCallable;
	}

	public void setLastCallable(Callable lastCallable) {
		this.lastCallable = lastCallable;
	}

	public void setRoot(Callable root) {
		this.root = root;
	}
}