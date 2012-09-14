package com.raj.tracer.core;

import com.raj.tracer.rule.PrintStackTraceAction;
import com.sun.jdi.event.Event;

public class EventObserverImpl implements EventObserver {

	private RuleEngine ruleEngine;
	
	public EventObserverImpl () {
		ruleEngine = new RuleEngine();
		ruleEngine.addRule(null);
	}
	@Override
	public void execute(Event e) {
		System.out.println("Event observer:" + e);
		
		ruleEngine.fireRule(e);
	}

}
