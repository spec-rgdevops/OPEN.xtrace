package org.spec.research.open.xtrace.dflt.impl.core.callables;

import java.io.Serializable;
import java.util.Optional;

import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.LoggingInvocation;
import org.spec.research.open.xtrace.api.utils.StringUtils;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;

/**
 * Default implementation of the {@link LoggingInvocation} API element.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public class LoggingInvocationImpl extends AbstractCallableImpl implements LoggingInvocation, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7758739160409253319L;

	/**
	 * Logging level.
	 */
	private String loggingLevel;

	/**
	 * Logged message.
	 */
	private String message;

	/**
	 * Default constructor for serialization. This constructor should not be used except for
	 * deserialization.
	 */
	public LoggingInvocationImpl() {
	}

	/**
	 * Constructor. Adds the newly created {@link Callable} instance to the passed parent if the
	 * parent is not null!
	 * 
	 * @param parent
	 *            {@link AbstractNestingCallableImpl} that called this Callable
	 * @param containingSubTrace
	 *            the SubTrace containing this Callable
	 */
	public LoggingInvocationImpl(AbstractNestingCallableImpl parent, SubTraceImpl containingSubTrace) {
		super(parent, containingSubTrace);
	}

	@Override
	public Optional<String> getLoggingLevel() {
		return Optional.ofNullable(loggingLevel);
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param loggingLevel
	 *            the loggingLevel to set
	 */
	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return StringUtils.getStringRepresentation(this);
	}

}
