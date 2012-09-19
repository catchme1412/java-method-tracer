package com.raj.tracer.core;

import com.raj.tracer.rule.Action;
import com.sun.istack.internal.logging.Logger;
import com.sun.jdi.event.Event;


public abstract class AbstractEventObserver implements EventObserver {

	private EventRequestCriteria eventRequestCriteria;

	private Action onEventAction;
	
	private Logger logger = Logger.getLogger(getClass());
	
	public AbstractEventObserver (Action onEventAction) {
		this.onEventAction = onEventAction;
	}

	public AbstractEventObserver(EventRequestCriteria eventRequestCriteria) {
		this.eventRequestCriteria = eventRequestCriteria;
	}

	@Override
	public EventRequestCriteria getEventRequestCriteria() {
		return eventRequestCriteria;
	}
	
	@Override 
	public void performAction(Event event) {
		onEventAction.execute(event);
	}

	@Override
	public void log(String msg) {
		logger.info(msg);
	}

	public Action getOnEventAction() {
		return onEventAction;
	}

	public void setOnEventAction(Action onEventAction) {
		this.onEventAction = onEventAction;
	}
}
