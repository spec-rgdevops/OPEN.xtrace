/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.spec.research.open.xtrace.adapters.kieker.source;

import java.util.concurrent.LinkedBlockingQueue;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;

import org.spec.research.open.xtrace.adapters.kieker.impl.TraceImpl;
import org.spec.research.open.xtrace.api.core.Trace;

/**
 * This filter converts traces into 'OPEN.xtraces'.
 * 
 * @author Dusan Okanovic
 *
 * @since 1.12
 */
@Plugin(description = "A filter to print the object to a configured stream", outputPorts = @OutputPort(name = OPENxtraceFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object", eventTypes = { Object.class }), configuration = {})
public final class OPENxtraceFilter extends AbstractFilterPlugin {

	/** The name of the input port for incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	/** The name of the output port delivering the incoming events. */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	private boolean active;
	private LinkedBlockingQueue<Trace> traceQueue;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public OPENxtraceFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.active = true;
	}

	@Override
	public final void terminate(final boolean error) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		// We reverse the if-decisions within the constructor.
		return configuration;
	}

	// private static int runs = 1;

	/**
	 * This method converts Kieker's trace object into 'OPEN.xtraces'.
	 * 
	 * @param object
	 *            The new object.
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Receives incoming objects to be logged and forwarded", eventTypes = { Object.class })
	public final void inputEvent(final Object object) {
		System.out.println("Input");
		if (this.active) {
			if (object instanceof MessageTrace) {
				MessageTrace messageTrace = (MessageTrace) object;
				// if (messageTrace.getSequenceAsVector().size() < 5)
				// return;
				// if (runs-- > 0) {
				TraceImpl trace = TraceImpl.createCallableTrace(messageTrace);
				try {
					traceQueue.put(trace);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// System.out.println("- Kieker-Message-Trace:\n"
				// + messageTrace);
				// System.out.println("- CTA-TraceImpl:\n" + trace);
				// System.out
				// .println("- Starting Dominic's conversion from CTA to Kieker");
				// MessageTrace dominicsMessageTrace = TraceImpl
				// .createMessageTrace(trace);
				// System.out
				// .println("- Finished Dominic's conversion from CTA to Kieker");
				// System.out.println("- Dominic's Kieker-MessageTrace:\n"
				// + dominicsMessageTrace);
				// Trace lastTrace = TraceImpl
				// .createCallableTrace(dominicsMessageTrace);
				// System.out
				// .println("- CTA-TraceImpl out of Dominic's Kieker-MessageTrace:\n"
				// + lastTrace);
				// } else {
				// System.exit(0);
				// }
			}
		}
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
	}

	public void setTraceQueue(LinkedBlockingQueue<Trace> traceQueue) {
		this.traceQueue = traceQueue;
	}
}