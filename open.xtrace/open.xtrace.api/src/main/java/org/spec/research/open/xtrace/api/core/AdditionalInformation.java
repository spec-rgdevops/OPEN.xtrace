package org.spec.research.open.xtrace.api.core;

import org.spec.research.open.xtrace.api.core.callables.Callable;

/**
 * This is a super interface for different kind of additional information objects / interfaces to be
 * attached to {@link Callable} instances.
 *
 * @author Alexander Wert
 */
public interface AdditionalInformation {

    /**
     * Returns the name of the additional information type.
     *
     * @return name of the {@link AdditionalInformation} type
     */
    String getName();
}
