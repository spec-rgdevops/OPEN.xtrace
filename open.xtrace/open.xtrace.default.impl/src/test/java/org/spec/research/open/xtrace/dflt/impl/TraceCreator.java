package org.spec.research.open.xtrace.dflt.impl;

import org.spec.research.open.xtrace.dflt.impl.core.LocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.HTTPRequestProcessingImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.RemoteInvocationImpl;

/**
 * Creates a trace instance for testing.
 * 
 * @author Alexander Wert
 *
 */
public class TraceCreator {

    /**
     * method name pattern.
     */
    protected static final String METHOD_PREFIX = "MyMethod";

    /**
     * common depth.
     */
    protected static final int DEPTH = 2;

    /**
     * number of child nodes for each inner node.
     */
    protected static final int WIDTH = 3;

    /**
     * node index for which a SubTrace invocation shell be created.
     */
    protected static final int IDX_ON_SUBTRACE_INVOCATION = 7;

    /**
     * size of SubTrace.
     */
    protected static final int SIZE;
    static {
        int sum = 0;
        for (int i = 0; i <= DEPTH; i++) {
            sum += Math.pow(WIDTH, i);
        }
        SIZE = 2 * sum;
    }

    /**
     * node index indicating the end of the invoked SubTrace.
     */
    protected static final int IDX_ON_SUBTRACE_INVOCATION_END = IDX_ON_SUBTRACE_INVOCATION + (SIZE / 2);

    /**
     * method counter for different method names.
     */
    private int methodCounter = 0;

    /**
     * SubTrace ID of the invoked SubTrace.
     */
    protected static final int INVOKED_SUB_TRACE_ID = 2;

    /**
     * SubTrace ID of the root SubTrace.
     */
    protected static final int ROOT_SUB_TRACE_ID = 1;

    /**
     * Initialize SubTrace.
     * 
     * @return trace instance
     */
    public TraceImpl createTrace() {

        TraceImpl trace = new TraceImpl(1);

        SubTraceImpl subTrace = new SubTraceImpl(1, null, trace);
        subTrace.setLocation(new LocationImpl());
        trace.setRoot(subTrace);

        AbstractCallableImpl rootCallable = createChildNode(null, subTrace, trace, 0);
        subTrace.setRoot(rootCallable);
        return trace;
    }

    /**
     * Recursively creates a SubTrace structure.
     * 
     * @param parent
     *            parent Callable
     * @param subTrace
     *            SubTrace container
     * @param trace
     *            containing trace
     * @param depth
     *            current depth
     * @return created Callable
     */
    private AbstractCallableImpl createChildNode(AbstractNestingCallableImpl parent, SubTraceImpl subTrace,
            TraceImpl trace, int depth) {

        if (depth > DEPTH) {
            return null;
        }

        methodCounter++;
        if (methodCounter == 1) {
            HTTPRequestProcessingImpl httpCall = new HTTPRequestProcessingImpl(null, subTrace);
            for (int i = 0; i < WIDTH; i++) {
                createChildNode(httpCall, subTrace, trace, depth + 1);
            }
            return httpCall;
        } else if (methodCounter == IDX_ON_SUBTRACE_INVOCATION) {

            RemoteInvocationImpl remoteInvocation = new RemoteInvocationImpl(parent, subTrace);

            SubTraceImpl newSubTrace = new SubTraceImpl(INVOKED_SUB_TRACE_ID, subTrace, trace);
            newSubTrace.setLocation(new LocationImpl());
            AbstractCallableImpl newRootCallable = createChildNode(null, newSubTrace, trace, 0);
            newSubTrace.setRoot(newRootCallable);

            remoteInvocation.setTargetSubTrace(newSubTrace);
            return remoteInvocation;
        } else {
            MethodInvocationImpl methodInvocation = new MethodInvocationImpl(parent, subTrace);

            methodInvocation.setSignature("package.MyClass.SubTrace Invocation");

            methodInvocation.setPackageName("package");
            methodInvocation.setClassName("MyClass");
            methodInvocation.setMethodName("MyMethod" + methodCounter);

            for (int i = 0; i < WIDTH; i++) {
                createChildNode(methodInvocation, subTrace, trace, depth + 1);
            }
            return methodInvocation;
        }

    }
}
