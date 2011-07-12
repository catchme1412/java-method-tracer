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

import java.util.Collection;
import java.util.Queue;
import java.util.logging.Logger;

public abstract class AbstractProducer implements Producer {

    private Queue queue;
    private volatile boolean done;

    private volatile int currentQueueSize;
    protected static long activeTime = 0;
    private static Logger logger = Logger.getLogger("AbstractProducer");

    int producerQueueSizeDelta = 2000;

    public AbstractProducer(Queue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            int i = 0;
            while (!done) {
                i++;
                if (currentQueueSize > producerQueueSizeDelta || currentQueueSize < 100) {
                    logger.fine("Yielding producer thread:" + currentQueueSize);
                    Thread.yield();
                } else {
                    logger.fine("One produce took:" + currentQueueSize);
                }
                Collection<?> justProduced = produce();
                queue.addAll(justProduced);
                currentQueueSize = queue.size();
            }
            activeTime += System.currentTimeMillis() - startTime;
        } catch (Exception ex) {
            throw new RuntimeException("Producer exception:" + ex, ex);
        } finally {
            logger.warning("Producer stopped.!!!!!!!");
        }
    }

    public long getActiveTime() {
        return activeTime;
    }

    public abstract Collection produce();

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public void setCurrentQueueSize(int currentQueueSize) {
        this.currentQueueSize = currentQueueSize;
    }

    @Override
    public int currentQueueSize() {
        return this.currentQueueSize;
    }

    public Queue getQueue() {
        return queue;
    }

}