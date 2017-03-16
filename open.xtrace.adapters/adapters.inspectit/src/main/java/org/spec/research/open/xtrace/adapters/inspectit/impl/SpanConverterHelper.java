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

class SpanConverterHelper {
	
	private static final String REQUEST = "http.request.";
	private static final String RESPONSE = "http.response.";
	private static final String URL = "http.url";
	
	protected IITAbstractCallable createCallable(IITSubTraceImpl containingTrace, IITAbstractNestingCallable parent, IITTraceImpl trace, MobileTraceData traceData) {
		
		List<Span> spans = traceData.getSpans();

		// TraceID is equal usecaseID
		long traceID = spans.get(0).getSpanIdent().getTraceId();
		
		// Operationname is equal UsecaseDescription
		String operationName = "OperationName";
		
		InvocationSequenceData data = new InvocationSequenceData();
		data.setTimeStamp(spans.get(0).getTimeStamp());
		data.setDuration(spans.get(0).getDuration());
		IITMobileMetaMeasurementCallable rootCallable = new IITMobileMetaMeasurementCallable(containingTrace, parent, traceID, operationName, null, data);
		
		System.out.println("Root: " + spans.get(0));
		
		
		addChildren(trace, traceData, containingTrace, rootCallable, spans.get(0), traceData.getMeasurements());
		

		
		/*
		if(mobileUsecase.getMeasurements() != null){
			for (MobilePeriodicMeasurement measurement : mobileUsecase.getMeasurements()) {
				IITAbstractCallable callable = new IITMobileMetaMeasurementCallable(containingTrace, parent, child.getTags());
				rootCallable.addChild(callable);
			}
		}
		*/
		/*
		if(mobileUsecase.getRemoteCalls() != null){
			for (RemoteCallMeasurementContainer remoteCallContainer : mobileUsecase.getRemoteCalls()) {
				
				RemoteCallMeasurement remoteCallMeasurement = null;
				if(remoteCallContainer.getRequestMeasurement() == null){
					if(remoteCallContainer.getResponseMeasurement() != null){
						remoteCallMeasurement = remoteCallContainer.getResponseMeasurement();
					}
				} else {
					remoteCallMeasurement = remoteCallContainer.getRequestMeasurement();
				}
				if(remoteCallMeasurement == null){
					continue;
				}
				
				InvocationSequenceData sequenceData = trace.getInvocationSequenceDataWithSpanID(remoteCallMeasurement.getRemoteCallID());
				
				if(sequenceData == null){
					continue;
				}
				
				IITRemoteInvocation remoteInvocation = new IITRemoteInvocation(sequenceData, containingTrace, parent);
				remoteInvocation.setTargetSubTrace(new IITSubTraceImpl(trace, sequenceData, trace.getPlatformIdentWithID(sequenceData.getPlatformIdent())));
				remoteInvocation.setRequestMeasurement(new IITMobileRemoteMeasurement(remoteCallContainer.getRequestMeasurement()));
				remoteInvocation.setResponseMeasurement(new IITMobileRemoteMeasurement(remoteCallContainer.getResponseMeasurement()));
				rootCallable.addChild(remoteInvocation);
			}
		}
	*/
		return rootCallable;
	}
	
	private void addChildren(IITTraceImpl trace, MobileTraceData traceData, IITSubTraceImpl containingTrace, IITMobileMetaMeasurementCallable parent, Span parentSpan, List<MobilePeriodicMeasurement> parentMeasurements){

		Timestamp startTime = parentSpan.getTimeStamp();
		if(startTime == null){
			throw new IllegalArgumentException("Timestamp of span with id " + parentSpan.getSpanIdent().getId() + " is null.");
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
				IITMobileMetaMeasurementCallable usecaseCallable = new IITMobileMetaMeasurementCallable(containingTrace, parent, parent.getUseCaseID().get(), parent.getUseCaseName().get(), null, data);
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
	
	/*
	private static class MobilePeriodicMeasurementComparator implements Comparator<MobilePeriodicMeasurement> {

		@Override
		public int compare(MobilePeriodicMeasurement arg0, MobilePeriodicMeasurement arg1) {
			long dif = arg0.getTimestamp() - arg1.getTimestamp();
			if (dif > 0){
				return 1;
			} else if(dif < 0){
				return -1;
			}
			return 0;
		}		
	}
	*/
}
