package org.spec.research.open.xtrace.adapters.introscope.source;

import java.io.File;
import java.io.FileNotFoundException;

import org.spec.research.open.xtrace.api.core.Trace;

public class IntroscopeTraceImporter {

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
	public Trace importTraceFromFile(final String path) throws IllegalStateException, FileNotFoundException {

		this.checkPath(path);

		IntroscopeDOMParser domParser = new IntroscopeDOMParser();
		domParser.initParser(path);
		return domParser.parseCAToOPEN_xtrace();
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
