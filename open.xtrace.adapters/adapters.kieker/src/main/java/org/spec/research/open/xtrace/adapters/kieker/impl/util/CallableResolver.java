/**
 * 
 */
package org.spec.research.open.xtrace.adapters.kieker.impl.util;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * @author Okanovic
 *
 */
public class CallableResolver {
	// TODO
	public static boolean isHttpCall(Execution e) {
		return false;
	}

	// TODO
	public static boolean isDbCall(Execution e) {
		return false;
	}

	public static boolean isRemoteCall(AbstractMessage message) {
		String receivingHost = message.getReceivingExecution().getAllocationComponent().getExecutionContainer().getName();
		String sendingHost = message.getSendingExecution().getAllocationComponent().getExecutionContainer().getName();
		return sendingHost.equals(receivingHost);
	}
}
