package com.raj.tracer.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleGroup {

	private String name;

	private List<Rule> ruleList;

	public RuleGroup() {
		ruleList = new ArrayList<Rule>();
	}

	public String getName() {
		return name;
	}

	public void add(Rule rule) {
		ruleList.add(rule);
	}

	public List<Rule> getRules() {
		return ruleList;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
