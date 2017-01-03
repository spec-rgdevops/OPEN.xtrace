package org.spec.research.open.xtrace.adapters.kieker.impl.additionalInformation;

import kieker.tools.traceAnalysis.systemModel.Execution;

import org.spec.research.open.xtrace.api.core.AdditionalInformation;

/**
 * This class saves @Execution data of a @Callable. These are relevant for
 * conversion between @MessageTrace#s and CTA traces.
 * 
 * @author Dominic Parga Cacheiro
 *
 */
public class CallableExecutionInformation implements AdditionalInformation {
	// |============|
	// | attributes |
	// |============|
	private int eoi;
	private int ess;
	private Execution receivingExecution;
	private Execution entryExecution; // for entry messages => usually null

	// |================|
	// | initialization |
	// |================|
	public CallableExecutionInformation(Execution sendingEntryExecution, Execution receivingExecution) {
		this.eoi = receivingExecution.getEoi();
		this.ess = receivingExecution.getEss();
		this.receivingExecution = receivingExecution;
		entryExecution = sendingEntryExecution;
	}

	// |===============|
	// | getter/setter |
	// |===============|
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

	public Execution getReceivingExecution() {
		return receivingExecution;
	}

	/**
	 * If the @Callable (that is containing this info) is an entry callable, it
	 * has to save the entry execution for start and end. Therefore, this method
	 * returns whether an entry @Execution is saved or not.
	 * 
	 * @return true, if there is an entry execution; false otherwise
	 */
	public boolean isEntryCallable() {
		return entryExecution != null;
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

	@Override
	public String getName() {
		return CallableExecutionInformation.class.getName();
	}
}