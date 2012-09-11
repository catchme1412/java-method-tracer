package com.raj.tracer.rule;

import com.raj.tracer.core.EventObserver;


public abstract class Clause implements EventObserver {

	public Object object;
	
	public Clause (Object object) {
		this.object = object;
	}
	
	public boolean equals(Object other) {
		return this.equals(other);
	}
	
	public int hashCode() {
		return object.hashCode();
	}
}
