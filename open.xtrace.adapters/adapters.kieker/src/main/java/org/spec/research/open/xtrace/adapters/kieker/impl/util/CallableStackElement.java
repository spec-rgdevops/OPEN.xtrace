package org.spec.research.open.xtrace.adapters.kieker.impl.util;

import java.util.ArrayList;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.Execution;

import org.spec.research.open.xtrace.adapters.kieker.impl.additionalInformation.CallableExecutionInformation;
import org.spec.research.open.xtrace.api.core.AdditionalInformation;
import org.spec.research.open.xtrace.api.core.callables.Callable;

/**
 * A @CallableStackElement is some kind of proto-@AbstractMessage. It is used to
 * save a @Callable and its @Execution state (eoi, ess) in the CTA trace
 * (@TraceImpl) to create an @AbstractMessage out of it later. Because you have
 * to save each callable in a stack (@CtaToKiekerTraceConverter) and need access
 * to its executions, it is easier to outsource these infos.
 * 
 * @author Dominic Parga Cacheiro
 *
 */
class CallableStackElement {
	// |============|
	// | attributes |
	// |============|
	private final Callable callable;
	private int eoi;
	private int ess;
	private Execution receivingExecution;
	private Execution entryExecution;

	// |================|
	// | initialization |
	// |================|
	public CallableStackElement(Callable callable) {
		super();
		this.callable = callable;
		eoi = -1;
		ess = -1;
		// extract infos
		List<AdditionalInformation> infos = new ArrayList<AdditionalInformation>();
		if (callable.getAdditionalInformation().isPresent()) {
			infos.addAll(callable.getAdditionalInformation().get());
		}
		for (AdditionalInformation info : infos) {
			if (info instanceof CallableExecutionInformation) {
				CallableExecutionInformation callableExecutionInfo = (CallableExecutionInformation) info;
				eoi = callableExecutionInfo.getEoi();
				ess = callableExecutionInfo.getEss();
				receivingExecution = callableExecutionInfo.getReceivingExecution();
				entryExecution = callableExecutionInfo.getEntryExecution();
				break;
			}
		}
	}

	// |===============|
	// | getter/setter |
	// |===============|
	@Override
	public String toString() {
		return "(eoi, ess) = (" + eoi + ", " + ess + ")";
	}

	/**
	 * @return execution order index
	 */
	public int getEoi() {
		return eoi;
	}

	/**
	 * @return execution stack TODO
	 */
	public int getEss() {
		return ess;
	}

	public Execution getExecution() {
		return receivingExecution;
	}

	/**
	 * If the @Callable (that is containing this info) is an entry callable, it
	 * has to save the entry execution for start and end. Therefore, this method
	 * returns this entry @Execution or null, if there is no entry execution.
	 * 
	 * @return entry execution if existing, null otherwise
	 */
	public Execution getEntryExecution() {
		return entryExecution;
	}

	/**
	 * @return @Callable.getTimestamp()
	 */
	public long getTimestamp() {
		return callable.getTimestamp();
	}
}