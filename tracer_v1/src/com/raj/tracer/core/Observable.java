package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.List;

import com.raj.tracer.rule.Clause;

/**
 * Inspired by java.util.Observable.
 */
public class Observable {
	private boolean changed = false;
	private List<Observer> obs;

	public Observable() {
		obs = new ArrayList<Observer>();
	}

	public void addObserver(Observer o) {
		if (o == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		obs.add(o);
	}

	public void deleteObserver(Observer o) {
		obs.remove(o);
	}

	public void notifyObservers(Clause arg) {
		if (!changed) {
			return;
		}
		setChanged(false);
		for (Observer observer : obs) {
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
