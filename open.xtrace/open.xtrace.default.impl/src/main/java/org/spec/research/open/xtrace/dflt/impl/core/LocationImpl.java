package org.spec.research.open.xtrace.dflt.impl.core;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Location;
import org.spec.research.open.xtrace.api.utils.StringUtils;

/**
 * Default implementation of the {@link Location} interface of the
 * 'OPEN.xtrace'.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public class LocationImpl implements Location, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7989999424512666828L;

	/**
	 * Identifies the machine.
	 */
	private String host = null;

	/**
	 * Identifies the runtime environment (e.g. the JVM).
	 */
	private Optional<String> runTimeEnvironment = Optional.empty();

	/**
	 * Identifies the application.
	 */
	private Optional<String> application = Optional.empty();

	/**
	 * Identifies the business transaction.
	 */
	private Optional<String> businessTransaction = Optional.empty();

	/**
	 * Identifies the node type.
	 */
	private Optional<String> nodeType = Optional.empty();

	/**
	 * Identifies the server name.
	 */
	private Optional<String> serverName = Optional.empty();

	/**
	 * Default constructor.
	 */
	public LocationImpl() {
	}

	/**
	 * Constructor.
	 * 
	 * @param host
	 *            Identifies the machine
	 * @param runTimeEnvironment
	 *            Identifies the runtime environment
	 * @param application
	 *            Identifies the application
	 * @param businessTransaction
	 *            Identifies the business transaction
	 */
	public LocationImpl(String host, String runTimeEnvironment, String application, String businessTransaction) {
		this.setHost(host);
		this.setRunTimeEnvironment(runTimeEnvironment);
		this.setApplication(application);
		this.setBusinessTransaction(businessTransaction);
	}

	/**
	 * Constructor.
	 * 
	 * @param host
	 *            Identifies the machine
	 */
	public LocationImpl(String host) {
		this.setHost(host);
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

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param runTimeEnvironment
	 *            the runTimeEnvironment to set
	 */
	public void setRunTimeEnvironment(String runTimeEnvironment) {
		this.runTimeEnvironment = Optional.ofNullable(runTimeEnvironment);
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(String application) {
		this.application = Optional.ofNullable(application);
	}

	/**
	 * @param businessTransaction
	 *            the businessTransaction to set
	 */
	public void setBusinessTransaction(String businessTransaction) {
		this.businessTransaction = Optional.ofNullable(businessTransaction);
	}

	/**
	 * @param nodeType
	 *            the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = Optional.ofNullable(nodeType);
	}

	@Override
	public Optional<String> getServerName() {
		return this.serverName;
	}

	/**
	 * @param serverName
	 *            the server name
	 */
	public void setServerName(String serverName) {
		this.serverName = Optional.ofNullable(serverName);
	}

}
