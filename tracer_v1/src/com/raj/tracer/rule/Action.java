package com.raj.tracer.rule;

import com.sun.jdi.event.Event;

public interface Action {

	public void execute(Event event);
}
