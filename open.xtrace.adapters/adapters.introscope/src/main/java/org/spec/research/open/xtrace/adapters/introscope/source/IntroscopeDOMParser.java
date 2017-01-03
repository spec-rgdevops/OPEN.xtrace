package org.spec.research.open.xtrace.adapters.introscope.source;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.spec.research.open.xtrace.adapters.introscope.enums.IntroscopeAttributes;
import org.spec.research.open.xtrace.adapters.introscope.enums.IntroscopeParameterNames;
import org.spec.research.open.xtrace.adapters.introscope.enums.IntroscopeTags;
import org.spec.research.open.xtrace.api.core.Trace;
import org.spec.research.open.xtrace.api.core.callables.Callable;
import org.spec.research.open.xtrace.api.core.callables.HTTPMethod;
import org.spec.research.open.xtrace.dflt.impl.core.LocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.SubTraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.TraceImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractNestingCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.AbstractTimedCallableImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.DatabaseInvocationImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.HTTPRequestProcessingImpl;
import org.spec.research.open.xtrace.dflt.impl.core.callables.MethodInvocationImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class IntroscopeDOMParser {

	private static final String[] SQL_STATEMENT_PREFIXES = { "SELECT ", "UPDATE ", "INSERT ", "DELETE ", "CREATE DATABASE", "CREATE TABLE", "CREATE VIEW",
			"CREATE INDEX ", "ALTER DATABASE", "ALTER TABLE", "DROP TABLE", "DROP INDEX", "DROP VIEW", "VALUES ", "WITH " };
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	private Document doc;
	private TraceImpl traceImpl = null;
	private long startTraceTimestamp = 0;
	private int timeFactorToNano = 1000000;

	protected void initParser(final String path) {

		try {
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw new IllegalStateException("Initialization of parser failed! (maybe not a xml file)");
		}
	}

	protected Trace parseCAToOPEN_xtrace() {

		Element currentElement = doc.getDocumentElement();
		setTimeFactorToNano(currentElement);

		if (!currentElement.getTagName().equals(IntroscopeTags.SUB_TRACE)) {

			NodeList nodeList = currentElement.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node childNode = nodeList.item(i);
				if (this.isSearchedNode(childNode, IntroscopeTags.SUB_TRACE)) {
					parse((Element) childNode, null);
				}
			}

		}

		return traceImpl;
	}

	private void setTimeFactorToNano(final Element currentElement) {

		if (this.isSearchedNode(currentElement, IntroscopeTags.TRANSACTION_TRACER_SESSION)) {
			String timeUnit = getAttributeValue(currentElement, IntroscopeAttributes.TIME_UNITS);
			switch (timeUnit) {
			case "ns":
				timeFactorToNano = 0;
				break;
			case "µs":
				timeFactorToNano = 1000;
				break;
			case "ms":
				timeFactorToNano = 1000000;
				break;
			case "s":
				timeFactorToNano = 1000000000;
				break;
			}
		}
	}

	private void parse(final Element currentElement, final AbstractNestingCallableImpl parentCallable) {

		Callable thisCallable = createOPEN_xtraceElement(currentElement, parentCallable);

		if (thisCallable != null && !(thisCallable instanceof AbstractNestingCallableImpl)) {
			return;
		}

		NodeList nodeList = currentElement.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node childNode = nodeList.item(i);
			if (this.isSearchedNode(childNode, IntroscopeTags.SUB_TRACE, IntroscopeTags.CALLED_COMPONENT_LIST, IntroscopeTags.CALLED_COMPONENT)) {

				parse((Element) childNode, (AbstractNestingCallableImpl) thisCallable);
			}
		}
	}

	private boolean isSearchedNode(final Node node, final IntroscopeTags... tagNames) {

		if (Node.ELEMENT_NODE != node.getNodeType()) {
			return false;
		}
		for (IntroscopeTags introscopeTags : tagNames) {
			if (introscopeTags.getName().equals(node.getNodeName())) {
				return true;
			}
		}
		return false;
	}

	private Callable createOPEN_xtraceElement(final Element currentElement, final Callable parentCallable) {

		switch (IntroscopeTags.fromName(currentElement.getTagName())) {
		case SUB_TRACE:
			newSubTrace(currentElement);

			return null;
		case CALLED_COMPONENT:

			SubTraceImpl currentSubTraceImpl = null;
			AbstractNestingCallableImpl currentNestingCallable = null;

			if (parentCallable == null) {
				currentSubTraceImpl = (SubTraceImpl) traceImpl.getRoot();
			} else {
				currentSubTraceImpl = (SubTraceImpl) parentCallable.getContainingSubTrace();
				currentNestingCallable = (AbstractNestingCallableImpl) parentCallable;
			}

			AbstractTimedCallableImpl timedCallableImpl = getDatabaseInvocation(currentElement, currentNestingCallable, currentSubTraceImpl);
			if (timedCallableImpl == null) {
				timedCallableImpl = getHTTPRequestProcessing(currentElement, currentNestingCallable, currentSubTraceImpl);
				if (timedCallableImpl == null) {
					timedCallableImpl = getMethodInvocation(currentElement, currentNestingCallable, currentSubTraceImpl);
				}
			}

			timedCallableImpl.setResponseTime(timeFactorToNano * Long.parseLong(getAttributeValue(currentElement, IntroscopeAttributes.DURATION)));
			timedCallableImpl.setTimestamp(startTraceTimestamp + Integer.parseInt(getAttributeValue(currentElement, IntroscopeAttributes.REL_TIMESTAMP)));

			if (traceImpl.getRoot().getRoot() == null) {

				NodeList nodeList = currentElement.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (this.isSearchedNode(node, IntroscopeTags.PARAMETER_LIST)) {
						Optional<String> optThreadName = getAttributeValueFromParameterList(node, IntroscopeParameterNames.THREAD_NAME);
						if (optThreadName.isPresent()) {
							timedCallableImpl.setThreadName(optThreadName.get());
						}
						break;
					}
				}

				((SubTraceImpl) traceImpl.getRoot()).setRoot(timedCallableImpl);
			}

			return timedCallableImpl;
		case CALLED_COMPONENT_LIST:
			return parentCallable;
		default:
			return null;
		}

	}

	private MethodInvocationImpl getMethodInvocation(final Element currentElement, final AbstractNestingCallableImpl parentNestingCallable,
			final SubTraceImpl subTraceImpl) {

		MethodInvocationImpl methodInvocationImpl = new MethodInvocationImpl(parentNestingCallable, subTraceImpl);

		NodeList nodeList = currentElement.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node childNode = nodeList.item(i);
			if (!this.isSearchedNode(childNode, IntroscopeTags.PARAMETER_LIST)) {
				continue;
			}

			// Only PARAMETER_LIST

			Optional<String> optMethodName = getAttributeValueFromParameterList(childNode, IntroscopeParameterNames.METHOD);
			Optional<String> optClassName = getAttributeValueFromParameterList(childNode, IntroscopeParameterNames.CLASS);
			Optional<String> optMethodDescriptor = getAttributeValueFromParameterList(childNode, IntroscopeParameterNames.METHOD_DESCRIPTOR);

			if (optMethodName.isPresent()) {
				methodInvocationImpl.setMethodName(optMethodName.get());
			}

			if (optClassName.isPresent()) {

				// Split package and class name
				int posLastPoint = optClassName.get().lastIndexOf('.');
				String className = optClassName.get().substring(posLastPoint + 1);
				String packageName = optClassName.get().substring(0, posLastPoint);

				methodInvocationImpl.setPackageName(packageName);
				methodInvocationImpl.setClassName(className);
			}

			if (optMethodDescriptor.isPresent()) {
				methodInvocationImpl.setParameterTypes(SignatureUtils.getParameterTypes(optMethodDescriptor.get()));
				methodInvocationImpl.setReturnType(SignatureUtils.getReturnType(optMethodDescriptor.get()));
			}

			this.setSignature(methodInvocationImpl);
		}

		return methodInvocationImpl;
	}

	private void setSignature(final MethodInvocationImpl invocationImpl) {

		if (invocationImpl.getClassName().isPresent() && invocationImpl.getPackageName().isPresent() && invocationImpl.getMethodName().isPresent()) {
			String signature = String.format("%s.%s.%s(", invocationImpl.getPackageName().get(), invocationImpl.getClassName().get(), invocationImpl
					.getMethodName().get());

			String signatureParameters = "";
			if (invocationImpl.getParameterTypes() != null && invocationImpl.getParameterTypes().isPresent()
					&& !invocationImpl.getParameterTypes().get().isEmpty()) {
				for (String parameter : invocationImpl.getParameterTypes().get()) {
					signatureParameters += parameter + ", ";
				}

				// Remove last comma
				signatureParameters = signatureParameters.substring(0, signatureParameters.length() - 2);
			}

			signature += signatureParameters + ")";

			invocationImpl.setSignature(signature);
		}
	}

	private Optional<String> getAttributeValueFromParameterList(final Node parameterList, final IntroscopeParameterNames attributeName) {

		if (!this.isSearchedNode(parameterList, IntroscopeTags.PARAMETER_LIST)) {
			throw new IllegalArgumentException(parameterList + " is not a 'Parameters' node!");
		}

		NodeList nodeList = parameterList.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (this.isSearchedNode(node, IntroscopeTags.PARAMETER)) {
				Optional<String> optValue = getAttributeValue(node, attributeName.getName());
				if (optValue.isPresent()) {
					return optValue;
				}
			}
		}

		return Optional.empty();
	}

	private Optional<String> getAttributeValue(final Node parameterNode, final String attributeName) {

		Optional<String> optValue = Optional.empty();

		if (parameterNode.getAttributes().getNamedItem(IntroscopeAttributes.INFORMATION_NAME.getName()).getNodeValue().equals(attributeName)) {

			String value = parameterNode.getAttributes().getNamedItem(IntroscopeAttributes.INFORMATION_VALUE.getName()).getNodeValue();
			return Optional.of(value);
		}

		return optValue;
	}

	private void newSubTrace(final Element currentElement) {

		traceImpl = new TraceImpl(System.nanoTime());

		try {
			final String strDate = getAttributeValue(currentElement, IntroscopeAttributes.SUB_TRACE_TIMESTAMP);
			startTraceTimestamp = dateFormat.parse(strDate).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		SubTraceImpl subTrace = new SubTraceImpl(System.nanoTime(), null, traceImpl);
		traceImpl.setRoot(subTrace);

		final String host = getAttributeValue(currentElement, IntroscopeAttributes.LOCATION_HOST);
		final String runTimeEnvironment = getAttributeValue(currentElement, IntroscopeAttributes.LOCATION_RUNTIME);
		LocationImpl location = new LocationImpl(host, runTimeEnvironment, null, null);
		subTrace.setLocation(location);
	}

	private DatabaseInvocationImpl getDatabaseInvocation(final Element currentElement, final Callable parentCallable, final SubTraceImpl subTraceImpl) {

		final String componentName = getAttributeValue(currentElement, IntroscopeAttributes.CALLABLE_COMPONENT_NAME);
		final String componentNameUpperCase = componentName.toUpperCase().trim();

		for (String prefix : SQL_STATEMENT_PREFIXES) {
			if (componentNameUpperCase.startsWith(prefix)) {
				DatabaseInvocationImpl dbInvocation = new DatabaseInvocationImpl((AbstractNestingCallableImpl) parentCallable, subTraceImpl);
				dbInvocation.setSQLStatement(componentName);
				return dbInvocation;
			}
		}

		return null;
	}

	private HTTPRequestProcessingImpl getHTTPRequestProcessing(final Element currentElement, final Callable parentCallable, final SubTraceImpl subTraceImpl) {

		NodeList callableChildren = currentElement.getChildNodes();
		Node parameterListNode = null;
		for (int i = 0; i < callableChildren.getLength(); i++) {
			parameterListNode = callableChildren.item(i);
			if (this.isSearchedNode(parameterListNode, IntroscopeTags.PARAMETER_LIST)) {
				break;
			}
		}

		Optional<String> optHttpMethod = getAttributeValueFromParameterList(parameterListNode, IntroscopeParameterNames.HTTP_METHOD);

		if (optHttpMethod.isPresent()) {

			HTTPRequestProcessingImpl httpRequestProcessingImpl = new HTTPRequestProcessingImpl((AbstractNestingCallableImpl) parentCallable, subTraceImpl);

			httpRequestProcessingImpl.setRequestMethod(HTTPMethod.valueOf(optHttpMethod.get()));

			Optional<String> optURL = getAttributeValueFromParameterList(parameterListNode, IntroscopeParameterNames.URL);

			httpRequestProcessingImpl.setUri(optURL.orElse(null));

			return httpRequestProcessingImpl;
		}

		return null;
	}

	private String getAttributeValue(final Element currentElement, final IntroscopeAttributes attribute) {
		return currentElement.getAttribute(attribute.getName());
	}
}
