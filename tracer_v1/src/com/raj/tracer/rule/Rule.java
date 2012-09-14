package com.raj.tracer.rule;

public interface Rule {

	public Clause getClause();

	public boolean isMatched(Clause criteria);

	public Action getAction();
	
	public int executionCount();

}
