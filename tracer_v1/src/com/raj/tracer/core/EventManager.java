package com.raj.tracer.core;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
	private long eventCount;
	private long startTime;
//	private Observable eventObservable;
	private Queue<Event> eventList;

	public EventManager(EventRequestManager eventRequestManager) {
		eventList = new LinkedBlockingQueue<Event>();
		this.eventRequestManager = eventRequestManager;
//		eventObservable = new Observable();
//		EventPrintAction eventPrintAction = new EventPrintAction();
//		ThreadStartRequestCriteria t = new ThreadStartRequestCriteria(eventPrintAction);
//		eventObservable.addObserver(new ThreadStartEventObserver(t));
//		BreakpointRequestCriteria b = new BreakpointRequestCriteria("java.lang.Thread", 673, eventPrintAction);
//		eventObservable.addObserver(new BreakpointEventObserver(b));
		startTime = System.currentTimeMillis();
	}

	public void addObserver(EventObserver eventObserver) {
//		eventObservable.addObserver(eventObserver);
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
		for (int i = lineNumber - 1; i++ < lineNumber + 100;) {
			try {
				r = eventRequestManager.createBreakpointRequest(f.locationsOfLine(i).get(0));
				r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
				return r;
			} catch (Exception e) {
			}
		}
		return r;
	}

	public ThreadStartRequest createThreadStartRequest() {
		ThreadStartRequest r = eventRequestManager.createThreadStartRequest();
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		return r;
	}

	public MethodEntryRequestCriteria fireMethodEntryRequest(String classFilter) {
		MethodEntryRequestCriteria eventRequestCriteria = new MethodEntryRequestCriteria(classFilter, null);
		eventRequestCriteria.createEventRequest(eventRequestManager).enable();
		return eventRequestCriteria;
	}

	public void fireThreadStartRequest() {
		createThreadStartRequest().setEnabled(true);
	}

	public void fireBreakpointEventRequest(String clazz, int lineNumber) throws AbsentInformationException {
		createBreakpointRequest(clazz, lineNumber).enable();
	}

	public void removeMethodEntryRequest(String classFilter) {
		eventRequestManager.methodEntryRequests().remove(createMethodEntryRequest(classFilter));
	}

}
