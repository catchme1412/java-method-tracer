package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.event.Event;

/**
 * Inspired by java.util.Observable.
 */
public class Observable {
	private boolean changed = false;
	private List<EventObserver> obs;

	public Observable() {
		obs = new ArrayList<EventObserver>();
	}

	public void addObserver(EventObserver o) {
		if (o == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		obs.add(o);
	}

	public void deleteObserver(EventObserver o) {
		obs.remove(o);
	}

	public void notifyObservers(Event event) {
		if (!changed) {
			return;
		}
		setChanged(false);

		for (EventObserver observer : obs) {
			if (observer.isMatch(event)) {
				observer.execute(event);
				observer.performAction(event);
			}
		}
	}

	public void deleteObservers() {
		obs.clear();
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
