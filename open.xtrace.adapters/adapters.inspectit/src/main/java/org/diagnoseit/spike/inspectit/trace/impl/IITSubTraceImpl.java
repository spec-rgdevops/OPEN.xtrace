package org.diagnoseit.spike.inspectit.trace.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import info.novatec.inspectit.cmr.model.PlatformIdent;
import info.novatec.inspectit.communication.data.HttpTimerData;
import info.novatec.inspectit.communication.data.InvocationSequenceData;
import info.novatec.inspectit.communication.data.InvocationSequenceDataHelper;

public class IITSubTraceImpl extends IITAbstractIdentifiableImpl implements SubTrace, Location {

	public static final String UNKNOWN = "UNKNOWN";

	private final InvocationSequenceData isData;
	private final Callable root;
	private PlatformIdent pIdent;
	protected IITTraceImpl trace;
	private long platformID;

	public IITSubTraceImpl(IITTraceImpl containingTrace, InvocationSequenceData isData, PlatformIdent pIdent) {

		super(isData.getId());
		this.root = IITTraceImpl.createCallable(isData, this, null);
		this.isData = isData;
		this.pIdent = pIdent;
		// platformID = pIdent.getId();
		platformID = 6352;
		trace = containingTrace;
	}

	public IITSubTraceImpl(IITTraceImpl containingTrace, InvocationSequenceData isData) {

		super(isData.getId());
		this.root = IITTraceImpl.createCallable(isData, this, null);
		this.isData = isData;
		this.platformID = isData.getPlatformIdent();
		trace = containingTrace;
		System.out.println("Inner: " + pIdent);
	}

	@Override
	public Callable getRoot() {

		return root;
	}

	@Override
	public SubTrace getParent() {

		return null;
	}

	@Override
	public List<SubTrace> getSubTraces() {

		return Collections.unmodifiableList(Collections.emptyList());
	}

	@Override
	public Location getLocation() {

		return this;
	}

	@Override
	public int size() {

		return (int) isData.getChildCount() + 1;
	}

	@Override
	public TreeIterator<Callable> iterator() {

		return new CallableIterator(root);
	}

	@Override
	public String getHost() {

//		if (getPlatformIdent().getDefinedIPs().isEmpty()) {
//			return UNKNOWN;
//		} else {
//			for (String ip : getPlatformIdent().getDefinedIPs()) {
//				if (!ip.contains(":")) {
//					return ip;
//				}
//			}
			return UNKNOWN;
		//}
	}

	@Override
	public Optional<String> getRuntimeEnvironment() {

		return Optional.empty();
		//return Optional.ofNullable(getPlatformIdent().getAgentName() + (getPlatformIdent().getId() == null ? "" : "-" + getPlatformIdent().getId()));
	}

	@Override
	public Optional<String> getApplication() {

		return Optional.empty();
	}

	@Override
	public Optional<String> getBusinessTransaction() {

		/*
		if (InvocationSequenceDataHelper.hasHttpTimerData(isData)) {
			String useCase = ((HttpTimerData) isData.getTimerData()).getInspectItTaggingHeaderValue();
			if (!HttpTimerData.UNDEFINED.equals(useCase)) {
				return Optional.ofNullable(useCase);
			}
		}
		*/
		return Optional.empty();
		
	}

	@Override
	public String toString() {

		return StringUtils.getStringRepresentation((SubTrace) this);
	}

	@Override
	public Trace getContainingTrace() {

		return trace;
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
		result = prime * result + ((isData == null) ? 0 : (int) isData.getId());
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
		IITSubTraceImpl other = (IITSubTraceImpl) obj;
		if (isData == null) {
			if (other.isData != null) {
				return false;
			}
		} else if (isData.getId() != other.isData.getId()) {
			return false;
		}
		return true;
	}

	@Override
	public Optional<String> getNodeType() {

		return Optional.ofNullable(getPlatformIdent().getAgentName());
	}

	protected PlatformIdent getPlatformIdent() {

		if (pIdent == null) {
			pIdent = trace.getCachedDataService().getPlatformIdentForId(platformID);
		}
		return pIdent;
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

		return Math.round(isData.getDuration() * Trace.MILLIS_TO_NANOS_FACTOR);
	}

	@Override
	public long getSubTraceId() {

		return isData.getId();
	}

	@Override
	public Optional<String> getServerName() {
		// Not supported
		return Optional.empty();
	}

}
