package com.raj.tracer.core;

import com.raj.tracer.core.event.EventManager;
import com.raj.tracer.core.event.EventObserver;
import com.raj.tracer.core.event.EventRequestCriteria;
import com.raj.tracer.rule.Action;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public class ThreadStartRequestCriteria extends EventRequestCriteria {


	public ThreadStartRequestCriteria(Action onEventAction) {
		super(onEventAction);
	}

	public ThreadStartRequestCriteria(String inclusionFilter[], Action onEventAction) {
		super(onEventAction);
	}

	@Override
	public EventRequest createEventRequest(EventRequestManager eventRequestManager) {
		return eventRequestManager.createThreadStartRequest();
	}

	public void fire(EventManager eventManager) {
		eventManager.fireThreadStartRequest();
	}

	@Override
	public EventObserver getEventObserver() {
		return new ThreadStartEventObserver(this);
	}
}
