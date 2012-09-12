package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.jdi.event.Event;
import com.sun.jdi.request.EventRequest;

/**
 * Inspired by java.util.Observable.
 */
public class Observable {
	private boolean changed = false;
	private List<EventObserver> obs;
	private Map<String, List<EventObserver>> m;
	
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

	public void notifyObservers(Event arg) {
		if (!changed) {
			return;
		}
		setChanged(false);
		
		for (EventObserver observer : obs) {
			observer.execute(arg);
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
