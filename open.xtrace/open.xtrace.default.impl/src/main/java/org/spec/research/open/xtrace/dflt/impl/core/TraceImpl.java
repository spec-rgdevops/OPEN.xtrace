package org.spec.research.open.xtrace.dflt.impl.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.spec.research.open.xtrace.api.core.SubTrace;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.TreeIterator;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.utils.CallableIteratorOnTrace;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.api.utils.SubTraceIterator;

/**
 * Default implementation of the {@link Trace} interface of the 'OPEN.xtrace'.
 * 
 * @author Alexander Wert
 *
 */
public class TraceImpl extends AbstractIdentifiableImpl implements Trace, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7936796356771531963L;

	/**
	 * Root of the sub trace composite structure.
	 */
	private SubTraceImpl rootSubTrace;

	/**
	 * Identifier of the Trace.
	 */
	private long traceId;

	/**
	 * Registry of Signatures used in this trace.
	 */
	// private Map<Integer, Signature> containedSignatures;

	/**
	 * Registry of String constants used in this trace instance.
	 */
	private Map<Integer, String> stringConstantsRegistry;

	/**
	 * size of this SubTrace.
	 */
	private transient int size = -1;

	/**
	 * Indicates whether CPU times are supported in this trace.
	 */
	// private boolean cpuTimesSupported;

	/**
	 * Default constructor.
	 */
	public TraceImpl() {
	}

	/**
	 * Constructor.
	 * 
	 * @param traceId
	 *            identifier of this Trace
	 */
	public TraceImpl(long traceId) {
		this.traceId = traceId;
	}

	@Override
	public TreeIterator<Callable> iterator() {
		return new CallableIteratorOnTrace(getRoot());
	}

	@Override
	public SubTrace getRoot() {
		return rootSubTrace;
	}

	/**
	 * Sets the root SubTrace.
	 * 
	 * @param root
	 *            root SubTrace
	 */
	public void setRoot(SubTraceImpl root) {
		this.rootSubTrace = root;
	}

	@Override
	public TreeIterator<SubTrace> subTraceIterator() {
		return new SubTraceIterator(getRoot());
	}

	@Override
	public long getTraceId() {
		return traceId;
	}

	@Override
	public int size() {
		if (size < 0) {
			int count = 0;
			Iterator<SubTrace> iterator = this.subTraceIterator();
			while (iterator.hasNext()) {
				SubTrace sTrace = iterator.next();
				count += sTrace.size();
			}
			size = count;
		}

		return size;
	}

	/**
	 * Retrieves the {@link String} for the given signature ID.
	 * 
	 * @param signatureId
	 *            id for which to retrieve the signature.
	 * @return Signature object for the passed id
	 */
	// public String getSignature(int signatureId) {
	// if (containedSignatures == null) {
	// return null;
	// }
	// return containedSignatures.get(signatureId);
	// }

	/**
	 * Registers a new {@link String} if it is not contained in the repository,
	 * yet.
	 * 
	 * @param returnType
	 *            return type
	 * @param packageName
	 *            package name
	 * @param className
	 *            class name
	 * @param methodName
	 *            method name
	 * @param parameterTypes
	 *            list of parameter types
	 * @return id of the registered signature
	 */
	// public int registerSignature(String returnType, String packageName,
	// String className, String methodName, List<String> parameterTypes) {
	// int methodNameId = registerStringConstant(methodName);
	// int packageNameId = registerStringConstant(packageName);
	// int classNameId = registerStringConstant(className);
	// int returnTypeId = registerStringConstant(returnType);
	// List<Integer> pTypeIds = null;
	// if (parameterTypes != null) {
	// pTypeIds = new ArrayList<Integer>();
	// for (String pType : parameterTypes) {
	// pTypeIds.add(registerStringConstant(pType));
	// }
	// } else {
	// pTypeIds = Collections.emptyList();
	// }
	//
	// Signature signature = new Signature(this, methodNameId, packageNameId,
	// classNameId, pTypeIds, returnTypeId);
	//
	// if (containedSignatures == null) {
	// containedSignatures = new HashMap<Integer, Signature>();
	// }
	// int hash = signature.hashCode();
	// if (!containedSignatures.containsKey(hash)) {
	// containedSignatures.put(hash, signature);
	//
	// }
	//
	// return hash;
	// }

	/**
	 * Retrieves the String constant for the passed ID.
	 * 
	 * @param stringConstantId
	 *            id for which to retrieve the String constant.
	 * @return String constant for the passed id
	 */
	public String getStringConstant(int stringConstantId) {
		if (stringConstantsRegistry == null) {
			return null;
		}
		return stringConstantsRegistry.get(stringConstantId);
	}

	/**
	 * Registers a new String constant if it is not contained in the registry,
	 * yet.
	 * 
	 * @param stringConstant
	 *            stringConstant to register
	 * @return identifier of the registered String constant
	 */
	public int registerStringConstant(String stringConstant) {
		if (stringConstantsRegistry == null) {
			stringConstantsRegistry = new HashMap<Integer, String>();
		}
		int hash = stringConstant == null ? 0 : stringConstant.hashCode();
		if (!stringConstantsRegistry.containsKey(hash)) {
			stringConstantsRegistry.put(hash, stringConstant);
		}
		return hash;
	}

	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

	// public void setCPUTimesSupported(boolean supported) {
	// cpuTimesSupported = supported;
	// }

	@Override
	public long getExclusiveTime() {
		return getResponseTime();
	}

	@Override
	public long getResponseTime() {
		if (rootSubTrace == null) {
			return 0;
		} else {
			return rootSubTrace.getResponseTime();
		}
	}

}
