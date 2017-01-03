package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.DatabaseInvocation;

/**
 * @author Okanovic
 *
 */
public class DatabaseInvocationImpl extends AbstractTimedCallableImpl implements DatabaseInvocation {

	private static final long serialVersionUID = -5261233572253671375L;

	public DatabaseInvocationImpl() {
		this(null, 0, null, null, null, 0, 0);
		// TODO only temporary, must be removed later
	}

	public DatabaseInvocationImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, long responseTime, long exclusiveTime) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace, responseTime, exclusiveTime);
		// TODO Auto-generated constructor stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Boolean> isPrepared() {
		return Optional.empty();
	}

	@Override
	public String getSQLStatement() {
		return "";
	}

	@Override
	public Optional<String> getBoundSQLStatement() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getUnboundSQLStatement() {
		return Optional.empty();
	}

	@Override
	public Optional<Map<Integer, String>> getParameterBindings() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getDBProductName() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getDBProductVersion() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getDBUrl() {
		return Optional.empty();
	}

}
