package org.spec.research.open.xtrace.api.core.callables;

import java.util.Optional;

/**
 * Represents a thrown exception in a trace / sub-trace.
 *
 * @author Alexander Wert, Christoph Heger, Manuel Palenga, Alper Hidiroglu
 */
public interface ExceptionThrow extends Callable {
    /**
     * Returns the error message of the exception.
     *
     * @return a {@link String} with the error message of the thrown exception
     */
    String getErrorMessage();

    /**
     * Returns the cause of the exception.
     *
     * @return an {@link Optional} with the cause of the thrown exception as value. Empty {@link Optional} when not present.
     */
    Optional<String> getCause();
    
    /**
     * Returns the causing of the exception.
     *
     * @return an {@link Optional} with the causing of the thrown exception as NestingExceptionThrow. Empty {@link Optional} when not present.
     */
    Optional<NestingExceptionThrow> getCausing();

    /**
     * Returns the stacktrace of the exception.
     *
     * @return an {@link Optional} with the stacktrace at the time of throwing the exception as value. Empty {@link Optional} when not present.
     */
    Optional<String> getStackTrace();

    /**
     * Returns the throwable type as {@link String}.
     *
     * @return an {@link Optional} with the full qualified class name of the type of the throwable as value. Empty {@link Optional} when not present.
     */
    Optional<String> getThrowableType();
}
