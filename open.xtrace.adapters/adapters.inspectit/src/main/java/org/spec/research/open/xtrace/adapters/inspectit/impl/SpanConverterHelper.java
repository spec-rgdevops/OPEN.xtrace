package org.spec.research.open.xtrace.adapters.inspectit.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spec.research.open.xtrace.adapters.inspectit.importer.MobileTraceData;

import rocks.inspectit.shared.all.cmr.model.PlatformIdent;
import rocks.inspectit.shared.all.communication.data.InvocationSequenceData;
import rocks.inspectit.shared.all.communication.data.MobilePeriodicMeasurement;
import rocks.inspectit.shared.all.tracing.data.Span;
import rocks.inspectit.shared.all.tracing.data.SpanIdent;

class SpanConverterHelper {
	
	private static final String REQUEST = "http.request.";
	private static final String RESPONSE = "http.response.";
	private static final String URL = "http.url";
	private static final String OPERATIONNAME = "ext.operation.name";
	
	protected IITAbstractCallable createCallable(IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent, IITTraceImpl trace, MobileTraceData traceData) {
		
		Span rootSpan = getRootSpan(traceData.getSpans());
		if(rootSpan == null){
			throw new IllegalArgumentException("No root span found!");
		}
		
		// TraceID is equal usecaseID
		long traceID = rootSpan.getSpanIdent().getTraceId();

		String operationName = rootSpan.getTags().get(OPERATIONNAME);
		
		InvocationSequenceData data = new InvocationSequenceData();
		data.setTimeStamp(rootSpan.getTimeStamp());
		data.setDuration(rootSpan.getDuration());
		IITMobileMetaMeasurementCallable rootCallable = new IITMobileMetaMeasurementCallable(containingTrace, parent, traceID, operationName, null, data);
		
		addChildren(trace, traceData, containingTrace, rootCallable, rootSpan, traceData.getMeasurements());
		
		return rootCallable;
	}
	
	private void addChildren(IITTraceImpl trace, MobileTraceData traceData, IITSubTraceImpl containingTrace, IITMobileMetaMeasurementCallable parent, Span parentSpan, List<MobilePeriodicMeasurement> parentMeasurements){

		Timestamp startTime = parentSpan.getTimeStamp();
		if(startTime == null){
			throw new IllegalArgumentException("Timestamp of span with id " + parentSpan.getSpanIdent().getId() + " is null!");
		}
		
		// Get all measurements for complete span (with nesting spans)
		List<MobilePeriodicMeasurement> measurements = getMeasurementsInInterval(parentMeasurements, startTime.getTime(), (long)parentSpan.getDuration());				
		for (MobilePeriodicMeasurement periodicMeasurement : measurements) {
			parentMeasurements.remove(periodicMeasurement);
		}
		
		List<Span> children = getChildrenOfSpan(traceData.getSpans(), parentSpan);
		for (Span child : children) {
			if(child.getTags().containsKey(URL)){
				
				Map<String, String> requestMeasurement = new HashMap<String, String>();
				Map<String, String> responseMeasurement = new HashMap<String, String>();
				
				for (String key : child.getTags().keySet()) {
					int lastIndexOfPoint = key.lastIndexOf('.');
					String value = child.getTags().get(key);
					
					if(lastIndexOfPoint == -1){
						requestMeasurement.put(key, value);
						responseMeasurement.put(key, value);
					} else {
						String prefix = key.substring(0, lastIndexOfPoint);
						String suffix = key.substring(lastIndexOfPoint);
						if(prefix.equals(REQUEST)){
							requestMeasurement.put(suffix, value);				
						} else if(prefix.equals(RESPONSE)){
							responseMeasurement.put(suffix, value);				
						} else {
							requestMeasurement.put(suffix, value);
							responseMeasurement.put(suffix, value);	
						}
					}
				}

				Long requestTimestamp = null;
				Long responseTimestamp = null;
				if(child.getTimeStamp() != null){
					requestTimestamp = child.getTimeStamp().getTime();
					responseTimestamp = requestTimestamp + (long) child.getDuration();
				}
				
				IITRemoteInvocation remoteInvocation = null;
				
				List<Span> childrenOfRemoteMeasurement = getChildrenOfSpan(traceData.getSpans(), child);
				
				if(childrenOfRemoteMeasurement.isEmpty()){
					// Remote system was not tracked
					remoteInvocation = new IITRemoteInvocation(null, containingTrace, parent);
					remoteInvocation.setTargetSubTrace(null);	
				} else {
					// Remote system was tracked
					InvocationSequenceData remoteCall = getInvocationSequenceDataWithSpanID(childrenOfRemoteMeasurement.get(0).getSpanIdent().getId(), traceData.getInvocationSequenceDatas());
					remoteInvocation = new IITRemoteInvocation(remoteCall, containingTrace, parent);
					remoteInvocation.setTargetSubTrace(new IITSubTraceImpl(trace, remoteCall, getPlatformIdentWithID(remoteCall.getPlatformIdent(), traceData.getPlatformIdents())));	
				}
				
				remoteInvocation.setRequestMeasurement(new IITMobileRemoteMeasurement(requestTimestamp, requestMeasurement));
				remoteInvocation.setResponseMeasurement(new IITMobileRemoteMeasurement(responseTimestamp, responseMeasurement));
				parent.addChild(remoteInvocation);
			} else {				
				InvocationSequenceData data = new InvocationSequenceData();
				data.setTimeStamp(child.getTimeStamp());
				data.setDuration(child.getDuration());
				IITMobileMetaMeasurementCallable usecaseCallable = new IITMobileMetaMeasurementCallable(containingTrace, parent, parent.getUseCaseID().get(), parent.getUseCaseName().orElse(""), null, data);
				parent.addChild(usecaseCallable);
				
				addChildren(trace, traceData, containingTrace, usecaseCallable, child, measurements);
			}
		}		

		// Add only span measurements, not nesting spans measurements
		for (MobilePeriodicMeasurement periodicMeasurement : measurements) {
			InvocationSequenceData data = new InvocationSequenceData();
			data.setTimeStamp(periodicMeasurement.getTimeStamp());
			IITAbstractNestingCallable childCallable = new IITMobileMetaMeasurementCallable(containingTrace, parent, parent.getUseCaseID().get(), parent.getUseCaseName().get(), periodicMeasurement, data);
			parent.addChild(childCallable);
		}
	}
	
