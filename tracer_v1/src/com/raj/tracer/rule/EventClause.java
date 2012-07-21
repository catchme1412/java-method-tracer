package com.raj.tracer.rule;



public class EventClause extends Clause {

	
	public EventClause(Object object) {
		super(object);
	}

	
	public boolean equals(Object other) {
		return ((EventClause)other).object.getClass().getName().indexOf(object.toString()) > -1;
	}
	
	public int hashCode() {
		return object.hashCode();
	}
}
