package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.io.Serializable;
import java.util.List;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.core.callables.TimedCallable;

/**
 * @author Okanovic
 *
 */
public class AbstractTimedCallableImpl extends AbstractCallableImpl implements TimedCallable, Serializable {

	private static final long serialVersionUID = 8126224575872353423L;

	protected long responseTime = -1;
	private transient long exclusiveTime = -1;

	public AbstractTimedCallableImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace);
	}

	public AbstractTimedCallableImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, long responseTime, long exclusiveTime) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace);
		this.responseTime = responseTime;
		this.exclusiveTime = exclusiveTime;
	}

	@Override
	public long getExclusiveTime() {
		if (exclusiveTime < 0) {
			exclusiveTime = responseTime;
			if (this instanceof NestingCallable) {
				for (TimedCallable tCallable : ((NestingCallable) this).getCallees(TimedCallable.class)) {
					exclusiveTime -= tCallable.getResponseTime();
				}
			}
		}
		return exclusiveTime;
	}

	@Override
	public long getResponseTime() {
		return responseTime;
	}

	@Override
	public long getExitTime() {
		return getTimestamp() + Math.round(((double) responseTime) * Trace.NANOS_TO_MILLIS_FACTOR);
	}
}