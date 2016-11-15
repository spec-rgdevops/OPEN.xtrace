package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.DatabaseInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

/**
 * Default implementation of the {@link DatabaseInvocation} API element.
 * 
 * @author Alexander Wert, Christoph Heger, Manuel Palenga, Alper Hidiroglu
 *
 */
public class DatabaseInvocationImpl extends AbstractTimedCallableImpl implements DatabaseInvocation, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3452520298014174828L;

    /**
     * Indicates whether this represents prepared SQL statement.
     */
    private boolean prepared = false;

    /**
     * SQL statement.
     */
    private String sql;

    /**
     * Parameter-bound SQL statement.
     */
    private transient Optional<String> boundSQL = Optional.empty();

    /**
     * DBMS product name.
     */
    private Optional<String> dbProductName = Optional.empty();

    /**
     * DBMS product version.
     */
    private Optional<String> dbProductVersion = Optional.empty();

    /**
     * DB URL.
     */
    private Optional<String> dbUrl = Optional.empty();

    /**
     * parameter bindings.
     */
    private Optional<Map<Integer, String>> parameterBindings = Optional.empty();

    /**
     * Default constructor for serialization. This constructor should not be used except for deserialization.
     */
    public DatabaseInvocationImpl() {

    }

    /**
     * Constructor. Adds the newly created {@link Callable} instance to the passed parent if the parent is not null!
     * 
     * @param parent
     *            {@link AbstractNestingCallableImpl} that called this Callable
     * @param containingSubTrace
     *            the SubTrace containing this Callable
     */
    public DatabaseInvocationImpl(AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {

        super(parent, containingSubTrace);
    }

    @Override
    public Optional<Boolean> isPrepared() {

        if (sql == null) {
            return Optional.empty();
        }

        if (!sql.contains("?")) {
            prepared = false;
        } else if (!sql.contains("'")) {
            prepared = true;
        } else {
            prepared = !this.getPositionOfAllPlaceholders(sql).isEmpty();
        }

        return Optional.ofNullable(prepared);
    }

    /**
     * 
     * Search a symbol in a text area.
     * 
     * @param text
     *            Text for searching
     * @param lowerBound
     *            Start searching here
     * @param upperBound
     *            Stop searching here
     * @param symbol
     *            Search symbol
     * @return Number of symbols in the text area.
     */
    private int countSymbols(final String text, final int lowerBound, final int upperBound, final char symbol) {

        int count = 0;
        for (int i = lowerBound; i < upperBound && i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == symbol) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param prepared
     *            the prepared to set
     */
    public void setPrepared(boolean prepared) {

        this.prepared = prepared;
    }

    @Override
    public String getSQLStatement() {

        return sql;
    }

    public void setSQLStatement(String sql) {

        this.sql = sql;
    }

    @Override
    public Optional<String> getBoundSQLStatement() {

        if (!isPrepared().orElse(false)) {
            return Optional.ofNullable(sql);
        } else if (!boundSQL.isPresent() && parameterBindings.isPresent()) {

            List<Integer> listPositionsOfPlaceholders = this.getPositionOfAllPlaceholders(sql);

            String tmpBoundSQL = sql;
            int count = 1;
            int shift = 0;

            for (Integer position : listPositionsOfPlaceholders) {
                if (!parameterBindings.get().containsKey(count)) {
                    throw new IllegalStateException("Invalid amount of parameter bindings for SQL statement.");
                }
                String value = parameterBindings.get().get(count);

                int lowerBound = position + shift;
                int upperBound = lowerBound + 1;
                shift += value.length() + 1;

                tmpBoundSQL = tmpBoundSQL.substring(0, lowerBound) + "'" + value + "'"
                        + tmpBoundSQL.substring(upperBound);
                count++;
            }

            boundSQL = Optional.of(tmpBoundSQL);
        }
        return boundSQL;
    }

    /**
     * Returns a list with positions of question marks in the provided sql statement.
     * 
     * @param sql
     *            SQL Statement for searching
     * @return List with positions
     */
    private List<Integer> getPositionOfAllPlaceholders(final String sql) {

        String tempSQL = sql;
        List<Integer> listPlaceholders = new LinkedList<Integer>();

        // Test whether ? is a variable or a symbol?
        while (tempSQL.contains("?")) {

            int positionQuestionmark = tempSQL.indexOf("?");
            int countLower = this.countSymbols(tempSQL, 0, positionQuestionmark, '\'');
            int countUpper = this.countSymbols(tempSQL, positionQuestionmark, tempSQL.length(), '\'');

            if ((countLower % 2 == 0 && countUpper % 2 == 0) || countLower == 0 || countUpper == 0) {
                listPlaceholders.add(positionQuestionmark);
            }
            tempSQL = tempSQL.replaceFirst("\\?", "#");

        }

        return listPlaceholders;
    }

    @Override
    public Optional<Map<Integer, String>> getParameterBindings() {

        return parameterBindings;
    }

    /**
     * Adds a parameter binding.
     * 
     * @param parameterIndex
     *            index of the parameter (first parameter has an index of 1)
     * @param value
     *            parameter value
     */
    public void addParameterBinding(int parameterIndex, String value) {

        if (!parameterBindings.isPresent()) {
            parameterBindings = Optional.of(new HashMap<Integer, String>());
        }
        parameterBindings.get().put(parameterIndex, value);
    }

    @Override
    public Optional<String> getDBProductName() {

        return dbProductName;
    }

    @Override
    public Optional<String> getDBProductVersion() {

        return dbProductVersion;
    }

    @Override
    public Optional<String> getDBUrl() {

        return dbUrl;
    }

    /**
     * Sets database product name.
     * 
     * @param productName
     *            name of the product
     */
    public void setDBProductName(Optional<String> productName) {

        dbProductName = productName;
    }

    /**
     * Sets database product version.
     * 
     * @param productVersion
     *            version of the product
     */
    public void setDBProductVersion(Optional<String> productVersion) {

        dbProductVersion = productVersion;
    }

    /**
     * Sets database URL.
     * 
     * @param url
     *            connection URL
     */
    public void setDBUrl(Optional<String> url) {

        dbUrl = url;
    }

    @Override
    public String toString() {

        return StringUtils.getStringRepresentation(this);
    }

    @Override
    public Optional<String> getUnboundSQLStatement() {

        if (this.isPrepared().orElse(false)) {
            return Optional.ofNullable(sql);
        }
        return Optional.empty();
    }

}
