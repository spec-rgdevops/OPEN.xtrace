package org.spec.research.open.xtrace.adapters.inspectit.importer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rocks.inspectit.shared.all.cmr.model.MethodIdent;
import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;

public class InvocationSequences implements Iterable<InvocationSequenceData> {
	private final List<InvocationSequenceData> invocationSequences;
	private PlatformIdent platformIdent;

	public InvocationSequences() {
		invocationSequences = new LinkedList<InvocationSequenceData>();
	}

	/**
	 * @return the platformIdent
	 */
	public PlatformIdent getPlatformIdent() {
		return platformIdent;
	}

	/**
	 * @param platformIdent
	 *            the platformIdent to set
	 */
	public void setPlatformIdent(PlatformIdent platformIdent) {
		this.platformIdent = platformIdent;
	}

	/**
	 * @return the invocationSequences
	 */
	public List<InvocationSequenceData> getInvocationSequences() {
		return invocationSequences;
	}

	public void addInvocationSequence(InvocationSequenceData isData) {
		invocationSequences.add(isData);
	}

	/**
	 * Retrieves the full qualified method name for the given {@link InvocationSequenceData}
	 * 
	 * @param isData
	 *            the {@link InvocationSequenceData} instance to retrieve the method name for
	 * @return the method name, or null if method name cannot be found for the given
	 *         {@link InvocationSequenceData} instance
	 */
	public String getFullQualifiedMethodName(InvocationSequenceData isData) {
		long mIdent = isData.getMethodIdent();
		for (MethodIdent mi : platformIdent.getMethodIdents()) {

			if (mi.getId().equals(mIdent)) {
				String fqn = mi.getPackageName() + "." + mi.getMethodName() + "(";
				boolean first = true;
				for (String par : mi.getParameters()) {
					if (first) {
						fqn += par;
						first = false;
					} else {
						fqn += "," + par;
					}

				}
				fqn += ")";
				return fqn;
			}
		}
		return null;
	}

	/**
	 * Retrieves the method name for the given {@link InvocationSequenceData}
	 * 
	 * @param isData
	 *            the {@link InvocationSequenceData} instance to retrieve the method name for
	 * @return the method name, or null if method name cannot be found for the given
	 *         {@link InvocationSequenceData} instance
	 */
	public String getMethodName(InvocationSequenceData isData) {
		long mIdent = isData.getMethodIdent();
		for (MethodIdent mi : platformIdent.getMethodIdents()) {

			if (mi.getId().equals(mIdent)) {
				return mi.getFQN() + "." + mi.getMethodName();
			}
		}
		return null;
	}

	/**
	 * Retrieves the simple method name for the given {@link InvocationSequenceData}
	 * 
	 * @param isData
	 *            the {@link InvocationSequenceData} instance to retrieve the method name for
	 * @return the method name, or null if method name cannot be found for the given
	 *         {@link InvocationSequenceData} instance
	 */
	public String getSimpleMethodName(InvocationSequenceData isData) {
		long mIdent = isData.getMethodIdent();
		for (MethodIdent mi : platformIdent.getMethodIdents()) {

			if (mi.getId().equals(mIdent)) {
				return mi.getMethodName();
			}
		}
		return null;
	}

	@Override
	public Iterator<InvocationSequenceData> iterator() {
		return invocationSequences.iterator();
	}

}
