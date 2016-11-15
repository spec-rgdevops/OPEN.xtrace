package org.spec.research.open.xtrace.api.core.callables;

import org.spec.research.open.xtrace.api.core.TimedElement;

/**
 * A {@link TimedCallable} is a specialized {@link Callable} that has a notion of duration (i.e.
 * response time and exclusive time).
 *
 * @author Alexander Wert
 */
public interface TimedCallable extends Callable, TimedElement {

    /**
     * Returns the timestamp when the control flow left the {@link TimedCallable}.
     *
     * @return exit timestamp when leaving the {@link TimedCallable} in milliseconds
     */
    long getExitTime();
}
