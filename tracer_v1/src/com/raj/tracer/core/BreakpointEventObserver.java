package com.raj.tracer.core;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.request.MethodEntryRequest;

public class BreakpointEventObserver extends AbstractEventObserver {

	public BreakpointEventObserver(BreakpointRequestCriteria breakpointRequestCriteria) {
		super(breakpointRequestCriteria);
	}

	@Override
	public void execute(Event e) {
		System.out.println("BreakpointEventObserver Entry observer:" + e);
	}

	@Override
	public boolean isMatch(Event event) {
		if (event instanceof BreakpointEvent) {

			return true;
		}
		return false;
	}

}
