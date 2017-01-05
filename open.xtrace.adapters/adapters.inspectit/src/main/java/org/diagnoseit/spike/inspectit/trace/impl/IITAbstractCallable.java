package org.diagnoseit.spike.inspectit.trace.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;

import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITAbstractCallable extends IITAbstractIdentifiableImpl implements Callable {

	/** Serial version id. */
	private static final long serialVersionUID = -7053659087635453623L;
	
	protected final InvocationSequenceData isData;
	protected IITAbstractNestingCallable parent = null;
	protected final IITSubTraceImpl containingTrace;
	private String threadName;
	private long threadID;

	public IITAbstractCallable(InvocationSequenceData isData, IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent) {
		super(isData.getId());
		this.isData = isData;
		this.containingTrace = containingTrace;
		this.parent = parent;
	}

	@Override
	public Optional<Collection<AdditionalInformation>> getAdditionalInformation() {
		return Optional.empty();
	}

	@Override
	public <T extends AdditionalInformation> Optional<Collection<T>> getAdditionalInformation(Class<T> arg0) {
		return Optional.empty();
	}

	@Override
	public SubTrace getContainingSubTrace() {
		return containingTrace;
	}

	@Override
	public Optional<List<String>> getLabels() {
		return Optional.empty();
	}

	@Override
	public NestingCallable getParent() {
		return parent;
	}

	@Override
	public long getTimestamp() {
		return isData.getTimeStamp().getTime();
	}

	@Override
	public Optional<String> getThreadName() {
		return Optional.of(threadName);
	}

	@Override
	public Optional<Long> getThreadID() {
		return Optional.of(threadID);
	}

}
