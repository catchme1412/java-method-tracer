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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Logger;

import com.model.producerconsumer.AbstractProducer;
import com.raj.util.PropertyLoader;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ThreadStartRequest;
import com.tracer.util.PackageTraceConfigManager;

/**
 * Add the Events available from remote server to the Queue.
 * 
 * @author rkvelayudhan
 * 
 */
public class EventQueueProducer extends AbstractProducer {

    private static Logger logger = Logger.getLogger("EventQueueProducer");
    /**
     * The event queue from the targeted JVM.
     */
    private EventQueue remoteVmEventQueue;

    /**
     * The virtual machine targeted for debugging.
     */
    private RemoteVirtualMachine remoteVirtualMachine;

    /**
     * The event request manager for the targeted JVM.
     */
    private EventRequestManager eventRequestManager;

    /**
     * The event queue from the targeted JVM.
     */
    private EventQueue eventQueue;

    /**
     * The events requested from this client application.
     */
    private Map<EventType, EventRequest> requestedEvents;

    public EventQueueProducer(Queue queue, RemoteVirtualMachine remoteVirtualMachine2) {
        super(queue);
        requestedEvents = new HashMap();
        remoteVirtualMachine = remoteVirtualMachine2;

        EventRequestManager eventRequestManager = remoteVirtualMachine.eventRequestManager();

        eventQueue = remoteVirtualMachine.eventQueue();
        remoteVirtualMachine.setDebugTraceMode(VirtualMachine.TRACE_NONE);
        setEventRequests(false);
    }

    /**
     * Create the desired event requests, and enable them so that we will get
     * events.
     * 
     * @param excludes
     *            Class patterns for which we don't want events
     * @param watchFields
     *            Do we want to watch assignments to fields
     */
    void setEventRequests(boolean watchFields) {
        EventRequestManager mgr = remoteVirtualMachine.eventRequestManager();

        // want all exceptions
        // ExceptionRequest excReq = mgr.createExceptionRequest(null, true,
        // true);
        // // suspend so we can step
        // excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        // excReq.enable();

        setMethodEntryRequest(mgr);

        setMethodExitRequest(mgr);

        // ThreadStartRequest tsr = mgr.createThreadStartRequest();
        // tsr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
        // tsr.enable();
        //
        // ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
        // // Make sure we sync on thread death
        // tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        // tdr.enable();
        //
        if (watchFields) {
            ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
            // for (int i = 0; i < excludes.length; ++i) {
            // cpr.addClassExclusionFilter(excludes[i]);
            // }
            cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            cpr.enable();
        }
    }

    private void setMethodExitRequest(EventRequestManager mgr) {
        MethodExitRequest methodExitRequest = mgr.createMethodExitRequest();
        PackageTraceConfigManager config = PackageTraceConfigManager.getInstance();
        // always call exclude first then include
        for (String exclude : config.getExcludeList()) {
            methodExitRequest.addClassExclusionFilter(exclude);
        }

        for (String include : config.getIncludeList()) {
            methodExitRequest.addClassFilter(include);
        }
        methodExitRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
        methodExitRequest.enable();
        requestedEvents.put(EventType.METHOD_EXIT, methodExitRequest);
    }

    private void setMethodEntryRequest(EventRequestManager mgr) {
        MethodEntryRequest methodEntryRequest = mgr.createMethodEntryRequest();
        PackageTraceConfigManager config = PackageTraceConfigManager.getInstance();

        // always call exclude first then include
        for (String exclude : config.getExcludeList()) {
            methodEntryRequest.addClassExclusionFilter(exclude);
        }

        for (String include : config.getIncludeList()) {
            methodEntryRequest.addClassFilter(include);
        }
        methodEntryRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
        methodEntryRequest.enable();
        requestedEvents.put(EventType.METHOD_ENTRY, methodEntryRequest);
    }

    @Override
    public List<Event> produce() {
        List<Event> events = new ArrayList<Event>();
        try {
            EventSet eventSet = eventQueue.remove();
            EventIterator it = eventSet.eventIterator();
            while (it.hasNext() && !isDone()) {
                if (!isPaused()) {
                    events.add(it.nextEvent());
                }
            }
            //if any thread is suspended invoke resume
            // eventSet.resume();
        } catch (InterruptedException exc) {
            throw new RuntimeException("Producer.produce failed", exc);
        } catch (VMDisconnectedException vmexe) {
            throw new RuntimeException(vmexe);
        }
        return events;
    }

    private boolean isPaused() {
        return false;
    }

    public void disconnect() {
        remoteVirtualMachine.dispose();
    }

    @Override
    public void stop() {
        setDone(true);
        disconnect();
    }
}
