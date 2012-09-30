package com.raj.tracer.core;

import com.raj.tracer.core.event.EventManager;
import com.raj.tracer.core.event.EventRequestCriteria;
import com.raj.tracer.rule.Action;
import com.sun.jdi.event.Event;


public class EventExecutorAction implements Action {

	private EventRequestCriteria eventRequestCriteria;
	private EventManager eventManager;
	
	public EventExecutorAction (EventRequestCriteria eventRequestCriteria, EventManager eventManager) {
		this.setEventRequestCriteria(eventRequestCriteria);
		this.eventManager  = eventManager;
	}

	@Override
	public void execute(Event event) {
		System.out.println("FIRING:" + getEventRequestCriteria());
		getEventRequestCriteria().fire(eventManager);
	}

	@Override
	public EventRequestCriteria getEventRequestCriteria() {
		return eventRequestCriteria;
	}

	public void setEventRequestCriteria(EventRequestCriteria eventRequestCriteria) {
		this.eventRequestCriteria = eventRequestCriteria;
	}

}
