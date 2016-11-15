package org.spec.research.open.xtrace.dflt.impl.callables;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spec.research.open.xtrace.dflt.impl.core.callables.DatabaseInvocationImpl;

/**
 * JUnit test for the {@link DatabaseInvocationImpl} class.
 * 
 * @author Manuel Palenga
 *
 */
public class TestDatabaseInvocationImpl {

    private DatabaseInvocationImpl databaseInvocationImpl = null;

    @Before
    public void setUp() {

        databaseInvocationImpl = new DatabaseInvocationImpl();
    }

    @Test
    /**
     * Tests queries with sql statement and bounded parameter.
     */
    public void testBoundSQLStatement() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table");
        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertFalse(databaseInvocationImpl.isPrepared().get());
        Assert.assertFalse(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertFalse(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table", databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table", databaseInvocationImpl.getBoundSQLStatement().get());

    }

    @Test
    /**
     * Tests queries with sql statement and bounded parameter.
     * With question mark as symbol.
     */
    public void testBoundSQLStatementWithQuestionMarkAsSymbol() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE question = 'Sure?'");
        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertFalse(databaseInvocationImpl.isPrepared().get());
        Assert.assertFalse(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertFalse(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?'", databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?'", databaseInvocationImpl
                .getBoundSQLStatement().get());
    }

    @Test
    /**
     * Tests queries with sql statement and unbounded parameter.
     */
    public void testUnboundSQLStatementWithoutParameterBinding() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE question = ?");
        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertTrue(databaseInvocationImpl.isPrepared().get());
        Assert.assertFalse(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertFalse(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE question = ?", databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE question = ?", databaseInvocationImpl.getUnboundSQLStatement()
                .get());
    }

    @Test
    /**
     * Tests queries with sql statement and unbounded parameter.
     * With question mark as symbol.
     */
    public void testUnboundSQLStatementWithoutParameterBindingWithQuestionMarkAsSymbol() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE question = 'Sure?' and id = ?");
        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertTrue(databaseInvocationImpl.isPrepared().get());
        Assert.assertFalse(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertFalse(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?' and id = ?",
                databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?' and id = ?", databaseInvocationImpl
                .getUnboundSQLStatement().get());
    }

    @Test
    /**
     * Tests queries with sql statement and unbounded parameter.
     */
    public void testUnboundSQLStatementWithParameterBinding() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE question = ?");
        databaseInvocationImpl.addParameterBinding(1, "Sure?");

        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertTrue(databaseInvocationImpl.isPrepared().get());
        Assert.assertTrue(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE question = ?", databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?'", databaseInvocationImpl
                .getBoundSQLStatement()
                .get());
        Assert.assertEquals("SELECT * FROM table WHERE question = ?", databaseInvocationImpl.getUnboundSQLStatement()
                .get());
    }

    @Test
    /**
     * Tests queries with sql statement and unbounded parameter.
     * Wtih question mark as symbol.
     */
    public void testUnboundSQLStatementWithParameterBindingAndQuestionMarkAsSymbol() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE question = 'Sure?' and id = ? and name = ? ");
        databaseInvocationImpl.addParameterBinding(1, "4242");
        databaseInvocationImpl.addParameterBinding(2, "test");

        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertTrue(databaseInvocationImpl.isPrepared().get());
        Assert.assertTrue(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?' and id = ? and name = ? ",
                databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?' and id = '4242' and name = 'test' ",
                databaseInvocationImpl
                        .getBoundSQLStatement().get());
        Assert.assertEquals("SELECT * FROM table WHERE question = 'Sure?' and id = ? and name = ? ",
                databaseInvocationImpl
                        .getUnboundSQLStatement().get());
    }

    @Test
    /**
     * Tests queries with sql statement and unbounded parameter.
     * With incorrect number of parameter binding variables.
     */
    public void testUnboundSQLStatementWithIncorrectNumberOfParameterBindingVariables() {

        databaseInvocationImpl.setSQLStatement("SELECT * FROM table WHERE id = ?");
        databaseInvocationImpl.addParameterBinding(1, "42");
        databaseInvocationImpl.addParameterBinding(2, "1337");

        Assert.assertTrue(databaseInvocationImpl.isPrepared().isPresent());
        Assert.assertTrue(databaseInvocationImpl.isPrepared().get());
        Assert.assertTrue(databaseInvocationImpl.getParameterBindings().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertTrue(databaseInvocationImpl.getUnboundSQLStatement().isPresent());

        Assert.assertEquals("SELECT * FROM table WHERE id = ?", databaseInvocationImpl.getSQLStatement());
        Assert.assertEquals("SELECT * FROM table WHERE id = '42'", databaseInvocationImpl
                .getBoundSQLStatement().get());
        Assert.assertEquals("SELECT * FROM table WHERE id = ?", databaseInvocationImpl
                .getUnboundSQLStatement().get());
    }

    /**
     * Tests is not present.
     */
    @Test
    public void testNotPresent() {

        DatabaseInvocationImpl invocationImpl = new DatabaseInvocationImpl();

        // Standard information
        Assert.assertEquals(null, invocationImpl.getSQLStatement());
        Assert.assertFalse(invocationImpl.getBoundSQLStatement().isPresent());
        Assert.assertFalse(invocationImpl.getUnboundSQLStatement().isPresent());
        Assert.assertFalse(invocationImpl.getParameterBindings().isPresent());
        Assert.assertFalse(invocationImpl.getDBProductName().isPresent());
        Assert.assertFalse(invocationImpl.getDBProductVersion().isPresent());
        Assert.assertFalse(invocationImpl.getDBUrl().isPresent());
        Assert.assertFalse(invocationImpl.isPrepared().isPresent());

        // Additional information
        Assert.assertFalse(invocationImpl.getAdditionalInformation().isPresent());
        Assert.assertFalse(invocationImpl.getIdentifier().isPresent());
        Assert.assertFalse(invocationImpl.getLabels().isPresent());
        Assert.assertFalse(invocationImpl.getThreadID().isPresent());
        Assert.assertFalse(invocationImpl.getThreadName().isPresent());
    }

}
