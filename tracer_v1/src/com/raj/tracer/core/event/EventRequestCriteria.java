package com.raj.tracer.core.event;

import com.raj.tracer.rule.Action;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public abstract class EventRequestCriteria {

	private int supsendPolicy;
	
	private Action onEventAction;

	public EventRequestCriteria(Action onEventAction) {
		this.onEventAction = onEventAction;
		supsendPolicy = EventRequest.SUSPEND_NONE;
	}

	public abstract EventRequest createEventRequest(EventRequestManager eventRequestManager);

	public int getSupsendPolicy() {
		return supsendPolicy;
	}

	public void setSupsendPolicy(int supsendPolicy) {
		this.supsendPolicy = supsendPolicy;
	}

	public Action getOnEventAction() {
		return onEventAction;
	}

	public void setOnEventAction(Action onEventAction) {
		this.onEventAction = onEventAction;
	}
	
	public abstract void fire(EventManager eventManager);

}
