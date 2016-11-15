package org.spec.research.open.xtrace.dflt.impl.core;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.spec.research.open.xtrace.api.core.callables.Callable;

/**
 * The {@link String} encapsulates static information on a {@link Callable}.
 * 
 * @author Alexander Wert
 *
 */
public class Signature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6703267446354294098L;

	/**
	 * Delimiter of package names (".").
	 */
	private static final String PACKAGE_DELIMITER = ".";

	/**
	 * Constructor name pattern.
	 */
	private static final String CONSTRUCTOR_PATTERN = "<init>";

	/**
	 * Simple method name.
	 */
	private int methodNameId;

	/**
	 * Full package name.
	 */
	private int packageNameId;

	/**
	 * Simple class name.
	 */
	private int classNameId;

	/**
	 * List of full qualified parameter types.
	 */
	private List<Integer> parameterTypeIds;

	/**
	 * Full qualified parameter type.
	 */
	private Integer returnTypeId;

	/**
	 * The trace instance this signature belongs to.
	 */
	private TraceImpl trace;

	/**
	 * Default constructor.
	 */
	public Signature() {
	}

	/**
	 * Constructor.
	 * 
	 * @param trace
	 *            trace to which this signature belongs
	 * @param methodNameId
	 *            id of the simple method name
	 * @param packageNameId
	 *            id of the full package name
	 * @param classNameId
	 *            id of the simple class name
	 * @param parameterTypeIds
	 *            list of ids of the full qualified parameter types
	 * @param returnTypeId
	 *            id of the full qualified parameter type
	 */
	public Signature(TraceImpl trace, int methodNameId, int packageNameId,
			int classNameId, List<Integer> parameterTypeIds, int returnTypeId) {
		this.trace = trace;
		setMethodNameId(methodNameId);
		setPackageNameId(packageNameId);
		setClassNameId(classNameId);
		setParameterTypeIds(parameterTypeIds);
		setReturnTypeId(returnTypeId);
	}

	public String getMethodName() {
		return trace.getStringConstant(methodNameId);
	}

	public String getPackageName() {
		return trace.getStringConstant(packageNameId);
	}

	public String getReturnType() {
		return trace.getStringConstant(returnTypeId);
	}

	public String getClassName() {
		return trace.getStringConstant(classNameId);
	}

	/**
	 * 
	 * @return list of full qualified parameter types
	 */
	public List<String> getParameterTypes() {
//		List<String> pTypes = new ArrayList<String>();
//		for (int id : parameterTypeIds) {
//			pTypes.add(trace.getStringConstant(id));
//		}
		
		List<String> pTypes = parameterTypeIds.stream().map(id -> trace.getStringConstant(id)).collect(Collectors.toList());
		
		return pTypes;
	}

	/**
	 * @return the methodNameId
	 */
	public int getMethodNameId() {
		return methodNameId;
	}

	/**
	 * @param methodNameId
	 *            the methodNameId to set
	 */
	public void setMethodNameId(int methodNameId) {
		this.methodNameId = methodNameId;
	}

	/**
	 * @return the packageNameId
	 */
	public int getPackageNameId() {
		return packageNameId;
	}

	/**
	 * @param packageNameId
	 *            the packageNameId to set
	 */
	public void setPackageNameId(int packageNameId) {
		this.packageNameId = packageNameId;
	}

	/**
	 * @return the classNameId
	 */
	public int getClassNameId() {
		return classNameId;
	}

	/**
	 * @param classNameId
	 *            the classNameId to set
	 */
	public void setClassNameId(int classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * @return the parameterTypeIds
	 */
	public List<Integer> getParameterTypeIds() {
		return parameterTypeIds;
	}

	/**
	 * @param parameterTypeIds
	 *            the parameterTypeIds to set
	 */
	public void setParameterTypeIds(List<Integer> parameterTypeIds) {
		this.parameterTypeIds = parameterTypeIds;
	}

	/**
	 * @return the returnTypeId
	 */
	public int getReturnTypeId() {
		return returnTypeId;
	}

	/**
	 * @param returnTypeId
	 *            the returnTypeId to set
	 */
	public void setReturnTypeId(int returnTypeId) {
		this.returnTypeId = returnTypeId;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		boolean first = true;
		for (String pType : getParameterTypes()) {
			if (first) {
				first = false;
			} else {
				strBuilder.append(",");
			}
			strBuilder.append(pType);

		}
		return getPackageName() + PACKAGE_DELIMITER + getClassName()
				+ PACKAGE_DELIMITER + getMethodName() + "("
				+ strBuilder.toString() + ")";

	}

	public boolean isConstructor() {
		return getMethodName().equalsIgnoreCase(CONSTRUCTOR_PATTERN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + classNameId;
		result = prime * result + methodNameId;
		result = prime * result + packageNameId;
		result = prime
				* result
				+ ((parameterTypeIds == null) ? 0 : parameterTypeIds.hashCode());
		result = prime * result + returnTypeId;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Signature other = (Signature) obj;
		if (classNameId != other.classNameId) {
			return false;
		}
		if (methodNameId != other.methodNameId) {
			return false;
		}
		if (packageNameId != other.packageNameId) {
			return false;
		}
		if (parameterTypeIds == null) {
			if (other.parameterTypeIds != null) {
				return false;
			}
		} else if (!parameterTypeIds.equals(other.parameterTypeIds)) {
			return false;
		}
		if (returnTypeId != other.returnTypeId) {
			return false;
		}
		return true;
	}

}
