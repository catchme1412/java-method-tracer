package com.raj.tracer.core;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.MethodEntryRequest;

public class MethodEntryObserver extends AbstractEventObserver {

	public MethodEntryObserver(MethodEntryRequestCriteria methodEntryRequestCriteria) {
		super(methodEntryRequestCriteria.getOnEventAction());
	}

	@Override
	public void execute(Event event) {
		getOnEventAction().execute(event);
	}

	@Override
	public boolean isMatch(Event event) {
		if (event.request() instanceof MethodEntryRequest) {
			return true;
		}
		return false;
	}

}
