package org.spec.research.open.xtrace.api.core;

import org.spec.research.open.xtrace.api.core.callables.Callable;

/**
 * A {@link Trace} subsumes a logical invocation sequence through the target system potentially
 * passing multiple system nodes, containers or applications. Hence, a {@link Trace} consists of a
 * composite structure (tree structure) of {@link SubTrace} instances.
 * 
 * @author Alexander Wert, Christoph Heger
 *
 */
public interface Trace extends TimedElement, TreeIterable<Callable>, Identifiable {

	/**
	 * Factor to convert nanoseconds to milliseconds.
	 */
	double NANOS_TO_MILLIS_FACTOR = 0.000001;

	/**
	 * Factor to convert milliseconds to nanoseconds.
	 */
	long MILLIS_TO_NANOS_FACTOR = 1000000L;

	/**
	 * 
	 * @return the root of the sub trace composite structure. This is usually the entry point to the
	 *         application.
	 */
	SubTrace getRoot();

	/**
	 * 
	 * @return an iterator on the tree structure of the sub traces
	 */
	TreeIterator<SubTrace> subTraceIterator();

	/**
	 * 
	 * @return the identifier of the entire logical trace (encapsulating all subtraces that belong
	 *         to the logical trace)
	 */
	long getTraceId();

	/**
	 * 
	 * @return the number of nodes (i.e. {@link Callable}) in the tree structure of the
	 *         corresponding {@link Trace}
	 */
	int size();

}
