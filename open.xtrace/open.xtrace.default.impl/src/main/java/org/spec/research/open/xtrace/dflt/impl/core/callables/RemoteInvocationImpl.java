package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.MobileRemoteMeasurement;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.MobileMetadataMeasurement;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.MobileRemoteMeasurementImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

/**
 * Default implementation of the {@link RemoteInvocation} API element.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public class RemoteInvocationImpl extends AbstractTimedCallableImpl implements RemoteInvocation, Serializable {

	/** Serial version id. */
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
	 * Measurement at the beginning of a remote call.
	 */
	private MobileRemoteMeasurementImpl requestMeasurement;
	
	/**
	 * Measurement at the ending of a remote call.
	 */
	private MobileRemoteMeasurementImpl responseMeasurement;

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

	@Override
	public Optional<Location> getTargetLocation() {
		return Optional.ofNullable(targetSubTrace.getLocation());
	}

	@Override
	public String getTarget() {
		if (targetSubTrace != null && target == null) {
			target = targetSubTrace.getLocation().toString();
		}
		return target;
	}
	
	@Override
	public Optional<MobileRemoteMeasurement> getRequestMeasurement() {
		return Optional.ofNullable(requestMeasurement);
	}

	@Override
	public Optional<MobileRemoteMeasurement> getResponseMeasurement() {
		return Optional.ofNullable(responseMeasurement);
	}

	/**
	 * Set the measurement at the ending of this remote call.
	 * 
	 * @param responseMeasurement
	 */
	public void setResponseMeasurement(MobileRemoteMeasurementImpl responseMeasurement) {
		this.responseMeasurement = responseMeasurement;
	}
	
	/**
	 * Set the measurement at the beginning of this remote call.
	 * 
	 * @param responseMeasurement
	 */
	public void setRequestMeasurement(MobileRemoteMeasurementImpl requestMeasurement) {
		this.requestMeasurement = requestMeasurement;
	}

	/**
	 * Set the target of this remote call.
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
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
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

}
