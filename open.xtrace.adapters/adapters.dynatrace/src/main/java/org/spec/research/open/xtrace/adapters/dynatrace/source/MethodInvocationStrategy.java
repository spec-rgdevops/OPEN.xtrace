package org.spec.research.open.xtrace.adapters.dynatrace.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.spec.research.open.xtrace.adapters.dynatrace.enums.AttachmentType;
import org.spec.research.open.xtrace.adapters.dynatrace.enums.PropertyKey;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Attachment;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Node;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;

/**
 * 
 * Creates MethodInvocation with all available measurements from the provided
 * trace.
 * 
 * @author Manuel Palenga
 * @since 21.08.2016
 */
class MethodInvocationStrategy {

	public MethodInvocationImpl initMethodInvocation(final Node node, final AbstractNestingCallableImpl parent, final SubTraceImpl containingSubTrace) {

		MethodInvocationImpl methodInvoc = new MethodInvocationImpl(parent, containingSubTrace);

		setMethodNameAndParameterTypes(node, methodInvoc);
		setPackageAndClassName(node, methodInvoc);

		this.setSignature(methodInvoc);

		methodInvoc.setCPUTime(Optional.of(Utils.extractTime(node.getBreakdown(), "CPU")));
		methodInvoc.setWaitTime(Optional.of(Utils.extractTime(node.getBreakdown(), "Wait")));
		methodInvoc.setSyncTime(Optional.of(Utils.extractTime(node.getBreakdown(), "Sync")));

		// Set Garbage Collection Time
		Attachment attachment = Utils.getAttachment(node, AttachmentType.RuntimeSuspensionNodeAttachment);
		Optional<String> reason = Utils.getValueOfAttachmentKey(attachment, PropertyKey.REASON);

		if (reason.isPresent() && reason.get().equals(PurepathTransformer.GARBAGE_COLLECTION_REASON)) {
			Optional<String> optGCTime = Utils.getValueOfAttachmentKey(attachment, PropertyKey.DURATION);
			if (optGCTime.isPresent()) {
				try {
					String strGCTime = optGCTime.get().replaceAll(",", "");
					long gcTime = (long) (Double.parseDouble(strGCTime) * PurepathTransformer.MILLIS_TO_NANOS_FACTOR);
					methodInvoc.setGCTime(Optional.of(gcTime));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}

		return methodInvoc;
	}

	private void setPackageAndClassName(final Node node, final MethodInvocationImpl methodInvoc) {

		// Split package and class name
		String packageClass = node.getClazz();
		int posLastPoint = packageClass.lastIndexOf('.');
		String className = packageClass.substring(posLastPoint + 1);
		String packageName = packageClass.substring(0, posLastPoint);

		methodInvoc.setPackageName(packageName);
		methodInvoc.setClassName(className);
	}

	private void setMethodNameAndParameterTypes(final Node node, final MethodInvocationImpl methodInvoc) {

		// Split method name and parameter types
		String methodSignature = node.getMethod();

		int posFirstBracket = methodSignature.indexOf("(");
		String method = methodSignature.substring(0, posFirstBracket);

		// Get parameter types
		String parameterList = methodSignature.substring(posFirstBracket);
		parameterList = parameterList.replace("(", "").replace(")", "");
		String[] parameters = parameterList.split(",");
		for (int i = 0; i < parameters.length; i++) {
			String parameter = parameters[i].trim();
			if (parameter.contains(" ")) {
				int posFirstSpace = parameter.indexOf(" ");
				parameter = parameter.substring(0, posFirstSpace).trim();
			}
			parameters[i] = parameter;
		}
		List<String> listParameters = new ArrayList<String>(Arrays.asList(parameters));

		methodInvoc.setMethodName(method);
		methodInvoc.setParameterTypes(listParameters);
	}

	private void setSignature(final MethodInvocationImpl invocationImpl) {

		if (invocationImpl.getClassName().isPresent() && invocationImpl.getPackageName().isPresent() && invocationImpl.getMethodName().isPresent()) {
			String signature = String.format("%s.%s.%s(", invocationImpl.getPackageName().get(), invocationImpl.getClassName().get(), invocationImpl
					.getMethodName().get());

			String signatureParameters = "";
			if (invocationImpl.getParameterTypes() != null && invocationImpl.getParameterTypes().isPresent()
					&& !invocationImpl.getParameterTypes().get().isEmpty()) {
				for (String parameter : invocationImpl.getParameterTypes().get()) {
					signatureParameters += parameter + ", ";
				}

				// Remove last comma
				signatureParameters = signatureParameters.substring(0, signatureParameters.length() - 2);
			}

			signature += signatureParameters + ")";

			invocationImpl.setSignature(signature);
		}
	}
}
