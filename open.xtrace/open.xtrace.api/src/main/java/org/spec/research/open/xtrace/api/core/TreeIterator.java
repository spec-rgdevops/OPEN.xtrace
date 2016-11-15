package org.spec.research.open.xtrace.api.core;

import java.util.Iterator;

/**
 * The {@link TreeIterator} is a specialization of an {@link Iterator} providing additional
 * functionality related to a tree structure.
 *
 * @param <E> type of the elements to iterate.
 * @author Alexander Wert
 */
public interface TreeIterator<E> extends Iterator<E> {

    /**
     * Returns the current depth.
     *
     * @return the current depth of the iterator position in the tree
     */
    int currentDepth();
}
