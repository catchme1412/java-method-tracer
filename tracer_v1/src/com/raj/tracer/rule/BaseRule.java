package com.raj.tracer.rule;

public class BaseRule implements Rule {

	private Clause clause;

	private Action action;

	public BaseRule(Clause clause, Action action) {
		this.clause = clause;
		this.setAction(action);
	}

	public boolean isMatched(Clause criteria) {
		return clause.equals(criteria);
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public Clause getClause() {
		return clause;
	}

	@Override
	public int executionCount() {
		return 1;
	}

}
