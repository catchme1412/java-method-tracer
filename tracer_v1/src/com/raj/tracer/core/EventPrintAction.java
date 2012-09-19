package com.raj.tracer.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.raj.tracer.rule.Action;
import com.sun.jdi.event.Event;


public class EventPrintAction implements Action {

	private static Logger logger;

	static {
		try {
			logger = Logger.getLogger(EventPrintAction.class.getName());
			FileHandler fileTxt = new FileHandler("/tmp/tracer.log");
			logger.addHandler(fileTxt);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(Event event) {
//		System.out.println("Action:"+ event);
		logger.info("Action:"+ event);
	}

}
