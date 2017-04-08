package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.adapters.inspectit.importer.MobileTraceData;
import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.TimedCallable;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.HttpTimerData;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITSubTraceImpl extends IITAbstractIdentifiableImpl implements SubTrace, Location {

	/** Serial version id. */
	private static final long serialVersionUID = -8069154836545294257L;

	public static final String UNKNOWN = "UNKNOWN";

	private final InvocationSequenceData sequenceData;
	private final Callable root;
	private PlatformIdent pIdent;
	protected IITTraceImpl trace;
	private Long platformID;

	public IITSubTraceImpl(IITTraceImpl containingTrace, InvocationSequenceData isData, PlatformIdent pIdent) {

		super(isData.getId());
		this.trace = containingTrace;
		this.root = createCallable(isData, this, null);
		this.sequenceData = isData;
		this.pIdent = pIdent;
		this.platformID = pIdent.getId();
	}

	public IITSubTraceImpl(IITTraceImpl containingTrace, InvocationSequenceData isData) {

		super(isData.getId());
		this.trace = containingTrace;
		this.root = createCallable(isData, this, null);
		this.sequenceData = isData;
		this.platformID = isData.getPlatformIdent();
	}
	
	/**
	 * Only for this package.
	 * 
	 * @param containingTrace
	 * @param spans
	 */
	IITSubTraceImpl(IITTraceImpl containingTrace, MobileTraceData traceData) {
		super((traceData.getSpans().get(0).hashCode() * (long)Math.pow(10, String.valueOf(traceData.getSpans().get(0).getDuration()).length() + 1)) + (int)traceData.getSpans().get(0).getDuration());
		this.trace = containingTrace;
		this.root = (new SpanConverterHelper()).createCallable(this, null, trace, traceData);
		this.sequenceData = null;
		this.platformID = null;
	}

	protected IITAbstractCallable createCallable(InvocationSequenceData isData, IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent) {
		if (isData.getSqlStatementData() != null) {
			return new IITDatabaseInvocation(isData, containingTrace, parent);
		} else if (isData.getTimerData() != null && isData.getTimerData() instanceof HttpTimerData) {
			return new IITHTTPRequestProcessing(isData, containingTrace, parent);
		} else {
			return new IITMethodInvocation(isData, containingTrace, parent);
		}
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

		return (int) sequenceData.getChildCount() + 1;
	}

	@Override
	public TreeIterator<Callable> iterator() {

		return new CallableIterator(root);
	}

	@Override
	public String getHost() {

		PlatformIdent ident = getPlatformIdent();
		if(ident == null || ident.getDefinedIPs() == null || ident.getDefinedIPs().isEmpty()){
			return UNKNOWN;
		}
		for (String ip : ident.getDefinedIPs()) {
			if (!ip.contains(":")) {
				return ip;
			}
		}
		return UNKNOWN;		
	}

	@Override
	public Optional<String> getRuntimeEnvironment() {
		PlatformIdent ident = getPlatformIdent();
		if(ident == null){
			return Optional.empty();
		}
		return Optional.ofNullable(ident.getAgentName() + (ident.getId() == null ? "" : "-" + ident.getId()));
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
		result = prime * result + ((sequenceData == null) ? 0 : (int) sequenceData.getId());
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
		if (sequenceData == null) {
			if (other.sequenceData != null) {
				return false;
			}
		} else if (sequenceData.getId() != other.sequenceData.getId()) {
			return false;
		}
		return true;
	}

	@Override
	public Optional<String> getNodeType() {

		return Optional.ofNullable(getPlatformIdent().getAgentName());
	}

	protected PlatformIdent getPlatformIdent() {

		if (pIdent == null && trace.getCachedDataService() != null) {
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

		if(sequenceData == null){
			if(root instanceof TimedCallable){
				TimedCallable callable = (TimedCallable) root;
				return callable.getResponseTime();
			}
			return -1;
		}
		return Math.round(sequenceData.getDuration() * Trace.MILLIS_TO_NANOS_FACTOR);
	}

	@Override
	public long getSubTraceId() {

		if(sequenceData == null){
			return -1;
		}
		return sequenceData.getId();
	}

	@Override
	public Optional<String> getServerName() {
		// Not supported
		return Optional.empty();
	}

}
