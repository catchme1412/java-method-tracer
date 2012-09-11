package com.raj.tracer.rule;

import com.sun.jdi.event.Event;




public class EventClause extends Clause {

	
	public EventClause(Object object) {
		super(object);
	}

	
	@Override
	public void execute(Event e) {
		System.out.println("Observable called execute "+ e);
	}

}
