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

import java.util.Queue;

import com.model.producerconsumer.BasicConsumer;
import com.model.producerconsumer.ConsumerStrategy;

public class EventQueueConsumer extends BasicConsumer {

    public EventQueueConsumer(Queue eventQueue, ConsumerStrategy strategy) {
        super(eventQueue, strategy);
    }

    /***
     * A VMDisconnectedException has happened while dealing with another event.
     * We need to flush the event queue, dealing only with exit events (VMDeath,
     * VMDisconnect) so that we terminate correctly.
     */
    synchronized void handleDisconnectedException() {
        // EventQueue queue = remoteVirtualMachine.eventQueue();
        // while (connected) {
        // try {
        // EventSet eventSet = queue.remove();
        // EventIterator iter = eventSet.eventIterator();
        // while (iter.hasNext()) {
        // Event event = iter.nextEvent();
        // if (event instanceof VMDeathEvent) {
        // vmDeathEvent((VMDeathEvent) event);
        // } else if (event instanceof VMDisconnectEvent) {
        // vmDisconnectEvent((VMDisconnectEvent) event);
        // }
        // }
        // eventSet.resume(); // Resume the VM
        // } catch (InterruptedException exc) {
        // disconnect();
        // }
        // }
        // disconnect();
    }

}
