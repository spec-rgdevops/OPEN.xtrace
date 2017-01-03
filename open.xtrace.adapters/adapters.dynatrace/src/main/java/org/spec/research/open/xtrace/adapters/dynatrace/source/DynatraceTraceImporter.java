package org.spec.research.open.xtrace.adapters.dynatrace.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.diagnoseit.spike.shared.TraceSource;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Dashboardreport;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Dashboardreport.Data;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Purepathsdashlet;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Purepathsdashlet.Purepaths;
import org.spec.research.open.xtrace.adapters.dynatrace.model.generated.Purepathsdashlet.Purepaths.Purepath;
import org.spec.research.open.xtrace.api.core.Trace;

/**
 * {@link TraceSource} for imported Pure Paths from XML file.
 * 
 * @author Christoph Heger, Manuel Palenga
 * @since 06.11.2015
 */
public class DynatraceTraceImporter {

	/**
	 * Converts the provided purepath into OPEN.xtrace.
	 * 
	 * @param purepath
	 *            Trace to convert.
	 * @return Converted trace.
	 */
	public Trace convertPurepathToTrace(final Purepath purepath) {

		PurepathTransformer transformer = new PurepathTransformer();
		return transformer.transform(purepath);
	}

	/**
	 * Imports the Pure Paths from the XML file and provides them as
	 * {@link Trace}s.
	 *
	 * @param file
	 *            the path to the import file
	 * @return the {@link List} of {@link Trace}s
	 * @throws FileNotFoundException
	 */
	public List<Trace> importTracesFromFile(final String path) throws FileNotFoundException {

		this.checkPath(path);

		List<Trace> traces = new LinkedList<Trace>();

		for (Purepath purepath : getPurePaths(path).getPurepath()) {
			traces.add(convertPurepathToTrace(purepath));
		}

		return traces;
	}

	/**
	 * Returns a list of purepaths from the provided file.
	 *
	 * @param file
	 *            the path to the import file
	 * @return the {@link List} of {@link Purepath}s
	 * @throws FileNotFoundException
	 */
	public List<Purepath> getPurepathsFromFile(final String path) throws FileNotFoundException {

		this.checkPath(path);

		return getPurePaths(path).getPurepath();
	}

	/**
	 * Read the provided xml data and return the purepaths of the file.
	 * 
	 * @param path
	 *            Path to xml file.
	 * @return Purepaths of xml file.
	 */
	Purepaths getPurePaths(final String path) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Dashboardreport.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Dashboardreport report = (Dashboardreport) jaxbUnmarshaller.unmarshal(new File(path));
			Data data = report.getData();

			for (Object obj : data.getAdmdashletOrAgentbreakdowndashletOrAgentsoverviewdashlet()) {
				if (obj instanceof Purepathsdashlet) {
					return ((Purepathsdashlet) obj).getPurepaths();
				}
			}

		} catch (JAXBException e) {
			throw new IllegalArgumentException("Data in file is improperly formatted!");
		}

		return null;
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
