package com.raj.tracer.rule;

public class Clause {

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
