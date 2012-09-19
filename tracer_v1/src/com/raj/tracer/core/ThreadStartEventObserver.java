package com.raj.tracer.core;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.MethodEntryRequest;

public class ThreadStartEventObserver extends AbstractEventObserver {

	public ThreadStartEventObserver(ThreadStartRequestCriteria methodEntryRequestCriteria) {
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
