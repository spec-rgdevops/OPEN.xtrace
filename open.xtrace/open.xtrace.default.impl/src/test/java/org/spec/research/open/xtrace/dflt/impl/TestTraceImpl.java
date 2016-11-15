package org.spec.research.open.xtrace.dflt.impl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.HTTPRequestProcessing;
import org.spec.research.open.xtrace.api.core.callables.MethodInvocation;
import org.spec.research.open.xtrace.api.core.callables.NestingCallable;
import org.spec.research.open.xtrace.api.core.callables.RemoteInvocation;
import org.spec.research.open.xtrace.api.utils.CallableIterator;
import org.spec.research.open.xtrace.api.utils.CallableIteratorOnTrace;
import org.spec.research.open.xtrace.api.utils.SubTraceIterator;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;

/**
 * JUnit test for the {@link TraceImpl} class and corresponding iterators {@link CallableIteratorOnTrace} and
 * {@link SubTraceIterator}.
 * 
 * @author Alexander Wert
 *
 */
public class TestTraceImpl {

    /**
     * SubTrace to test.
     */
    private static Trace mainTrace;

    /**
     * Initialize SubTrace.
     */
    @BeforeClass
    public static void createSubTrace() {

        mainTrace = new TraceCreator().createTrace();
    }

    /**
     * Tests the structure of a SubTrace and corresponding iterator {@link CallableIterator}.
     */
    // @Test
    public void testTreeStructure() {

        mainTrace.toString();
        mainTrace.getRoot().toString();
        Assert.assertEquals(TraceCreator.SIZE, mainTrace.size());
        Assert.assertEquals(TraceCreator.SIZE / 2 - 1,
                ((NestingCallable) mainTrace.getRoot().getRoot()).getChildCount());
        int i = 1;
        for (Callable clbl : mainTrace) {
            if (i <= TraceCreator.IDX_ON_SUBTRACE_INVOCATION || i > TraceCreator.IDX_ON_SUBTRACE_INVOCATION_END) {
                Assert.assertEquals(TraceCreator.ROOT_SUB_TRACE_ID, clbl.getContainingSubTrace().getSubTraceId());
            } else {
                Assert.assertEquals(TraceCreator.INVOKED_SUB_TRACE_ID, clbl.getContainingSubTrace().getSubTraceId());
            }
            if (i == TraceCreator.IDX_ON_SUBTRACE_INVOCATION) {
                Assert.assertTrue(clbl instanceof RemoteInvocation);
                Assert.assertEquals(TraceCreator.INVOKED_SUB_TRACE_ID, ((RemoteInvocation) clbl).getTargetSubTrace()
                        .get().getSubTraceId());
            }
            if (i == 1) {
                Assert.assertTrue(clbl instanceof HTTPRequestProcessing);
            } else {
                Assert.assertEquals(TraceCreator.METHOD_PREFIX + i, ((MethodInvocation) clbl).getMethodName().get());
            }

            i++;
        }
    }

}
