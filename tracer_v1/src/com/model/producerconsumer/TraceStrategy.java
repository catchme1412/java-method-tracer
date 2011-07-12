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
package com.model.producerconsumer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.raj.util.TracerOutput;
import com.sun.jdi.Method;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;

public class TraceStrategy implements ConsumerStrategy {


    private TracerOutput fout;
    private Map levelIndicator;
    
    private boolean isTraceMethodExit = true;
    
    public TraceStrategy() {
        
        try {
            fout = new TracerOutput();
            fout.println("Staring the tracing\n");
            fout.println("*******************************************************************\n");
            levelIndicator = new HashMap ();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void consume(Object queueElement) {

        if (queueElement instanceof Collection) {
            Event event = (Event) ((Collection) queueElement).iterator();
            trace(event.toString());
        } else {
            // System.out.println("Attempt to handle"+queueElement.getClass());
            handleEvent((Event) queueElement);
        }

    }

    /**
     * Dispatch incoming events
     */
    private void handleEvent(Event event) {
        if (event instanceof ExceptionEvent) {
            exceptionEvent((ExceptionEvent) event);
        } else if (event instanceof ModificationWatchpointEvent) {
            fieldWatchEvent((ModificationWatchpointEvent) event);
        } else if (event instanceof MethodEntryEvent) {
            methodEntryEvent((MethodEntryEvent) event);
        } else if (event instanceof MethodExitEvent) {
            if (isTraceMethodExit) {
                methodExitEvent((MethodExitEvent) event);
            }
        } else if (event instanceof StepEvent) {
            stepEvent((StepEvent) event);
        } else if (event instanceof ThreadStartEvent) {
            levelIndicator.put(((ThreadStartEvent)event).thread(), new Integer(0));
        } else if (event instanceof ThreadDeathEvent) {
            threadDeathEvent((ThreadDeathEvent) event);
        } else if (event instanceof ClassPrepareEvent) {
            classPrepareEvent((ClassPrepareEvent) event);
        } else if (event instanceof VMStartEvent) {
            vmStartEvent((VMStartEvent) event);
        } else if (event instanceof VMDeathEvent) {
            vmDeathEvent((VMDeathEvent) event);
        } else if (event instanceof VMDisconnectEvent) {
            vmDisconnectEvent((VMDisconnectEvent) event);
        } else {
            throw new Error("Unexpected event type" + event.getClass());
        }
    }

    private void vmStartEvent(VMStartEvent event) {
        trace("-- VM Started --");
    }

    // Forward event for thread specific processing
    private void methodEntryEvent(MethodEntryEvent event) {
        Integer level = (Integer) levelIndicator.get(event.thread());
        if (level == null) {
            level = new Integer(0);
            levelIndicator.put(event.thread(), level);
        }
        Method method = event.method();
        StringBuilder buf = new StringBuilder();
        buf.append(System.currentTimeMillis());
        buf.append("|>|");
        buf.append((++level));
        buf.append("|");
        buf.append(event.thread().name());
        buf.append("|");
        buf.append(method.declaringType().name());
        buf.append(".");
        buf.append(method.name());
        buf.append("(");
        buf.append(method.location());
        buf.append(")\n");
        trace(buf.toString());
    }

    // Forward event for thread specific processing
    private void methodExitEvent(MethodExitEvent event) {
        Integer level = (Integer) levelIndicator.get(event.thread());
        if (level == null) {
            level = new Integer(0);
            levelIndicator.put(event.thread(), level);
        }
        Method method = event.method();
        StringBuilder buf = new StringBuilder();
        buf.append(System.currentTimeMillis());
        buf.append("|<|");
        buf.append((--level));
        buf.append("|");
        buf.append(event.thread().name());
        buf.append("|");
        buf.append(method.declaringType().name());
        buf.append(".");
        buf.append(method.name());
        buf.append("(");
        buf.append(method.location());
        buf.append(")\n");
        trace(buf.toString());
    }

    public void trace(String str) {
        try {
            fout.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        writer.append(str);
//        writer.flush();
    }

    // Forward event for thread specific processing
    private void stepEvent(StepEvent event) {
        // threadTrace(event.thread()).stepEvent(event);
        // EventRequestManager mgr = remoteVirtualMachine.eventRequestManager();
        // mgr.deleteEventRequest(event.request());
    }

    // Forward event for thread specific processing
    private void fieldWatchEvent(ModificationWatchpointEvent event) {
        // threadTrace(event.thread()).fieldWatchEvent(event);
    }

    void threadDeathEvent(ThreadDeathEvent event) {
        // BaseTraceStrategy trace = (BaseTraceStrategy)
        // traceMap.get(event.thread());
        // if (trace != null) { // only want threads we care about
        // trace.threadDeathEvent(event); // Forward event
        // }
    }

    /**
     * A new class has been loaded. Set watchpoints on each of its fields
     */
    private void classPrepareEvent(ClassPrepareEvent event) {
        // EventRequestManager mgr = remoteVirtualMachine.eventRequestManager();
        // List fields = event.referenceType().visibleFields();
        // for (Iterator it = fields.iterator(); it.hasNext();) {
        // Field field = (Field) it.next();
        // ModificationWatchpointRequest req =
        // mgr.createModificationWatchpointRequest(field);
        // for (int i = 0; i < excludes.length; ++i) {
        // req.addClassExclusionFilter(excludes[i]);
        // }
        // req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
        // req.enable();
        // }
    }

    private void exceptionEvent(ExceptionEvent event) {
        // BaseTraceStrategy trace = (BaseTraceStrategy)
        // traceMap.get(event.thread());
        // if (trace != null) { // only want threads we care about
        // trace.exceptionEvent(event); // Forward event
        // // Step to the catch
        // EventRequestManager mgr =
        // getRemoteVirtualMachine().eventRequestManager();
        // StepRequest req = mgr.createStepRequest(trace.getThread(),
        // StepRequest.STEP_MIN, StepRequest.STEP_INTO);
        // req.addCountFilter(1); // next step only
        // req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        // req.enable();
        // }
    }

    public void vmDeathEvent(VMDeathEvent event) {
        // vmDied = true;
        trace("-- The application exited --");
    }

    public void vmDisconnectEvent(VMDisconnectEvent event) {
        // connected = false;
        // if (!vmDied) {
        trace("-- The application has been disconnected --");
        // }
    }

    public void setTraceMethodExit(boolean isTraceMethodExit) {
        this.isTraceMethodExit = isTraceMethodExit;
    }

    public boolean isTraceMethodExit() {
        return isTraceMethodExit;
    }

    
    

}
