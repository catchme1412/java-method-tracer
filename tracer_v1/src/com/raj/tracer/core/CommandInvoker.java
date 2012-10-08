package com.raj.tracer.core;

import com.raj.tracer.core.event.EventManager;

public class CommandInvoker {

	private EventManager eventManager;

	public CommandInvoker(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public void execute(Command cmd) {
		cmd.execute();
	}

	public void execute(String[] cmd) {
		switch (cmd[0]) {
		case "stop":
			eventManager.stop();
			break;
		case "breakpoint":
			
			break;
		default:
			System.out.println("Unknow command");
		}

	}

}
