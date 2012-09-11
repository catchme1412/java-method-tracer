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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

import com.raj.tracer.rule.BaseRule;
import com.raj.tracer.rule.EventClause;
import com.raj.tracer.rule.PrintStackTraceAction;
import com.raj.tracer.rule.RuleProcessor;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ThreadStartRequest;
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

	private static Logger logger = Logger.getLogger(Tracer.class.getName());

	private static final long serialVersionUID = 1L;
	private EventQueue eventQueue;
	private String machine;
	private String port;
	private VirtualMachine remoteVirtualMachine;
	private RuleProcessor ruleProcessor;
	private EventManager eventManager;
	private boolean done;
	private Queue<Event> localQueue;

	public Tracer() {
		ruleProcessor = new RuleProcessor();

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
					if (eventSet != null) {
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
		}

		@Override
		protected Collection<Event> compute() {
			while (!done) {
				if (!localQueue.isEmpty()) {
					Event e = localQueue.remove();
					System.out.println("Consumer:" + e);
					eventManager.notifyEvent(e);
				}
			}
			return localQueue;
		}
	}

	public static void main(String[] args) throws AbsentInformationException {
		Tracer job = new Tracer();
		job.setMachine("localhost");
		job.setPort("8000");
		job.setLocalQueue(new LinkedBlockingQueue<Event>());
		job.hook();
		job.getReady();
		job.fireMethodEntry("java.lang.*");
		job.fireThreadStartEvent();
		job.fireBreakPoint();
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.invoke(job);
		job.join();
		Tracer.addShutdownHook();
		System.out.println("OVER");
	}

	private void getReady() {
		eventManager = new EventManager(remoteVirtualMachine.eventRequestManager());
		eventQueue = remoteVirtualMachine.eventQueue();
		remoteVirtualMachine.resume();
	}

	@Override
	protected void compute() {

		Producer p = new Producer(eventQueue, localQueue);
		Consumer c = new Consumer(localQueue);
		c.fork();
		p.fork();

		try {
			p.get();
			c.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	private SocketAttachingConnector findSocketAttachingConnector() {
		List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator<Connector> iter = connectors.iterator();
		while (iter.hasNext()) {
			Connector connector = (Connector) iter.next();
			System.out.println("connector:" + connector);
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
		Connector.Argument host = (Connector.Argument) connectorArguments.get("hostname");
		Connector.Argument portArg = (Connector.Argument) connectorArguments.get("port");
		Connector.Argument timeout = (Connector.Argument) connectorArguments.get("timeout");
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
		ruleProcessor.addRule(new BaseRule(new EventClause("MethodEntry"), new PrintStackTraceAction()));
	}

	public void fireBreakPoint() throws AbsentInformationException {
		BreakpointRequest r = eventManager.createBreakpointRequest("java.lang.String", 2694);
	}

	public void fireThreadStartEvent() {
		eventManager.fireThreadStartRequest();
//		PrintStackTraceAction action = new PrintStackTraceAction();
//		ruleProcessor.addRule(new BaseRule(new EventClause("ThreadStart"), action));
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info("\n\nStarting clean shutdown...");
				try {
					// tracer.stop();
					logger.info("\n\nClean shutdown complete.");
				} catch (Throwable e) {
					logger.warning("Clean shutdown failed due to " + e.getMessage());
				}
			}
		});
	}

	public void stop() {
		remoteVirtualMachine.dispose();
		cancel(true);
	}

}
