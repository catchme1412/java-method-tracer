package com.raj.tracer.core;

import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

public abstract class EventRequestCriteria {

	private int supsendPolicy;

	public EventRequestCriteria() {
		supsendPolicy = EventRequest.SUSPEND_NONE;
	}

	public abstract EventRequest createEventRequest(EventRequestManager eventRequestManager);

	public int getSupsendPolicy() {
		return supsendPolicy;
	}

	public void setSupsendPolicy(int supsendPolicy) {
		this.supsendPolicy = supsendPolicy;
	}

}
