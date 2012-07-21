package com.raj.tracer.rule;

import com.sun.jdi.request.EventRequest;


public class FireMethodEntryAction implements Action {

	private String message;
	
	private EventRequest r;
	
	public FireMethodEntryAction (EventRequest r) {
		message = "";
		this.r = r;
		
	}
	public void execute(RuleSession input) {
		if (r.suspendPolicy() != EventRequest.SUSPEND_NONE)
		r.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		r.setEnabled(true);
		System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
