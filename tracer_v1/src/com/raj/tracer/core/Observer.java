package com.raj.tracer.core;

import com.raj.tracer.rule.Clause;

/**
 * Inspired by java.util.Observer.
 * @author rkv
 *
 */
public interface Observer {
	public void execute(Clause e);
}
