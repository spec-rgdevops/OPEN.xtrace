package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.NestingExceptionThrow;

/**
 * Default implementation of the {@link NestingExceptionThrow} API element.
 * 
 * @author Manuel Palenga, Alper Hidiroglu
 *
 */
public class NestingExceptionThrowImpl implements NestingExceptionThrow, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7175343664830255482L;

	/**
	 * Error message of the exception.
	 */
	private String errorMessage;

	/**
	 * Stack trace of the exception.
	 */
	private String stackTrace;

	/**
	 * Type of the throwable.
	 */
	private String throwableType;

	/**
	 * The nested Exception causing this exception.
	 */
	private Optional<NestingExceptionThrow> nestingExceptionThrow = Optional.empty();

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public Optional<NestingExceptionThrow> getCausing() {
		return this.nestingExceptionThrow;
	}

	/**
	 * @param nestingExceptionThrow
	 *            the causing nested exception to the exception
	 */
	public void setCausing(NestingExceptionThrow nestingExceptionThrow) {
		this.nestingExceptionThrow = Optional.ofNullable(nestingExceptionThrow);
	}

	@Override
	public Optional<String> getStackTrace() {
		return Optional.ofNullable(stackTrace);
	}

	@Override
	public Optional<String> getThrowableType() {
		return Optional.ofNullable(throwableType);
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @param stackTrace
	 *            the stackTrace to set
	 */
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	/**
	 * @param throwableType
	 *            the throwableType to set
	 */
	public void setThrowableType(String throwableType) {
		this.throwableType = throwableType;
	}

}
