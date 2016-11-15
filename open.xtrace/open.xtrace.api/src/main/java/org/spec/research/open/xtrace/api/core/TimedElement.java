package org.spec.research.open.xtrace.api.core;

/**
 * Represents any element that has a duration.
 *
 * @author Alexander Wert
 */
public interface TimedElement {
    /**
     * Returns the exclusive time of this {@link TimedElement} instance. The exclusive time is the
     * duration of this {@link TimedElement} instance excluding all nested {@link TimedElement}
     * instances. If this {@link TimedElement} instance does not have nested elements, then the
     * exclusive time is equal to the response time as no children exist.
     *
     * @return exclusive time in nanoseconds
     */
    long getExclusiveTime();

    /**
     * Returns the response time of the this {@link TimedElement} including the response time of called methods.
     *
     * @return response time (including the response time of nested {@link TimedElement} instances)
     * in nanoseconds
     */
    long getResponseTime();
}
