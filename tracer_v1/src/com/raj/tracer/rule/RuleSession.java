package com.raj.tracer.rule;

import java.util.LinkedHashMap;
import java.util.Map;

public class RuleSession {

	public static final String DEFAULT_RULE_GROUP = "DEFAULT";

	private Map<String, RuleGroup> ruleGroupMap;

	public RuleSession() {
		ruleGroupMap = new LinkedHashMap<String, RuleGroup>();
		ruleGroupMap.put(DEFAULT_RULE_GROUP, new RuleGroup());
	}

	public void addRule(String ruleGroupName, Rule rule) {
		RuleGroup rg = ruleGroupMap.get(ruleGroupName);
		rg.add(rule);
	}

	public void addRuleGroup(String ruleGroupName, RuleGroup rule) {
		ruleGroupMap.put(ruleGroupName, rule);
	}

	/**
	 * @return the ruleGroupMap
	 */
	public LinkedHashMap<String, RuleGroup> getRuleGroupMap() {
		return (LinkedHashMap<String, RuleGroup>) ruleGroupMap;
	}

	/**
	 * @param ruleGroupMap
	 *            the ruleGroupMap to set
	 */
	public void setRuleGroupMap(LinkedHashMap<String, RuleGroup> ruleGroupMap) {
		this.ruleGroupMap = ruleGroupMap;
	}

	public RuleGroup getRuleGroup(String defaultRuleGroup) {
		return ruleGroupMap.get(defaultRuleGroup);
	}

	public RuleGroup addRule(Rule rule) {
		RuleGroup rg = getRuleGroup(DEFAULT_RULE_GROUP);
		rg.add(rule);
		return rg;
	}

}
