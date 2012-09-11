package com.raj.tracer.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raj.tracer.rule.EventClause;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ThreadStartRequest;

public class EventManager {

	private EventRequestManager eventRequestManager;
	
	private Map<String, EventRequest> requestMap;

	private Observable eventObservable;
	
	
	
	public EventManager(EventRequestManager eventRequestManager) {
		requestMap = new HashMap<>();
		this.eventRequestManager = eventRequestManager;
		eventObservable = new Observable();
		eventObservable.addObserver(new EventClause(new Object()));
	}

	public MethodEntryRequest createMethodEntryRequest(String classFilter) {
		MethodEntryRequest r = eventRequestManager.createMethodEntryRequest();
		r.addClassFilter(classFilter);
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		return r;
	}

	public MethodExitRequest createMethodExitRequest(String classFilter) {
		MethodExitRequest r = eventRequestManager.createMethodExitRequest();
		r.addClassFilter(classFilter);
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		return r;
	}

	public BreakpointRequest createBreakpointRequest(String clazz, int lineNumber) throws AbsentInformationException {
		BreakpointRequest r = null;
		List<ReferenceType> ref = eventRequestManager.virtualMachine().classesByName(clazz);
		ReferenceType f = ref.get(0);
		r = eventRequestManager.createBreakpointRequest(f.locationsOfLine(lineNumber).get(0));
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		return r;
	}

	public ThreadStartRequest createThreadStartRequest() {
		ThreadStartRequest r = eventRequestManager.createThreadStartRequest();
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		return r;
	}

	public void notifyEvent(Event event) {
		eventObservable.setChanged(true);
		eventObservable.notifyObservers(event);
	}
	
	public void fireMethodEntryRequest(String classFilter) {
		createMethodEntryRequest(classFilter).enable();
	}
	
	public void fireThreadStartRequest() {
		createThreadStartRequest().enable();
	}
	
	public void removeMethodEntryRequest(String classFilter) {
		eventRequestManager.methodEntryRequests().remove(createMethodEntryRequest(classFilter));
	}
	
}
