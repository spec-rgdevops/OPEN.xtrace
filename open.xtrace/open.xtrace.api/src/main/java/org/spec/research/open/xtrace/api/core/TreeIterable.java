package org.spec.research.open.xtrace.api.core;

/**
 * A {@link TreeIterable} can be applied on data structures that have a tree structure. The
 * corresponding iterator interface {@link TreeIterator} provides for specifying additional
 * functionality regarding a tree structure.
 * 
 * @author Alexander Wert
 *
 * @param <E>
 *            type of the elements to iterate.
 */
public interface TreeIterable<E> extends Iterable<E> {

	/**
	 * Overwrites the return type of the corresponding method of the {@link Iterable} interface.
	 * 
	 * @return {@link TreeIterator} instance
	 */
	@Override
	TreeIterator<E> iterator();
}
