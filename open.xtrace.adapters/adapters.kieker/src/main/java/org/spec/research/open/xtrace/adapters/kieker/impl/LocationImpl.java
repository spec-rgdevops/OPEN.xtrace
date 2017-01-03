package org.spec.research.open.xtrace.adapters.kieker.impl;

import java.io.Serializable;
import java.util.Optional;

import kieker.tools.traceAnalysis.systemModel.Execution;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.utils.StringUtils;

/**
 * Implementation of Location interface.
 *
 * @author Okanovic
 */
public class LocationImpl implements Location, Serializable {

	private static final long serialVersionUID = 8959194364402329417L;
	private static final String UNKNOWN = "UNKNOWN";
	private String host = UNKNOWN;
	private Optional<String> runTimeEnvironment = Optional.empty();
	private Optional<String> application = Optional.empty();
	private Optional<String> businessTransaction = Optional.empty();
	private Optional<String> nodeType = Optional.empty();
	private Optional<String> serverName = Optional.empty();

	public LocationImpl() {
	}

	public LocationImpl(String host, String runTimeEnvironment, String application, String businessTransaction, String nodeType) {
		this.host = host;
		this.setRunTimeEnvironment(runTimeEnvironment);
		this.setApplication(application);
		this.setBusinessTransaction(businessTransaction);
		this.setNodeType(nodeType);
	}

	public LocationImpl(Execution receivingExecution) {
		this(receivingExecution.getAllocationComponent().getExecutionContainer().getName(), null, null, null, null);
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public Optional<String> getRuntimeEnvironment() {
		return runTimeEnvironment;
	}

	@Override
	public Optional<String> getApplication() {
		return application;
	}

	@Override
	public Optional<String> getBusinessTransaction() {
		return businessTransaction;
	}

	@Override
	public Optional<String> getNodeType() {
		return nodeType;
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setRunTimeEnvironment(String runTimeEnvironment) {
		this.runTimeEnvironment = Optional.ofNullable(runTimeEnvironment);
	}

	public void setApplication(String application) {
		this.application = Optional.ofNullable(application);
	}

	public void setBusinessTransaction(String businessTransaction) {
		this.businessTransaction = Optional.ofNullable(businessTransaction);
	}

	public void setNodeType(String nodeType) {
		this.nodeType = Optional.ofNullable(nodeType);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Location))
			return false;
		LocationImpl otherLocation = (LocationImpl) obj;
		return this.host.equals(otherLocation.host) && this.runTimeEnvironment.equals(otherLocation.runTimeEnvironment)
				&& this.application.equals(otherLocation.application) && this.businessTransaction.equals(otherLocation.businessTransaction)
				&& this.nodeType.equals(otherLocation.nodeType);
	}

	@Override
	public Optional<String> getServerName() {
		return serverName;
	}
}