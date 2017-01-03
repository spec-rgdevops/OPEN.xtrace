package org.spec.research.open.xtrace.adapters.appdynamics.source;

import java.io.File;
import java.io.FileNotFoundException;

import org.spec.research.open.xtrace.api.core.Trace;

/**
 * 
 * Converts AppDynamics traces into 'OPEN.xtrace'.
 * 
 * @author Manuel Palenga
 * @since 27.09.2016
 *
 */
public class AppDynamicsTraceImporter {

	/**
	 * Parse trace from file to OPEN.xtrace.
	 * 
	 * @param path
	 *            Path to file.
	 * @return OPEN.xtrace from file
	 * @throws IllegalStateException
	 *             Initialization of parser failed
	 * @throws FileNotFoundException
	 */
	public Trace importTraceFromFile(final String path) throws FileNotFoundException {

		this.checkPath(path);

		CallGraphParser parser = new CallGraphParser();
		return parser.getOPEN_xtrace(path);
	}

	/**
	 * Check whether the file exists.
	 * 
	 * @param path
	 *            Path to file.
	 * @throws FileNotFoundException
	 */
	void checkPath(final String path) throws FileNotFoundException {

		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("No file found with path: " + path);
		}
	}
}