	private Span getRootSpan(List<Span> spans){
		for (Span span : spans) {
			SpanIdent ident = span.getSpanIdent();
			if(ident.getId() == ident.getParentId() && ident.getId() == ident.getTraceId()){
				return span;
			}
		}
		return null;
	}
	
	private List<MobilePeriodicMeasurement> getMeasurementsInInterval(List<MobilePeriodicMeasurement> measurements, long from, long duration){
		long to = from + duration;
		
		List<MobilePeriodicMeasurement> results = new ArrayList<MobilePeriodicMeasurement>();
		for (MobilePeriodicMeasurement periodicMeasurement : measurements) {
			if(periodicMeasurement.getTimestamp() >= from && periodicMeasurement.getTimestamp() <= to){
				results.add(periodicMeasurement);
			}
		}
		return results;
	}
	
	private List<Span> getChildrenOfSpan(List<Span> spans, Span span){
		List<Span> children = new ArrayList<Span>();
		for (Span element : spans) {
			if(element.getSpanIdent().getParentId() == span.getSpanIdent().getId() && element.getSpanIdent().getParentId() != element.getSpanIdent().getId()){
				children.add(element);
			}
		}
		return children;
	}	
	
	private InvocationSequenceData getInvocationSequenceDataWithSpanID(long spanIdentID, List<InvocationSequenceData> listInvocationSequenceData){
		if(listInvocationSequenceData == null){
			return null;
		}
		for (InvocationSequenceData sequenceData : listInvocationSequenceData) {
			if(sequenceData.getSpanIdent() != null && sequenceData.getSpanIdent().getId() == spanIdentID){
				return sequenceData;				
			}
		}
		return null;
	}
	
	private PlatformIdent getPlatformIdentWithID(long platformIdentID, List<PlatformIdent> listPlatformIdents){
		if(listPlatformIdents == null){
			return null;
		}
		for (PlatformIdent platformIdent : listPlatformIdents) {
			if(platformIdent.getId() == platformIdentID){
				return platformIdent;				
			}
		}
		return null;
	}
}
