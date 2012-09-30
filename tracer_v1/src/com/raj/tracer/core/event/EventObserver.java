package com.raj.tracer.core.event;

import com.sun.jdi.event.Event;

/**
 * Inspired by java.util.Observer.
 * 
 * @author rkv
 * 
 */
public interface EventObserver {

	public void execute(Event e);
	
	public void performAction(Event e);

	public boolean isMatch(Event event);

	public EventRequestCriteria getEventRequestCriteria();

	public void log(String msg);

}
