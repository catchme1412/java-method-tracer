package com.raj.tracer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public abstract class AbstractEventObserver implements EventObserver {

	private EventRequestCriteria eventRequestCriteria;

	private PrintStream printStream;
	
	public AbstractEventObserver () {
		try {
			File file = new File("/tmp/test.log");
			printStream = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public AbstractEventObserver(EventRequestCriteria eventRequestCriteria) {
		this.eventRequestCriteria = eventRequestCriteria;
	}

	@Override
	public EventRequestCriteria getEventRequestCriteria() {
		return eventRequestCriteria;
	}

	@Override
	public void log(String msg) {
//		printStream.append(msg);
//		printStream.flush();
		System.out.println(msg);
	}
}
