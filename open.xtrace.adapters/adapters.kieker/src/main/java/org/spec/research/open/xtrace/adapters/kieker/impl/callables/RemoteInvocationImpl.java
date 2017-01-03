package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;

/**
 * @author Okanovic
 *
 */
public class RemoteInvocationImpl extends AbstractTimedCallableImpl implements RemoteInvocation {

	private static final long serialVersionUID = 1129980818226035999L;

	private String target;
	private SubTrace targetSubTrace;

	public RemoteInvocationImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, SubTrace targetSubTrace) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace);
		this.targetSubTrace = targetSubTrace;
	}

	@Override
	public String getTarget() {
		if (targetSubTrace != null && target == null) {
			target = targetSubTrace.getLocation().toString();
		}
		return target;
	}

	@Override
	public Optional<Location> getTargetLocation() {
		if (targetSubTrace != null) {
			return Optional.ofNullable(targetSubTrace.getLocation());
		}
		return Optional.empty();
	}

	@Override
	public Optional<SubTrace> getTargetSubTrace() {
		return Optional.ofNullable(targetSubTrace);
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}
}