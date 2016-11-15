package org.spec.research.open.xtrace.api.core.callables;

import java.util.List;

import org.spec.research.open.xtrace.api.core.TreeIterable;

/**
 * A {@link NestingCallable} is a {@link Callable} that can have other {@link Callable} instances as
 * children.
 *
 * @author Alexander Wert
 */
public interface NestingCallable extends TimedCallable, TreeIterable<Callable> {
    /**
     * Returns a list of all {@link Callable} instances invoked by the current
     * {@link NestingCallable}. The list contains the {@link Callable} instances in the order they
     * have been called. If no nested {@link Callable} instances are available, this method returns
     * an empty list.
     *
     * @return an <b>unmodifiable list</b> of all {@link Callable} instances invoked by the current
     * {@link NestingCallable}.
     */
    List<Callable> getCallees();

    /**
     * Returns a list of all {@link Callable} instances invoked by the current
     * {@link NestingCallable} <b>that match the given type</b>. The list contains the
     * {@link Callable} instances in the order they have been called. If no nested {@link Callable}
     * instances are available, this method returns an empty list.
     *
     * @param type class of the type defining a filter for the callees to be retrieved
     * @param <T>  type defining a filter
     * @return an <b>unmodifiable list</b> of all {@link Callable} instances invoked by the current
     * {@link NestingCallable}.
     */
    <T extends Callable> List<T> getCallees(Class<T> type);

    /**
     * Returns the number of nodes below this {@link NestingCallable} transitively including all
     * children, grandchildren, etc.
     *
     * @return the number of nodes below this {@link NestingCallable}
     */
    int getChildCount();
}
