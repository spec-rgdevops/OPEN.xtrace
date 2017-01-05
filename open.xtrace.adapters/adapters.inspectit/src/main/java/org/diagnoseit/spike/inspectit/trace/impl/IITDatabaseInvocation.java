package org.diagnoseit.spike.inspectit.trace.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.DatabaseInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;

import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.SqlStatementData;

public class IITDatabaseInvocation extends IITAbstractTimedCallable implements DatabaseInvocation {
	
	/** Serial version id. */
	private static final long serialVersionUID = 2816048692421267237L;
	private SqlStatementData sqlData;

	public IITDatabaseInvocation(InvocationSequenceData isData, IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent) {
		super(isData, containingTrace, parent);
		sqlData = isData.getSqlStatementData();
	}

	@Override
	public Optional<String> getBoundSQLStatement() {
		return Optional.ofNullable(sqlData.getSqlWithParameterValues());
	}

	@Override
	public Optional<String> getDBProductName() {
		return Optional.ofNullable(sqlData.getDatabaseProductName());
	}

	@Override
	public Optional<String> getDBProductVersion() {
		return Optional.ofNullable(sqlData.getDatabaseProductVersion());
	}

	@Override
	public Optional<String> getDBUrl() {
		return Optional.ofNullable(sqlData.getDatabaseUrl());
	}

	@Override
	public Optional<Map<Integer, String>> getParameterBindings() {
		List<String> pValues = sqlData.getParameterValues();
		if (pValues != null && !pValues.isEmpty()) {
			Map<Integer, String> result = new HashMap<Integer, String>();
			int idx = 1;
			for (String value : pValues) {
				result.put(idx, value);
				idx++;
			}
			return Optional.ofNullable(Collections.unmodifiableMap(result));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public String getSQLStatement() {
		return sqlData.getSql();
	}

	@Override
	public Optional<Boolean> isPrepared() {
		return Optional.ofNullable(sqlData.isPreparedStatement());
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public Optional<String> getUnboundSQLStatement() {
		return Optional.ofNullable(sqlData.getSql());
	}
}
