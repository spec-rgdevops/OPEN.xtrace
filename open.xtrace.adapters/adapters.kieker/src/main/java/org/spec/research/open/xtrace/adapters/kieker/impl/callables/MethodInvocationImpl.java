package org.spec.research.open.xtrace.adapters.kieker.impl.callables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kieker.tools.traceAnalysis.systemModel.Execution;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.callables.MethodInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;

/**
 * @author Okanovic
 *
 */
public class MethodInvocationImpl extends AbstractNestingCallableImpl implements MethodInvocation {
	private static final long serialVersionUID = -8621678119497714613L;

	private static final String PACKAGE_DELIMITER = ".";

	private static final String CONSTRUCTOR_PATTERN = "<init>";

	private String methodName;
	private String packageName;
	private String className;
	private List<String> parameterTypes;
	private Map<Integer, String> parameterValues;
	private String returnType;

	private long exclusiveCPUTime;
	private long cpuTime;

	protected MethodInvocationImpl(AbstractNestingCallableImpl parent, long entryTime, List<Integer> labelIds, List<AdditionalInformation> additionInfos,
			SubTrace containingSubTrace, long responseTime, long exclusiveTime) {
		super(parent, entryTime, labelIds, additionInfos, containingSubTrace, responseTime, exclusiveTime);
	}

	// see about exclusive time
	public MethodInvocationImpl(Execution receivingExecution, AbstractNestingCallableImpl parent, List<Integer> labelIds,
			List<AdditionalInformation> additionInfos, SubTrace containingSubTrace) {
		super(parent, receivingExecution.getTin(), labelIds, additionInfos, containingSubTrace, receivingExecution.getTout() - receivingExecution.getTin(), -1);
		this.entryTime = receivingExecution.getTin();
		this.responseTime = receivingExecution.getTout() - this.entryTime;
		// TODO fix
		this.cpuTime = -1;

		this.className = receivingExecution.getOperation().getComponentType().getTypeName();
		this.methodName = receivingExecution.getOperation().getSignature().getName();
		this.packageName = receivingExecution.getOperation().getComponentType().getPackageName();
		if (this.parameterTypes == null) {
			this.parameterTypes = new ArrayList<String>();
		}
		this.parameterTypes.addAll(Arrays.asList(receivingExecution.getOperation().getSignature().getParamTypeList()));
		this.parameterValues = Collections.emptyMap();
		this.returnType = receivingExecution.getOperation().getSignature().getReturnType();
	}

	@Override
	public Optional<Long> getCPUTime() {
		return Optional.ofNullable(cpuTime);
	}

	@Override
	public Optional<String> getClassName() {
		return Optional.ofNullable(className);
	}

	@Override
	public Optional<Long> getExclusiveCPUTime() {
		if (exclusiveCPUTime < 0) {
			exclusiveCPUTime = cpuTime;

			for (MethodInvocation child : getCallees(MethodInvocation.class)) {
				exclusiveCPUTime -= child.getCPUTime().orElse((long) 0);
			}
		}

		return Optional.ofNullable(exclusiveCPUTime);
	}

	@Override
	public Optional<String> getMethodName() {
		return Optional.ofNullable(methodName);
	}

	@Override
	public Optional<String> getPackageName() {
		return Optional.ofNullable(packageName);
	}

	@Override
	public Optional<List<String>> getParameterTypes() {
		return Optional.ofNullable(parameterTypes);
	}

	@Override
	public Optional<Map<Integer, String>> getParameterValues() {
		return Optional.ofNullable(parameterValues);
	}

	@Override
	public Optional<String> getReturnType() {
		return Optional.ofNullable(returnType);
	}

	@Override
	public String getSignature() {
		return getPackageName() + PACKAGE_DELIMITER + getClassName() + PACKAGE_DELIMITER + getMethodName() + "("
				+ parameterTypes.stream().collect(Collectors.joining(",")) + ")";
	}

	@Override
	public Optional<Boolean> isConstructor() {
		return methodName != null ? Optional.of(methodName.equalsIgnoreCase(CONSTRUCTOR_PATTERN)) : Optional.empty();
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	@Override
	public Optional<Long> getWaitTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getExclusiveWaitTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getGCTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getExclusiveGCTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getSyncTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Long> getExclusiveSyncTime() {
		return Optional.empty();
	}

	@Override
	public Optional<Object> getReturnValue() {
		return Optional.empty();
	}
}