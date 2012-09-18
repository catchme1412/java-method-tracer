package com.raj.tracer.core;

import java.io.PrintStream;

import com.raj.tracer.rule.Action;
import com.sun.jdi.event.Event;


public abstract class AbstractEventObserver implements EventObserver {

	private EventRequestCriteria eventRequestCriteria;

	private Action onEventAction;
	
	private PrintStream printStream;
	
	public AbstractEventObserver (Action onEventAction) {
		this.onEventAction = onEventAction;
//		try {
//			File file = new File("/tmp/test.log");
//			printStream = new PrintStream(file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}

	public AbstractEventObserver(EventRequestCriteria eventRequestCriteria) {
		this.eventRequestCriteria = eventRequestCriteria;
	}

	@Override
	public EventRequestCriteria getEventRequestCriteria() {
		return eventRequestCriteria;
	}
	
	@Override 
	public void performAction(Event event) {
		onEventAction.execute(event);
	}

	@Override
	public void log(String msg) {
//		printStream.append(msg);
//		printStream.flush();
		System.out.println(msg);
	}

	public Action getOnEventAction() {
		return onEventAction;
	}

	public void setOnEventAction(Action onEventAction) {
		this.onEventAction = onEventAction;
	}
}
