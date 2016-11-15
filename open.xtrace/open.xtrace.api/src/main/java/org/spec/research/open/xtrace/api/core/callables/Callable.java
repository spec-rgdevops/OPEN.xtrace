package org.spec.research.open.xtrace.api.core.callables;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.Identifiable;
import org.spec.research.open.xtrace.api.core.SubTrace;

/**
 * A {@link Callable} represents a node in a {@link SubTrace}, hence, stands for any callable
 * behaviour (e.g. operation execution). A {@link Callable} is iterable in the sense that the
 * iterator traverses the sub-tree below the corresponding {@link Callable} instance.
 *
 * @author Alexander Wert, Christoph Heger
 */

public interface Callable extends Identifiable {
    /**
     * Returns the parent {@link NestingCallable} of the current {@link Callable} within the tree
     * structure of the corresponding {@link SubTrace}.
     * <p>
     * If the current {@link Callable} is the root of the current {@link SubTrace} this method
     * returns null.
     *
     * @return the parent or null if this is the root of the {@link SubTrace} instance
     */
    NestingCallable getParent();

    /**
     * Returns the {@link SubTrace} that acts as a container for this {@link Callable}.
     *
     * @return returns the {@link SubTrace} this {@link Callable} belongs to.
     */
    SubTrace getContainingSubTrace();

    /**
     * Returns the timestamp when the control started the execution.
     *
     * @return entry timestamp to the {@link Callable} in milliseconds
     */
    long getTimestamp();

    /**
     * Labels convey simple additional information to for individual {@link Callable} instances.
     *
     * @return an {@link Optional} with an <b>unmodifiable list</b> of labels as value
     */
    Optional<List<String>> getLabels();

    /**
     * Returns a collection of additional information that may be tool specific following a convention.
     *
     * @return an {@link Optional} with an <b>unmodifiable list</b> of all additional information objects as value
     */
    Optional<Collection<AdditionalInformation>> getAdditionalInformation();

    /**
     * Returns a list of all additional information objects of the provided type.
     *
     * @param type the {@link AdditionalInformation} type for which the information should be
     *             retrieved
     * @param <T>  Class type of the retrieved additional information
     * @return an {@link Optional} with an <b>unmodifiable list</b> of additional information objects of the provided type as value if present or an {@link Optional} that is empty
     */
    <T extends AdditionalInformation> Optional<Collection<T>> getAdditionalInformation(Class<T> type);

    /**
     * Returns the name of the executed thread.
     *
     * @return an {@link Optional} with the name of the executed thread
     */
    Optional<String> getThreadName();
    
    /**
     * Returns the ID of the executed thread.
     *
     * @return an {@link Optional} with the ID of the executed thread
     */
    Optional<Long> getThreadID();
}
