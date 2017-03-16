package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.util.List;

import org.spec.research.open.xtrace.adapters.inspectit.importer.MobileTraceData;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.utils.CallableIteratorOnTrace;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.api.utils.SubTraceIterator;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.cmr.service.ICachedDataService;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITTraceImpl extends IITAbstractIdentifiableImpl implements Trace {

	/** Serial version id. */
	private static final long serialVersionUID = 2768574993119101303L;

	private SubTrace root;
	private ICachedDataService cachedDataService;

	public IITTraceImpl(InvocationSequenceData root, ICachedDataService cachedDataService) {
		super(root.getId());
		this.root = new IITSubTraceImpl(this, root);
		this.cachedDataService = cachedDataService;
	}

	public IITTraceImpl(InvocationSequenceData root, PlatformIdent pIdent) {
		super(root.getId());
		this.root = new IITSubTraceImpl(this, root, pIdent);
	}
	
	public IITTraceImpl(InvocationSequenceData root, List<PlatformIdent> pIdents) {
		super(root.getId());
		this.root = new IITSubTraceImpl(this, root);
	}
	
	/**
	 * Only for this package.
	 * 
	 * @param remoteCalls
	 * @param pIdents
	 * @param spans
	 */
	public IITTraceImpl(MobileTraceData traceData) {		
		super((traceData.getSpans().get(0).hashCode() * (long)Math.pow(10, String.valueOf(traceData.getSpans().get(0).getDuration()).length() + 1)) + (int)traceData.getSpans().get(0).getDuration());
		this.root = new IITSubTraceImpl(this, traceData);
	}
	
	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIteratorOnTrace(root);
	}

	@Override
	public SubTrace getRoot() {
		return root;
	}

	@Override
	public long getTraceId() {
		return root.getSubTraceId();
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public int size() {
		return root.size();
	}

	@Override
	public TreeIterator<SubTrace> subTraceIterator() {
		return new SubTraceIterator(root);
	}

	protected ICachedDataService getCachedDataService() {
		return cachedDataService;
	}

	@Override
	public long getExclusiveTime() {
		return getResponseTime();
	}

	@Override
	public long getResponseTime() {
		if (root == null) {
			return 0;
		} else {
			return root.getResponseTime();
		}
	}

}
