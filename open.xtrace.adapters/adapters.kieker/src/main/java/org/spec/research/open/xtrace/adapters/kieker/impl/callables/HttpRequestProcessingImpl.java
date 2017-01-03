package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.HTTPMethod;
import org.spec.research.open.xtrace.api.core.callables.HTTPRequestProcessing;

/**
 * @author Okanovic
 *
 */
public class HttpRequestProcessingImpl extends AbstractNestingCallableImpl implements HTTPRequestProcessing {

	private static final long serialVersionUID = 6495741068107006151L;

	public HttpRequestProcessingImpl() {
		this(null, 0, null, null, null, 0, 0);
		// TODO only temporary, must be removed later
	}

	public HttpRequestProcessingImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, long responseTime, long exclusiveTime) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace, responseTime, exclusiveTime);
		// TODO Auto-generated constructor stub
		throw new UnsupportedOperationException();
	}

	// TODO unfinished

	@Override
	public Optional<Map<String, String>> getHTTPAttributes() {
		return Optional.empty();
	}

	@Override
	public Optional<Map<String, String>> getHTTPHeaders() {
		return Optional.empty();
	}

	@Override
	public Optional<Map<String, String[]>> getHTTPParameters() {
		return Optional.empty();
	}

	@Override
	public Optional<Map<String, String>> getHTTPSessionAttributes() {
		return Optional.empty();
	}

	@Override
	public Optional<HTTPMethod> getRequestMethod() {
		return Optional.empty();
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Long> getResponseCode() {
		return Optional.empty();
	}

	@Override
	public Optional<Map<String, String>> getResponseHTTPHeaders() {
		return Optional.empty();
	}

}
