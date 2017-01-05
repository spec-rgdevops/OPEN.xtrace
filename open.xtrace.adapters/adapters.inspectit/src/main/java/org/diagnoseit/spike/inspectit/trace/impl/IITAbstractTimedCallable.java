package org.diagnoseit.spike.inspectit.trace.impl;

import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.core.callables.TimedCallable;

import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITAbstractTimedCallable extends IITAbstractCallable implements TimedCallable {

	/** Serial version id. */
	private static final long serialVersionUID = 7076154836629316056L;

	public IITAbstractTimedCallable(InvocationSequenceData isData, IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent) {
		super(isData, containingTrace, parent);
	}

	@Override
	public long getExclusiveTime() {
		if (isData.getNestedSequences() == null || isData.getNestedSequences().isEmpty()) {
			return getResponseTime();
		} else if (isData.getTimerData() == null) {
			long exclusiveDuration = getResponseTime();
			if (this instanceof NestingCallable) {
				for (TimedCallable tCallable : ((NestingCallable) this).getCallees(TimedCallable.class)) {
					exclusiveDuration -= tCallable.getResponseTime();
				}
			}

			return exclusiveDuration;
		} else {
			return Math.round(isData.getTimerData().getExclusiveDuration() * Trace.MILLIS_TO_NANOS_FACTOR);
		}
	}

	@Override
	public long getExitTime() {
		return isData.getTimeStamp().getTime() + Math.round(isData.getDuration());
	}

	@Override
	public long getResponseTime() {
		return Math.round(isData.getDuration() * Trace.MILLIS_TO_NANOS_FACTOR);
	}

}
