package com.raj.tracer.rule;

import com.raj.tracer.core.event.EventRequestCriteria;
import com.sun.jdi.event.Event;

public interface Action {

	public void execute(Event event);

	public abstract EventRequestCriteria getEventRequestCriteria();
}
