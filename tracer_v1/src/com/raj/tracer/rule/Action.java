package com.raj.tracer.rule;

import com.raj.tracer.core.event.EventObserver;
import com.sun.jdi.event.Event;

public interface Action {

	public void execute(Event event);

	public abstract EventObserver getEventObserver();
}
