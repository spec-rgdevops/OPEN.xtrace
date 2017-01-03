package org.spec.research.open.xtrace.adapters.kieker.impl;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.Identifiable;

/**
 * Implementation of the {@link Identifiable} interface of the 'OPEN.xtrace'.
 * 
 * @author Christoph Heger
 *
 */
public abstract class AbstractIdentifiableImpl implements Identifiable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5844714352437823529L;

	/**
	 * The identifier.
	 */
	private Object identifier;

	@Override
	public Optional<Object> getIdentifier() {
		return Optional.ofNullable(identifier);
	}

	@Override
	public void setIdentifier(Object id) {
		identifier = id;
	}

}
