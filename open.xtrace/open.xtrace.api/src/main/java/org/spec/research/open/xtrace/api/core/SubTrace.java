package org.spec.research.open.xtrace.api.core;

import java.util.List;

import org.spec.research.open.xtrace.api.core.callables.Callable;

/**
 * A {@link SubTrace} represent an extract of the logical {@link Trace} that is executed within one
 * {@link Location}. Hence, operation executions ({@link Callable} instances) that have different
 * locations must not belong to the same {@link SubTrace}.
 *
 * @author Alexander Wert, Christoph Heger
 */
public interface SubTrace extends TimedElement, TreeIterable<Callable>, Identifiable {
    /**
     * Returns the root of the {@link SubTrace}.
     *
     * @return the root {@link Callable} of the {@link SubTrace} (i.e. operation that represents the
     * root of the call tree)
     */
    Callable getRoot();

    /**
     * Returns the parent {@link SubTrace} containing this {@link SubTrace}.
     *
     * @return the parent {@link SubTrace} within the composite (i.e. tree) structure of
     * {@link SubTrace} instances. Returns null if no parent exists.
     */
    SubTrace getParent();

    /**
     * Returns all child {@link SubTrace}s.
     *
     * @return an <b>unmodifiable list</b> containing the child {@link SubTrace} instances within the composite (i.e. tree) structure of
     * sub traces. Returns an empty list if no sub traces exist.
     */
    List<SubTrace> getSubTraces();

    /**
     * Returns the {@link Location} information of this {@link SubTrace}.
     *
     * @return the unique {@link Location} of the {@link SubTrace} Two operation executions (
     * {@link Callable} instances) with different {@link Location} instances belong to
     * different {@link SubTrace} instances.
     */
    Location getLocation();

    /**
     * Returns the {@link Trace} containing this {@link SubTrace}.
     *
     * @return the {@link Trace} instance this {@link SubTrace} instance belongs to
     */
    Trace getContainingTrace();

    /**
     * Returns the identifier of the {@link SubTrace}.
     *
     * @return unique identifier of the {@link SubTrace}
     */
    long getSubTraceId();

    /**
     * Returns the number of {@link Callable}s in this {@link SubTrace}.
     *
     * @return the number of nodes (i.e. {@link Callable}) in the tree structure of the
     * corresponding {@link SubTrace}
     */
    int size();
}
