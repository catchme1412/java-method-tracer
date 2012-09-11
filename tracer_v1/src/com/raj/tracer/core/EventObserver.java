package com.raj.tracer.core;

import com.sun.jdi.event.Event;

/**
 * Inspired by java.util.Observer.
 * @author rkv
 *
 */
public interface EventObserver {
	public void execute(Event e);
}
