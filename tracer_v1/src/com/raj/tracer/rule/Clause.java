package com.raj.tracer.rule;

public abstract class Clause {

	public Object object;

	public Clause(Object object) {
		this.object = object;
	}

	@Override
	public boolean equals(Object other) {
		return this.equals(other);
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}
}
