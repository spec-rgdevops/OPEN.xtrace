package org.spec.research.open.xtrace.adapters.appdynamics.source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spec.research.open.xtrace.adapters.appdynamics.enums.AppDynamicsProperties;
import org.spec.research.open.xtrace.adapters.appdynamics.enums.AppDynamicsTraceAttributes;

/**
 * 
 * Helper class for converting traces from AppDynamics into 'OPEN.xtrace'.
 * 
 * @author Manuel Palenga
 * @since 27.09.2016
 *
 */
class Utils {

	/**
	 * Get the content of a file.
	 * 
	 * @param path
	 * @return
	 */
	static String getContentOfFile(final String path) {

		StringBuilder content = new StringBuilder();
		BufferedReader reader = null;

		try {
			FileReader fileReader = new FileReader(path);
			reader = new BufferedReader(fileReader);

			String line = "";
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return content.toString();
	}

	/**
	 * Get a list of lines of the provided content.
	 * 
	 * @param content
	 * @return
	 */
	static List<String> getTraceLines(final String content) {
		String[] contentLines = content.split("\n");
		List<String> traceLines = new LinkedList<String>();

		// Skip information before first line break
		boolean firstLineBreak = false;
		for (String line : contentLines) {
			if (!firstLineBreak) {

				// Get only trace information
				if (line.isEmpty()) {
					firstLineBreak = true;
				}
				continue;
			}
			traceLines.add(line);
		}
		return traceLines;
	}

	/**
	 * Get the attribute value of the matching attribute in the provided line.
	 * 
	 * @param line
	 * @param attribute
	 * @return
	 */
	static String getTraceAttribute(final String line, final AppDynamicsTraceAttributes attribute) {

		String strProperty = attribute.getName();

		Matcher matcher = Pattern.compile(String.format("%s=\"[^\"]*\"", strProperty)).matcher(line);
		while (matcher.find()) {
			String result = matcher.group();
			int startQuote = result.indexOf("\"");
			int endQuote = result.lastIndexOf("\"");
			return result.substring(startQuote + 1, endQuote).trim();
		}
		return null;
	}

	/**
	 * Get the property value of the matching property in the provided content.
	 * 
	 * @param content
	 * @param property
	 * @return
	 */
	static String getProperty(final String content, final AppDynamicsProperties property) {

		String strProperty = property.getName();

		Matcher matcher = Pattern.compile(String.format("%s.*", strProperty)).matcher(content);
		while (matcher.find()) {
			String result = matcher.group();
			if (result.length() > strProperty.length()) {
				return result.substring(strProperty.length()).trim();
			}
		}
		return null;
	}

}
