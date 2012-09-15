package com.raj.tracer.core;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

	private Observable eventObservable;

	public EventManager(EventRequestManager eventRequestManager) {
		this.eventRequestManager = eventRequestManager;
		eventObservable = new Observable();
		MethodEntryRequestCriteria methodEntryRequestCriteria;
		methodEntryRequestCriteria = new MethodEntryRequestCriteria("java.lang.*");
		eventObservable.addObserver(new MethodEntryObserver(methodEntryRequestCriteria));
		ScriptEngine se = new ScriptEngineManager().getEngineByExtension("js");
	}

	public void addObserver(EventObserver eventObserver) {
		eventObservable.addObserver(eventObserver);
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

	public MethodEntryRequestCriteria fireMethodEntryRequest(String classFilter) {
		MethodEntryRequestCriteria eventRequestCriteria = new MethodEntryRequestCriteria(classFilter);
		eventRequestCriteria.createEventRequest(eventRequestManager).enable();
		return eventRequestCriteria;
	}

	public void fireThreadStartRequest() {
		createThreadStartRequest().enable();
	}

	public void removeMethodEntryRequest(String classFilter) {
		eventRequestManager.methodEntryRequests().remove(createMethodEntryRequest(classFilter));
	}

}
