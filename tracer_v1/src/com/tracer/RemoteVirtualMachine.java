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
/**
 * Copyright (C) 2011  Rajesh Kumar K V
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.tracer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VoidValue;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.request.EventRequestManager;
import com.sun.tools.jdi.SocketAttachingConnector;

/**
 * 
 * @author rajeshkumarkv@gmail.com
 * 
 */
public class RemoteVirtualMachine implements VirtualMachine {

	private String machineName;
	private int port;

	private VirtualMachine virtualMachine;

	public RemoteVirtualMachine(VirtualMachine attach) {
		virtualMachine = attach;
	}

	public void setMachine(String machineName) {
		this.machineName = machineName;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public RemoteVirtualMachine hook() throws InterruptedException {
		SocketAttachingConnector connector = findSocketAttachingConnector();
		Map connectorArguments = connector.defaultArguments();
		Connector.Argument host = (Connector.Argument) connectorArguments.get("hostname");
		Connector.Argument portArg = (Connector.Argument) connectorArguments.get("port");

		host.setValue(machineName);

		portArg.setValue(String.valueOf(port));

		try {
			setVirtualMachine(connector.attach(connectorArguments));
		} catch (IOException exc) {
			throw new Error("Unable to hook to target JVM: [" + machineName + ":" + port + "] due to " + exc);
		} catch (IllegalConnectorArgumentsException exc) {
			throw new Error("Unable to hook due to error: " + exc);
		}

		return this;
	}

	/**
	 * @return
	 */
	private SocketAttachingConnector findSocketAttachingConnector() {
		List connectors = Bootstrap.virtualMachineManager().allConnectors();
		Iterator iter = connectors.iterator();
		while (iter.hasNext()) {
			Connector connector = (Connector) iter.next();
			System.out.println(connector);
			if ("com.sun.jdi.SocketAttach".equals(connector.name())) {
				return (SocketAttachingConnector) connector;
			}
		}
		throw new Error("No socket connector found!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.Mirror#virtualMachine()
	 */
	@Override
	public VirtualMachine virtualMachine() {
		// TODO Auto-generated method stub
		return virtualMachine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#allClasses()
	 */
	@Override
	public List<ReferenceType> allClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#allThreads()
	 */
	@Override
	public List<ThreadReference> allThreads() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canAddMethod()
	 */
	@Override
	public boolean canAddMethod() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canBeModified()
	 */
	@Override
	public boolean canBeModified() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canForceEarlyReturn()
	 */
	@Override
	public boolean canForceEarlyReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetBytecodes()
	 */
	@Override
	public boolean canGetBytecodes() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetClassFileVersion()
	 */
	@Override
	public boolean canGetClassFileVersion() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetConstantPool()
	 */
	@Override
	public boolean canGetConstantPool() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetCurrentContendedMonitor()
	 */
	@Override
	public boolean canGetCurrentContendedMonitor() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetInstanceInfo()
	 */
	@Override
	public boolean canGetInstanceInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetMethodReturnValues()
	 */
	@Override
	public boolean canGetMethodReturnValues() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetMonitorFrameInfo()
	 */
	@Override
	public boolean canGetMonitorFrameInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetMonitorInfo()
	 */
	@Override
	public boolean canGetMonitorInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetOwnedMonitorInfo()
	 */
	@Override
	public boolean canGetOwnedMonitorInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetSourceDebugExtension()
	 */
	@Override
	public boolean canGetSourceDebugExtension() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canGetSyntheticAttribute()
	 */
	@Override
	public boolean canGetSyntheticAttribute() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canPopFrames()
	 */
	@Override
	public boolean canPopFrames() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canRedefineClasses()
	 */
	@Override
	public boolean canRedefineClasses() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canRequestMonitorEvents()
	 */
	@Override
	public boolean canRequestMonitorEvents() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canRequestVMDeathEvent()
	 */
	@Override
	public boolean canRequestVMDeathEvent() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canUnrestrictedlyRedefineClasses()
	 */
	@Override
	public boolean canUnrestrictedlyRedefineClasses() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canUseInstanceFilters()
	 */
	@Override
	public boolean canUseInstanceFilters() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canUseSourceNameFilters()
	 */
	@Override
	public boolean canUseSourceNameFilters() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canWatchFieldAccess()
	 */
	@Override
	public boolean canWatchFieldAccess() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#canWatchFieldModification()
	 */
	@Override
	public boolean canWatchFieldModification() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#classesByName(java.lang.String)
	 */
	@Override
	public List<ReferenceType> classesByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#description()
	 */
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		virtualMachine.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#eventQueue()
	 */
	@Override
	public EventQueue eventQueue() {
		// TODO Auto-generated method stub
		return virtualMachine.eventQueue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#eventRequestManager()
	 */
	@Override
	public EventRequestManager eventRequestManager() {
		// TODO Auto-generated method stub
		return virtualMachine.eventRequestManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#exit(int)
	 */
	@Override
	public void exit(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#getDefaultStratum()
	 */
	@Override
	public String getDefaultStratum() {
		// TODO Auto-generated method stub
		return virtualMachine.getDefaultStratum();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#instanceCounts(java.util.List)
	 */
	@Override
	public long[] instanceCounts(List<? extends ReferenceType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(boolean)
	 */
	@Override
	public BooleanValue mirrorOf(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(byte)
	 */
	@Override
	public ByteValue mirrorOf(byte arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(char)
	 */
	@Override
	public CharValue mirrorOf(char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(short)
	 */
	@Override
	public ShortValue mirrorOf(short arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(int)
	 */
	@Override
	public IntegerValue mirrorOf(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(long)
	 */
	@Override
	public LongValue mirrorOf(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(float)
	 */
	@Override
	public FloatValue mirrorOf(float arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(double)
	 */
	@Override
	public DoubleValue mirrorOf(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOf(java.lang.String)
	 */
	@Override
	public StringReference mirrorOf(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#mirrorOfVoid()
	 */
	@Override
	public VoidValue mirrorOfVoid() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#process()
	 */
	@Override
	public Process process() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#redefineClasses(java.util.Map)
	 */
	@Override
	public void redefineClasses(Map<? extends ReferenceType, byte[]> arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		virtualMachine.resume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#setDebugTraceMode(int)
	 */
	@Override
	public void setDebugTraceMode(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#setDefaultStratum(java.lang.String)
	 */
	@Override
	public void setDefaultStratum(String arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#suspend()
	 */
	@Override
	public void suspend() {
		// TODO Auto-generated method stub
		virtualMachine.suspend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#topLevelThreadGroups()
	 */
	@Override
	public List<ThreadGroupReference> topLevelThreadGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jdi.VirtualMachine#version()
	 */
	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setVirtualMachine(VirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
		this.virtualMachine.setDebugTraceMode(VirtualMachine.TRACE_NONE);
	}

	public VirtualMachine getVirtualMachine() {
		return virtualMachine;
	}

}
