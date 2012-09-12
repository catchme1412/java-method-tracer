package com.raj.tracer.core;

import com.sun.jdi.event.Event;
import com.sun.mirror.type..ClassType;

/**
 * Inspired by java.util.Observer.
 * @author rkv
 *
 */
public interface EventObserver {
	public void execute(Event e);
	
	public EnumType getEventType();
}
