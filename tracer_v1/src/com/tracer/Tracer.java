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
package com.tracer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.model.producerconsumer.ProducerConsumerModel;
import com.model.producerconsumer.TraceStrategy;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.tools.jdi.SocketAttachingConnector;

public class Tracer {

	private String machine;
	private int port;
	private static Logger logger = Logger.getLogger("Tracer");
	private ProducerConsumerModel tracerMainThread;
	private Queue eventQueue;
	
	public Tracer(String machineName, int debugPort, TraceStrategy traceStrategy) throws InterruptedException {
		machine = machineName;
		port = debugPort;
		eventQueue = new LinkedBlockingQueue();
		EventQueueConsumer eventQueueConsumer = new EventQueueConsumer(eventQueue, traceStrategy);
		EventQueueProducer eventQueueProducer = new EventQueueProducer(eventQueue, hook());
		logger.info("Successfully hooked to " + machine + ":" + port);
		tracerMainThread = new ProducerConsumerModel(eventQueueProducer, eventQueueConsumer);
	}

	public RemoteVirtualMachine hook() throws InterruptedException {
		RemoteVirtualMachine vm = null;
		SocketAttachingConnector connector = findSocketAttachingConnector();
		
		logger.info("Attempt to hook to " + connector);
		Map<String, Connector.Argument> connectorArguments = connector.defaultArguments();
		Connector.Argument host = (Connector.Argument) connectorArguments.get("hostname");
		Connector.Argument portArg = (Connector.Argument) connectorArguments.get("port");
		Connector.Argument timeout = (Connector.Argument) connectorArguments.get("timeout");
		timeout.setValue(String.valueOf(3));
		host.setValue(machine);

		portArg.setValue(String.valueOf(port));
		
		try {
			vm = new RemoteVirtualMachine(connector.attach(connectorArguments));
			vm.setDebugTraceMode(VirtualMachine.TRACE_NONE);
		} catch (IOException exc) {
			throw new Error("Unable to hook to target JVM: [" + machine + ":" + port + "] due to " + exc + ".\n If the target JVM is running please make sure no other debug is running for the same JVM.");
		} catch (IllegalConnectorArgumentsException exc) {
			throw new Error("Unable to hook due to error: " + exc);
		}

		return vm;
	}

	/**
	 * @return
	 */
	private SocketAttachingConnector findSocketAttachingConnector() {
		List connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator iter = connectors.iterator();
		while (iter.hasNext()) {
			Connector connector = (Connector) iter.next();
			logger.info("Got connector :" + connector);
			if ("com.sun.jdi.SocketAttach".equals(connector.name())) {
				return (SocketAttachingConnector) connector;
			}
		}
		throw new Error("No socket connector found!");
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getMachine() {
		return machine;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void start() {
		tracerMainThread.start();
	}

	public void stop() {
		tracerMainThread.end();
	}

}
