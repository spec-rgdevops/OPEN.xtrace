package org.spec.research.open.xtrace.api.core;

import java.util.Optional;

/**
 * A {@link Identifiable} can provide an unique identifier.
 * 
 * @author Christoph Heger
 *
 */
public interface Identifiable {

	/**
	 * Provides the unique identifier.
	 * 
	 * @return an {@link Optional} with the identifier as value or an empty
	 *         {@link Optional} when not present.
	 */
	Optional<Object> getIdentifier();

	/**
	 * Set the unique identifier.
	 * 
	 * @param id identifier to set.
	 */
	void setIdentifier(Object id);
}
