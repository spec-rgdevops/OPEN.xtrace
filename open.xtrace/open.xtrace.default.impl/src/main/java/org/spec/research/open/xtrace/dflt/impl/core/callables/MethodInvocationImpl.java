package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.MethodInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;

/**
 * Default implementation of the {@link MethodInvocation} API element.
 * 
 * @author Alexander Wert, Manuel Palenga, Alper Hidiroglu
 *
 */
public class MethodInvocationImpl extends AbstractNestingCallableImpl implements
        MethodInvocation, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4548591658615282164L;

    /**
     * Constructor name pattern.
     */
    private static final String CONSTRUCTOR_PATTERN = "<init>";

    /**
     * Signature identifier to retrieve the signature from registry.
     */
    private Optional<Integer> signatureId = Optional.empty();

    /**
     * Method identifier to retrieve the signature from registry.
     */
    private Optional<Integer> methodId = Optional.empty();

    /**
     * Class identifier to retrieve the signature from registry.
     */
    private Optional<Integer> classId = Optional.empty();

    /**
     * Package identifier to retrieve the signature from registry.
     */
    private Optional<Integer> packageId = Optional.empty();

    /**
     * Return type identifier to retrieve the signature from registry.
     */
    private Optional<Integer> returnTypeId = Optional.empty();

    /**
     * Parameter types identifiers to retrieve the signature from registry.
     */
    private Optional<List<Integer>> parameterTypeIds = Optional.empty();

    /**
     * CPU time consumed by this {@link MethodInvocation} instance.
     */
    private Optional<Long> cpuTime = Optional.empty();

    /**
     * Exclusive CPU time [nanoseconds].
     */
    private transient Optional<Long> exclusiveCPUTime = Optional.empty();

    /**
     * Holds the parameter values, if any available. Key is the index of the corresponding parameter in the method
     * signature. Value is the string representaiton of the parameter value.
     */
    private Optional<Map<Integer, String>> parameterValues = Optional.empty();

    /**
     * Wait time.
     */
    private Optional<Long> waitTime = Optional.empty();

    /**
     * Sync time.
     */
    private Optional<Long> syncTime = Optional.empty();

    /**
     * Garbage Collector time.
     */
    private Optional<Long> gcTime = Optional.empty();

    /**
     * Return value of the method.
     */
    private Optional<Object> returnValue = Optional.empty();

    /**
     * Exclusive wait time.
     */
    private Optional<Long> exclusiveWaitTime = Optional.empty();

    /**
     * Exclusive Sync time.
     */
    private Optional<Long> exclusiveSyncTime = Optional.empty();

    /**
     * Exclusive Garbage Collector time.
     */
    private Optional<Long> exclusiveGcTime = Optional.empty();

    /**
     * Default constructor for serialization. This constructor should not be used except for deserialization.
     */
    public MethodInvocationImpl() {

    }

    /**
     * Constructor. Adds the newly created {@link Callable} instance to the passed parent if the parent is not null!
     * 
     * @param parent
     *            {@link AbstractNestingCallableImpl} that called this Callable
     * @param containingSubTrace
     *            the SubTrace containing this Callable
     */
    public MethodInvocationImpl(AbstractNestingCallableImpl parent,
            SubTraceImpl containingSubTrace) {

        super(parent, containingSubTrace);
    }

    @Override
    public Optional<Long> getCPUTime() {

        return cpuTime;
    }

    /**
     * Sets the CPU time.
     * 
     * @param cpuTime
     *            CPU time in [nanoseconds]
     */
    public void setCPUTime(Optional<Long> cpuTime) {

        this.cpuTime = cpuTime;
    }

    @Override
    public Optional<Long> getExclusiveCPUTime() {

        if (!exclusiveCPUTime.isPresent() && cpuTime.isPresent()) {
            long tmpExclusiveCPUTime = cpuTime.get();

            for (MethodInvocation child : getCallees(MethodInvocation.class)) {
                tmpExclusiveCPUTime -= child.getCPUTime().orElse((long) 0);
            }

            exclusiveCPUTime = Optional.of(tmpExclusiveCPUTime);

        }
        return exclusiveCPUTime;
    }

    @Override
    public String getSignature() {

        return resolveStringId(signatureId).orElse(null);
    }

    /**
     * Register the string in the registry.
     * 
     * @param content
     *            Text for the registry
     * @return {@link Optional} for the given Text
     */
    private Optional<Integer> registerString(String content) {

        return Optional.of(((TraceImpl) getContainingSubTrace().getContainingTrace())
                .registerStringConstant(content));
    }

    /**
     * Resolves the string for the given id from registry.
     * 
     * @param id
     *            identifier of the string
     * @return {@link String} for the given identifier
     */
    private Optional<String> resolveStringId(Optional<Integer> id) {

        if (id.isPresent()) {
            return Optional.ofNullable(((TraceImpl) getContainingSubTrace().getContainingTrace())
                    .getStringConstant(id.get()));
        }

        return Optional.empty();
    }

    /**
     * Sets the signature of this Callable.
     * 
     * @param signature
     *            full signature
     */
    public void setSignature(String signature) {

        signatureId = this.registerString(signature);
    }

    /**
     * Sets the method name of the signature of this Callable.
     * 
     * @param name
     *            simple method name
     */
    public void setMethodName(String name) {

        methodId = this.registerString(name);
    }

    /**
     * Sets the class name of the signature of this Callable.
     * 
     * @param name
     *            simple class name
     */
    public void setClassName(String name) {

        classId = this.registerString(name);
    }

    /**
     * Sets the package name of the signature of this Callable.
     * 
     * @param name
     *            package name
     */
    public void setPackageName(String name) {

        packageId = this.registerString(name);
    }

    /**
     * Sets the return type of the signature of this Callable.
     * 
     * @param name
     *            return type
     */
    public void setReturnType(String name) {

        returnTypeId = this.registerString(name);
    }

    /**
     * Sets the list of parameter types of the signature of this Callable.
     * 
     * @param types
     *            list of parameter types
     */
    public void setParameterTypes(List<String> types) {

        parameterTypeIds = Optional.of(types
                .stream()
                .map(type -> ((TraceImpl) getContainingSubTrace()
                        .getContainingTrace()).registerStringConstant(type))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<String> getMethodName() {

        return resolveStringId(methodId);
    }

    @Override
    public Optional<String> getClassName() {

        return resolveStringId(classId);
    }

    @Override
    public Optional<String> getPackageName() {

        return resolveStringId(packageId);
    }

    @Override
    public Optional<List<String>> getParameterTypes() {

        if (!parameterTypeIds.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(parameterTypeIds.get().stream()
                .map(x -> resolveStringId(Optional.of(x)).get()).collect(Collectors.toList()));
    }

    @Override
    public Optional<Map<Integer, String>> getParameterValues() {

        return parameterValues;
    }

    /**
     * Adds a parameter value.
     * 
     * @param parameterIndex
     *            index of the corresponding parameter in the method signature (first parameter has an index of 1)
     * @param value
     *            String representation of the parameter value
     */
    public void addParameterValue(int parameterIndex, String value) {

        if (!parameterValues.isPresent()) {
            parameterValues = Optional.of(new HashMap<Integer, String>());
        }

        parameterValues.map(p -> p.put(parameterIndex, value));

    }

    @Override
    public Optional<String> getReturnType() {

        return resolveStringId(returnTypeId);
    }

    @Override
    public Optional<Boolean> isConstructor() {

        return getMethodName().map(
                name -> name.equalsIgnoreCase(CONSTRUCTOR_PATTERN));
    }

    @Override
    public String toString() {

        return StringUtils.getStringRepresentation(this);
    }

    @Override
    public Optional<Long> getWaitTime() {

        return this.waitTime;
    }

    @Override
    public Optional<Long> getExclusiveWaitTime() {

        if (!exclusiveWaitTime.isPresent() && waitTime.isPresent()) {
            long tmpExclusiveWaitTime = waitTime.get();

            for (MethodInvocation child : getCallees(MethodInvocation.class)) {
                tmpExclusiveWaitTime -= child.getWaitTime().orElse((long) 0);
            }

            exclusiveWaitTime = Optional.of(tmpExclusiveWaitTime);
        }
        return exclusiveWaitTime;
    }

    @Override
    public Optional<Long> getGCTime() {

        return this.gcTime;
    }

    @Override
    public Optional<Long> getExclusiveGCTime() {

        if (!exclusiveGcTime.isPresent() && gcTime.isPresent()) {
            long tmpExclusiveGcTime = gcTime.get();

            for (MethodInvocation child : getCallees(MethodInvocation.class)) {
                tmpExclusiveGcTime -= child.getGCTime().orElse((long) 0);
            }

            exclusiveGcTime = Optional.of(tmpExclusiveGcTime);
        }
        return exclusiveGcTime;
    }

    @Override
    public Optional<Long> getSyncTime() {

        return this.syncTime;
    }

    @Override
    public Optional<Long> getExclusiveSyncTime() {

        if (!exclusiveSyncTime.isPresent() && syncTime.isPresent()) {
            long tmpExclusiveSyncTime = syncTime.get();

            for (MethodInvocation child : getCallees(MethodInvocation.class)) {
                tmpExclusiveSyncTime -= child.getSyncTime().orElse((long) 0);
            }

            exclusiveSyncTime = Optional.of(tmpExclusiveSyncTime);
        }
        return exclusiveSyncTime;
    }

    @Override
    public Optional<Object> getReturnValue() {

        return this.returnValue;
    }

    /**
     * 
     * @param gcTime
     */
    public void setGCTime(Optional<Long> gcTime) {

        this.gcTime = gcTime;
    }

    /**
     * 
     * @param waitTime
     */
    public void setWaitTime(Optional<Long> waitTime) {

        this.waitTime = waitTime;
    }

    /**
     * 
     * @param syncTime
     */
    public void setSyncTime(Optional<Long> syncTime) {

        this.syncTime = syncTime;
    }

    /**
     * 
     * @param returnValue
     */
    public void setReturnValue(Optional<Object> returnValue) {

        this.returnValue = returnValue;
    }

}
