package com.raj.tracer.core;

import com.raj.tracer.rule.Action;
import com.sun.jdi.event.Event;


public class EventPrintAction implements Action {

	public void execute(Event event) {
		System.out.println("Action:"+ event);
	}

}
