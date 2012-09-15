package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

public class RuleEngine {

	private Map<EventRequest, List<EventRequest>> ruleMap;

	public RuleEngine() {
		ruleMap = new HashMap<>();
	}

	public void fireRule(Event e) {
		System.out.println(e);
		List<EventRequest> ruleList = ruleMap.get(e.request());
		System.out.println(e);
	}

	public void addRule(EventRequest req) {
		List<EventRequest> ruleList = ruleMap.get(req);
		// if breakpoint on string :83 then fire method trace event for package
		// java.lang.
		// if thread creation event then print stack trace.
		if (ruleList == null) {
			ruleList = new ArrayList<>();
			ruleMap.put(req, ruleList);
		}
		ruleList.add(req);
	}

}
