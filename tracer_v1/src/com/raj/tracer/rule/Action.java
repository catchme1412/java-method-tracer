package com.raj.tracer.rule;

import java.util.Observer;



public interface Action extends Observer {

	public void execute(RuleSession input) ;
}
