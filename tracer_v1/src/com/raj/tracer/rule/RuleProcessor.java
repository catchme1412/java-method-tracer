package com.raj.tracer.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleProcessor {

	private RuleSession session;

	public RuleProcessor() {
		session = new RuleSession();
	}

	public void addRule(Rule rule) {
		session.addRule(rule);
	}

	public RuleGroup getApplicableRules(String ruleGroupName) {
		return session.getRuleGroup(ruleGroupName);
	}

	public void executeRules(String ruleGroupName, Clause ruleInput) {

		List<Action> rules = fireRules(ruleGroupName, ruleInput);
		for (Action a : rules) {
			// a.execute(session);
		}
	}

	public List<Action> fireRules(String ruleGroupName, Clause ruleInput) {
		List result = new ArrayList();
		RuleGroup applicableRuleGroup = getApplicableRules(ruleGroupName);
		for (Rule rule : applicableRuleGroup.getRules()) {
			if (rule.isMatched(ruleInput)) {
				result.add(rule.getAction());
			}
		}
		return result;
	}

	public static void main(String[] args) {

	}

	public void executeRules(Clause c) {
		executeRules(RuleSession.DEFAULT_RULE_GROUP, c);
	}

}
