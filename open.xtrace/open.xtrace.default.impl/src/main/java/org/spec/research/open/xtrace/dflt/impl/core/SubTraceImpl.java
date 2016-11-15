package org.spec.research.open.xtrace.dflt.impl.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractCallableImpl;

/**
 * Default implementation of the {@link SubTrace} interface of the
 * 'OPEN.xtrace'.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public class SubTraceImpl extends AbstractIdentifiableImpl implements SubTrace, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5403027032809860948L;

	/**
	 * Root Callable.
	 */
	private AbstractCallableImpl root;

	/**
	 * SubTrace that invoked this SubTrace.
	 */
	private SubTrace parentSubTrace;

	/**
	 * SubTraces invoked by this SubTrace.
	 */
	private List<SubTrace> childSubTraces;

	/**
	 * Location characterizing this SubTrace.
	 */
	private Location location;

	/**
	 * Trace instance containing this SubTrace.
	 */
	private TraceImpl containingTrace;

	/**
	 * Identified of this SubTRace.
	 */
	private long subTraceId;

	/**
	 * size of this SubTrace.
	 */
	private transient int size = -1;

	/**
	 * Default constructor.
	 */
	public SubTraceImpl() {
	}

	/**
	 * Constructor. Adds the newly created {@link SubTrace} instance to the
	 * passed parent if the parent is not null!
	 * 
	 * @param subTraceId
	 *            the identifier of this SubTrace
	 * @param parentSubTrace
	 *            SubTrace that invoked this SubTrace.
	 * @param containingTrace
	 *            trace containing this SubTrace
	 */
	public SubTraceImpl(long subTraceId, SubTraceImpl parentSubTrace, TraceImpl containingTrace) {
		this.subTraceId = subTraceId;
		this.parentSubTrace = parentSubTrace;
		if (parentSubTrace != null) {
			parentSubTrace.addSubTrace(this);
		}
		this.containingTrace = containingTrace;
	}

	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIterator(getRoot());
	}

	@Override
	public Callable getRoot() {
		return root;
	}

	/**
	 * Setter for the root.
	 * 
	 * @param root
	 *            root of this SUbTrace
	 */
	public void setRoot(AbstractCallableImpl root) {
		this.root = root;
	}

	@Override
	public SubTrace getParent() {
		return parentSubTrace;
	}

	@Override
	public List<SubTrace> getSubTraces() {
		if (childSubTraces == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(childSubTraces);
	}

	/**
	 * Adds a new child SubTrace.
	 * 
	 * @param subTrace
	 *            a SubTrace invoked by this SubTrace
	 */
	public void addSubTrace(SubTrace subTrace) {
		if (childSubTraces == null) {
			childSubTraces = new ArrayList<SubTrace>();
		}
		childSubTraces.add(subTrace);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	/**
	 * Setter for the location.
	 * 
	 * @param location
	 *            location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
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
	public int size() {
		if (size < 0) {
			int count = 0;
			for (@SuppressWarnings("unused")
			Callable cbl : this) {
				count++;
			}
			size = count;
		}

		return size;
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (subTraceId ^ (subTraceId >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	@Override
	public long getExclusiveTime() {
		long exclTime = getResponseTime();
		// for (SubTrace child : getSubTraces().get()) {
		// exclTime -= child.getResponseTime();
		// }

		exclTime -= getSubTraces().stream().mapToLong(SubTrace::getResponseTime).sum();

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

}
