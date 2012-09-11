package com.raj.tracer.rule;




public class EventClause extends Clause {

	
	public EventClause(Object object) {
		super(object);
	}

	
	@Override
	public void execute(Clause e) {
		System.out.println("Observable called execute "+ e);
	}

}
