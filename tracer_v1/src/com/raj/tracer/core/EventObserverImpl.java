package com.raj.tracer.core;

import com.sun.jdi.event.Event;

public class EventObserverImpl implements EventObserver {

	@Override
	public void execute(Event e) {
		System.out.println("Event observer:" + e);
	}

}
