package org.spec.research.open.xtrace.api.core;

import java.util.Optional;

/**
 * A {@link Location} specifies the execution context of a single {@link SubTrace}.
 *
 * @author Alexander Wert, Christoph Heger
 */
public interface Location {


    /**
     * Returns the host where the trace {@link SubTrace} was collected.
     *
     * @return unique host name or IP of the corresponding system node. "unknown" if not specified.
     */
    String getHost();

    /**
     * Returns the runtime environment.
     *
     * @return {@link Optional} with the identifier of the run-time container (in Java: JVM, in .NET: CLR, etc.) as value. Empty {@link Optional} if
     * not specified.
     */
    Optional<String> getRuntimeEnvironment();

    /**
     * Returns the application identifier.
     *
     * @return {@link Optional} with the identifier of the application the {@link SubTrace} belongs to as value. Empty {@link Optional} if not
     * specified.
     */
    Optional<String> getApplication();

    /**
     * Returns the business transaction.
     *
     * @return {@link Optional} with the identifier of the business transaction {@link SubTrace} belongs to as value. Empty {@link Optional} if not
     * specified.
     */
    Optional<String> getBusinessTransaction();

    /**
     * Returns the type of the node.
     *
     * @return {@link Optional} with the identifier of the node type (e.g. "Application Node", "Messaging Node", etc.) the
     * {@link SubTrace} belongs to as value. Empty {@link Optional} if not specified.
     */
    Optional<String> getNodeType();
    
    /**
     * Returns the name of the server.
     *
     * @return {@link Optional} with the name of the server. Empty {@link Optional} if not specified.
     */
    Optional<String> getServerName();
}
