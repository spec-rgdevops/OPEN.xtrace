package org.spec.research.open.xtrace.api.core.callables;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link DatabaseInvocation} instance represents a request / call to a database capturing the
 * database request-relevant information.
 *
 * @author Alexander Wert, Christoph Heger
 */
public interface DatabaseInvocation extends TimedCallable {
    /**
     * Indicates whether the corresponding database request has been executed as a prepared
     * statement.
     *
     * @return an {@link Optional} with <code>true</code> as value, if corresponding SQL statement has been prepared, otherwise an empty {@link Optional}.
     */
    Optional<Boolean> isPrepared();

    /**
     * Returns the executed SQL statement. If the statement has been executed as a prepared
     * statement, this method returns the SQL without parameter bindings. Otherwise, the result is
     * the same as calling the {@code getBoundSQLStatement()} function.
     *
     * @return SQL statement without parameter bindings
     */
    String getSQLStatement();

    /**
     * Returns the SQL statement with parameter bindings.
     *
     * @return an {@link Optional} with the SQL statement <b>with</b> parameter bindings as value. Empty {@link Optional} if not present.
     */
    Optional<String> getBoundSQLStatement();

    /**
     * Returns the SQL statement without parameter bindings.
     *
     * @return an {@link Optional} with the SQL statement <b>without</b> parameter bindings as value. Empty {@link Optional} if not present.
     */
    Optional<String> getUnboundSQLStatement();

    /**
     * If the corresponding statement is a prepared statement, this method returns a map of
     * corresponding SQL parameter bindings. Otherwise, this method returns an empty list. The Key
     * represents the parameter index. (first parameter has an index of 1)
     *
     * @return an {@link Optional} with an <b>unmodifiable map</b> of bound SQL parameter values as value. Empty {@link Optional} if not present.
     */
    Optional<Map<Integer, String>> getParameterBindings();

    /**
     * Returns the name of the database product.
     *
     * @return an {@link Optional} with the name of the associated database product as value. Empty {@link Optional} if not present.
     */
    Optional<String> getDBProductName();

    /**
     * Returns the version of the database product.
     *
     * @return an {@link Optional} with the version of the associated database product version as value. Empty {@link Optional} if not present.
     */
    Optional<String> getDBProductVersion();

    /**
     * Returns the database connection URL.
     *
     * @return an {@link Optional} with the URL of the database connection as value. Empty {@link Optional} if not present.
     */
    Optional<String> getDBUrl();

}
