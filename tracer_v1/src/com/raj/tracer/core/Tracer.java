package com.raj.tracer.core;

/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

import com.raj.tracer.core.event.EventManager;
import com.raj.tracer.core.event.EventRequestCriteria;
import com.raj.tracer.core.event.Observable;
import com.raj.tracer.rule.Action;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.EventRequest;
import com.sun.tools.jdi.SocketAttachingConnector;

/**
 * Implementation of Producer Consumer pattern. The class creates separate
 * threads for producer and consumer and waits until it is done. Any exception
 * occurs at producer or consumer is captured by this class itself.
 * 
 * @author rkvelayudhan
 * 
 */
public class Tracer extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Tracer.class.getName());

	private EventQueue eventQueue;
	private String machine;
	private String port;
	private VirtualMachine remoteVirtualMachine;
	private EventManager eventManager;
	private Observable eventObservable;
	private static boolean done;
	private Queue<Event> localQueue;

	private Producer producer;

	private Consumer consumer;

	public Tracer() {
		new Thread(new CommandModule()).start();
	}

	class Producer extends RecursiveTask<Collection<Event>> {
		private static final long serialVersionUID = 1L;

		private final EventQueue jvmRemoteEventQueue;
		private Queue<Event> localQueue;
		boolean isWaiting;

		Producer(EventQueue queue, Queue<Event> localQueue) {
			this.jvmRemoteEventQueue = queue;
			this.localQueue = localQueue;
		}

		@Override
		protected Collection<Event> compute() {
			System.out.println("Staring producer");

			EventSet eventSet = null;
			while (!done) {

				try {
					eventSet = null;
					isWaiting = true;
					eventSet = jvmRemoteEventQueue.remove(10000);
					isWaiting = false;
					if (eventSet != null) {
						localQueue.addAll(eventSet);
					}
				} catch (Exception e) {
				} finally {
					// if any thread is suspended invoke resume
					if (eventSet != null && eventSet.suspendPolicy() != EventRequest.SUSPEND_NONE) {
						eventSet.resume();
					}
				}
			}
			return null;
		}
	}

	class Consumer extends RecursiveTask<Collection<Event>> {
		private static final long serialVersionUID = 1L;
		private final Queue<Event> localQueue;

		Consumer(Queue<Event> queue) {
			this.localQueue = queue;
			eventObservable = new Observable();
			Action event = null;//new EventPrintAction();
			
			Action onEventAction = new EventPrintAction();
			EventRequestCriteria methodEntryRequest = new MethodEntryRequestCriteria("java.lang.*", onEventAction );
			event = new EventExecutorAction(methodEntryRequest , eventManager);
			
			ThreadStartRequestCriteria t = new ThreadStartRequestCriteria(event);
			eventObservable.addObserver(new ThreadStartEventObserver(t));
			t.fire(eventManager);
		}

		@Override
		protected Collection<Event> compute() {
			while (!done) {
				if (!localQueue.isEmpty()) {
					Event e = localQueue.remove();
					 System.out.println("Consumer:" + e);
					eventObservable.notifyObservers(e);
				}
			}
			return localQueue;
		}
	}

	class CommandModule implements Runnable {
		private Scanner readUserInput;

		CommandModule() {
		}

		@Override
		public void run() {
			while (!done) {
				String st = null;
				System.out.print("Waiting for input:");
				readUserInput = new Scanner(System.in);
				st = readUserInput.nextLine();
				ServiceLoader<CommandModule> cmd = ServiceLoader.load(CommandModule.class);
				CommandModule c = cmd.iterator().next();
				// stop

				System.out.println(">>>>>>>>>>>>>>>>>>>readed the command:" + st);
			}
		}
	}

	public static void main(String[] args) throws AbsentInformationException {
		Tracer job = new Tracer();
		job.setMachine("localhost");
		job.setPort("8000");
		job.setLocalQueue(new LinkedBlockingQueue<Event>());
		job.hook();
		job.initEventManager();
		job.fireMethodEntry("java.lang.*");
//		job.fireThreadStartEvent();
//		job.fireBreakPoint("java.lang.Thread", 1480);
		job.resume();
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.invoke(job);
		job.join();
		Tracer.addShutdownHook();
		System.out.println("OVER");
	}

	private void initEventManager() {
		eventManager = new EventManager(remoteVirtualMachine.eventRequestManager());
		eventQueue = remoteVirtualMachine.eventQueue();
	}

	public void resume() {
		remoteVirtualMachine.resume();
	}

	@Override
	protected void compute() {

		producer = new Producer(eventQueue, localQueue);
		consumer = new Consumer(localQueue);
		consumer.fork();
		producer.fork();
		try {
			producer.get();
			consumer.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	private SocketAttachingConnector findSocketAttachingConnector() {
		List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator<Connector> iter = connectors.iterator();
		while (iter.hasNext()) {
			Connector connector = iter.next();
			logger.info("connector:" + connector);
			if ("com.sun.jdi.SocketAttach".equals(connector.name())) {
				return (SocketAttachingConnector) connector;
			}
		}
		throw new Error("No socket connector found!");
	}

	public VirtualMachine hook() {
		SocketAttachingConnector connector = findSocketAttachingConnector();

		Map<String, Connector.Argument> connectorArguments = connector.defaultArguments();
		setConnectionInfo(connectorArguments);

		try {
			remoteVirtualMachine = connector.attach(connectorArguments);
			remoteVirtualMachine.setDebugTraceMode(VirtualMachine.TRACE_NONE);
		} catch (IOException exc) {
			throw new Error("Unable to hook to target JVM: [" + machine + ":" + port + "] due to " + exc
					+ ".\n If the target JVM is running "
					+ "please make sure no other debug is already hooked for the same JVM.");

		} catch (IllegalConnectorArgumentsException exc) {
			throw new Error("Unable to hook due to error: " + exc);
		}

		return remoteVirtualMachine;
	}

	private void setConnectionInfo(Map<String, Connector.Argument> connectorArguments) {
		Connector.Argument host = connectorArguments.get("hostname");
		Connector.Argument portArg = connectorArguments.get("port");
		Connector.Argument timeout = connectorArguments.get("timeout");
		timeout.setValue(String.valueOf(30));

		host.setValue(machine);
		portArg.setValue(port);
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the localQueue
	 */
	public Queue<Event> getLocalQueue() {
		return localQueue;
	}

	/**
	 * @param localQueue
	 *            the localQueue to set
	 */
	public void setLocalQueue(Queue<Event> localQueue) {
		this.localQueue = localQueue;
	}

	public void fireMethodEntry(String classFilter) {
		eventManager.fireMethodEntryRequest(classFilter);
	}

	public void fireBreakPoint(String className, int lineNumber) throws AbsentInformationException {
		eventManager.fireBreakpointEventRequest(className, lineNumber);
	}

	public void fireThreadStartEvent() {
		eventManager.fireThreadStartRequest();
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("\n\nStarting clean shutdown...");
				try {
					// tracer.stop();
					done = true;
					logger.info("\n\nClean shutdown complete.");

				} catch (Throwable e) {
					logger.warning("Clean shutdown failed due to " + e.getMessage());
				} finally {
				}
			}
		});
	}

	public void stop() {
		remoteVirtualMachine.dispose();
		done = true;
		producer.cancel(false);
		consumer.cancel(false);
		this.complete(null);
	}

}
