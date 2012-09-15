package com.raj.tracer.core;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.MethodEntryRequest;

public abstract class AbstractEventObserver implements EventObserver {
	
	private EventRequestCriteria eventRequestCriteria;

	public AbstractEventObserver(EventRequestCriteria eventRequestCriteria) {
		this.eventRequestCriteria = eventRequestCriteria;
	}

	@Override
	public EventRequestCriteria getEventRequestCriteria() {
		return eventRequestCriteria;
	}

}
