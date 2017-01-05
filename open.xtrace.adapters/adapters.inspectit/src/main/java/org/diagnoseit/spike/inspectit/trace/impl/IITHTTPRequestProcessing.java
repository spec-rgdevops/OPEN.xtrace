package org.diagnoseit.spike.inspectit.trace.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.HTTPMethod;
import org.spec.research.open.xtrace.api.core.callables.HTTPRequestProcessing;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import rocks.inspectit.shared.all.communication.data.HttpTimerData;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class IITHTTPRequestProcessing extends IITAbstractNestingCallable implements HTTPRequestProcessing {
	
	/** Serial version id. */
	private static final long serialVersionUID = 6077981592141469965L;
	private HttpTimerData httpData;

	public IITHTTPRequestProcessing(InvocationSequenceData isData, IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent) {
		super(isData, containingTrace, parent);
		httpData = (HttpTimerData) isData.getTimerData();
	}

	@Override
	public Optional<Map<String, String>> getHTTPAttributes() {
		return Optional.ofNullable(Collections.unmodifiableMap(httpData.getAttributes()));
	}

	@Override
	public Optional<Map<String, String>> getHTTPHeaders() {
		return Optional.ofNullable(Collections.unmodifiableMap(httpData.getHeaders()));
	}

	@Override
	public Optional<Map<String, String[]>> getHTTPParameters() {
		return Optional.ofNullable(Collections.unmodifiableMap(httpData.getParameters()));
	}

	@Override
	public Optional<Map<String, String>> getHTTPSessionAttributes() {
		return Optional.ofNullable(Collections.unmodifiableMap(httpData.getSessionAttributes()));
	}

	@Override
	public Optional<HTTPMethod> getRequestMethod() {
		throw new UnsupportedOperationException(); 
		//return Optional.ofNullable(HTTPMethod.valueOf(httpData.getRequestMethod().toUpperCase()));
	}

	@Override
	public String getUri() {
		throw new UnsupportedOperationException(); 
		//return httpData.getUri();
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public Optional<Long> getResponseCode() {
		// Not supported
		return Optional.empty();
	}

	@Override
	public Optional<Map<String, String>> getResponseHTTPHeaders() {
		// Not supported
		return Optional.empty();
	}
}
