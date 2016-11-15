package org.spec.research.open.xtrace.api.core.callables;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an invocation of a method in a trace / sub-trace..
 *
 * @author Alexander Wert, Christoph Heger, Manuel Palenga, Alper Hidiroglu
 */
public interface MethodInvocation extends NestingCallable {

    /**
     * Return the time the method execution spent on CPU including the time of called methods.
     *
     * @return an {@link Optional} with CPU time (including the CPU time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getCPUTime();

    /**
     * Return the time the method execution spent on CPU without the time of called methods.
     *
     * @return an {@link Optional} with exclusive CPU time (excluding the CPU time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getExclusiveCPUTime();
    
    /**
     * Return the time the method execution spent on Wait including the time of called methods.
     *
     * @return an {@link Optional} with Wait time (including the Wait time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getWaitTime();
    
    /**
     * Return the time the method execution spent on Wait without the time of called methods.
     *
     * @return an {@link Optional} with exclusive Wait time (excluding the Wait time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getExclusiveWaitTime();
    
    /**
     * Return the time the method execution spent on GC including the time of called methods.
     *
     * @return an {@link Optional} with GC time (including the GC time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getGCTime();
    
    /**
     * Return the time the method execution spent on GC without the time of called methods.
     *
     * @return an {@link Optional} with exclusive GC time (excluding the GC time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getExclusiveGCTime();
    
    /**
     * Return the time the method execution spent on Sync including the time of called methods.
     *
     * @return an {@link Optional} with CPU time (including the Sync time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getSyncTime();
    
    /**
     * Return the time the method execution spent on Sync without the time of called methods.
     *
     * @return an {@link Optional} with exclusive CPU time (excluding the Sync time of nested callees) in nanoseconds as value. Empty {@link Optional} if not present.
     */
    Optional<Long> getExclusiveSyncTime();

    /**
     * Returns the full qualified signature of the corresponding operation (including all
     * full-qualified types of the parameters).
     * <p>
     * Example: org.my.MyClass.myMethod(org.my.Param1,org.my.Param2)
     *
     * @return full qualified signature
     */
    String getSignature();

    /**
     * Returns the simple method name of the corresponding operation.
     * <p>
     * Example:
     * <p>
     * For operation "org.my.MyClass.myMethod(org.my.Parameter)" this method would return "myMethod"
     *
     * @return an {@link Optional} with the method name as value. Empty {@link Optional} if not set.
     */
    Optional<String> getMethodName();

    /**
     * Returns the simple class name of the corresponding operation.
     * <p>
     * Example:
     * <p>
     * For operation "org.my.MyClass.myMethod(org.my.Parameter)" this method would return "MyClass"
     *
     * @return an {@link Optional} with the class name as value. Empty {@link Optional} if not set
     */
    Optional<String> getClassName();

    /**
     * Returns the package name of the corresponding operation.
     * <p>
     * Example:
     * <p>
     * For operation "org.my.MyClass.myMethod(org.my.Parameter)" this method would return "org.my"
     *
     * @return an {@link Optional} with the package name as value. Empty {@link Optional} if not set.
     */
    Optional<String> getPackageName();

    /**
     * Returns the parameter type of the corresponding operation.
     *
     * @return an {@link Optional} with an <b>unmodifiable list</b> of full qualified class names for the parameter names of the
     * corresponding operation as value. Empty {@link Optional} if not present.
     */
    Optional<List<String>> getParameterTypes();

    /**
     * Returns a Map of parameter values. The key is the index of the corresponding parameter in the
     * method signature (first parameter has an index of 1). The value is the String representation of the parameter value.
     *
     * @return an {@link Optional} with an <b>unmodifiable map</b> of parameter values as value. Empty {@link Optional} if not present.
     */
    Optional<Map<Integer, String>> getParameterValues();

    /**
     * Returns the return type of the corresponding operation.
     *
     * @return an {@link Optional} with the full qualified class name of the return type as value. Empty {@link Optional} if return type is unknown.
     */
    Optional<String> getReturnType();
    
    /**
     * Returns the return value of the corresponding operation.
     *
     * @return an {@link Optional} with the object of the return value. Empty {@link Optional} if return value is unknown.
     */
    Optional<Object> getReturnValue();

    /**
     * Returns <code>true</code> when this method is a constructor.
     *
     * @return an {@link Optional} with <code>true</code> as value if this {@link Callable} is a constructor, otherwise
     * <code>false</code>. Empty {@link Optional} if not present.
     */
    Optional<Boolean> isConstructor();
}
