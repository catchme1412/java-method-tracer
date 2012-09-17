package com.raj.tracer.core;

import java.util.List;

import com.raj.tracer.rule.Action;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public class BreakpointRequestCriteria extends EventRequestCriteria {

	private String className;
	private int lineNumber;

	public BreakpointRequestCriteria(String className, int lineNumber, Action onEventAction) {
		super(onEventAction);
		this.className = className;
		this.lineNumber = lineNumber;
	}

	@Override
	public EventRequest createEventRequest(EventRequestManager eventRequestManager) {
		BreakpointRequest r = null;
		try {
			List<ReferenceType> ref = eventRequestManager.virtualMachine().classesByName(className);
			ReferenceType f = ref.get(0);
			r = eventRequestManager.createBreakpointRequest(f.locationsOfLine(lineNumber).get(0));
			r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		} catch (AbsentInformationException e) {
			e.printStackTrace();
		}
		return r;
	}

}
