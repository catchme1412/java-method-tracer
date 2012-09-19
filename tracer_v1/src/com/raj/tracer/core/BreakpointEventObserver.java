package com.raj.tracer.core;

import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;

public class BreakpointEventObserver extends AbstractEventObserver {

	public BreakpointEventObserver(BreakpointRequestCriteria breakpointRequestCriteria) {
		super(breakpointRequestCriteria);
	}

	@Override
	public void execute(Event e) {
		log("BreakpointEventObserver Entry observer:" + e);
	}

	@Override
	public boolean isMatch(Event event) {
		if (event instanceof BreakpointEvent) {

			return true;
		}
		return false;
	}

}
