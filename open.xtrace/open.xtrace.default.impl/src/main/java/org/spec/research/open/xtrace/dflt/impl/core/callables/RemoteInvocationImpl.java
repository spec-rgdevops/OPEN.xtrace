package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

/**
 * Default implementation of the {@link RemoteInvocation} API element.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public class RemoteInvocationImpl extends AbstractTimedCallableImpl implements RemoteInvocation, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5976896106312375101L;

	/**
	 * subtrace invoked by this remote call.
	 */
	private SubTraceImpl targetSubTrace;

	/**
	 * String representation of the target.
	 */
	private String target;

	/**
	 * Default constructor for serialization. This constructor should not be used except for
	 * deserialization.
	 */
	public RemoteInvocationImpl() {
	}

	/**
	 * Constructor. Adds the newly created {@link Callable} instance to the passed parent if the
	 * parent is not null!
	 * 
	 * @param parent
	 *            {@link AbstractNestingCallableImpl} that called this Callable
	 * @param containingSubTrace
	 *            the SubTrace containing this Callable
	 */
	public RemoteInvocationImpl(AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {
		super(parent, containingSubTrace);
	}

	@Override
	public Optional<SubTrace> getTargetSubTrace() {
		return Optional.ofNullable(targetSubTrace);
	}

	/**
	 * Sets the target SubTrace.
	 * 
	 * @param targetSubTrace
	 *            {@link SubTraceImpl} instance to set.
	 */
	public void setTargetSubTrace(SubTraceImpl targetSubTrace) {
		this.targetSubTrace = targetSubTrace;
	}

	@Override
	public Optional<Location> getTargetLocation() {
//		if (targetSubTrace != null) {
//			return targetSubTrace.getLocation();
//		}
//		return null;
		return Optional.ofNullable(targetSubTrace.getLocation());
	}

	@Override
	public String getTarget() {
		if (targetSubTrace != null && target == null) {
			target = targetSubTrace.getLocation().toString();
		}
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

}
