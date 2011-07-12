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

import java.util.Queue;
import java.util.logging.Logger;

/** class representing the Consumer side */
public class BasicConsumer implements Consumer {

    private static Logger logger = Logger.getLogger("BasicConsumer");
    protected Queue queue;

    private volatile boolean done;
    private ConsumerStrategy strategy;

    private static long activeTime;

    public long getActiveTime() {
        return activeTime;
    }

    public BasicConsumer(Queue eventQueue, ConsumerStrategy strategy) {
        this.queue = eventQueue;
        this.strategy = strategy;
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            while (!done) {
                while (!queue.isEmpty()) {
                    int len = queue.size();
                    Object obj = queue.remove();
                    consume(obj);
                }
                if (queue.isEmpty()) {
                    Thread.yield();
                }
            }

            activeTime += System.currentTimeMillis() - startTime;

        } catch (Throwable ex) {
            throw new RuntimeException("Consumer exception", ex);
        }

        logger.info("Consumer stopped.!!!!!");

    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    public void stop() {
        setDone(true);
    }
    
    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    public ConsumerStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ConsumerStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void consume(Object queueElement) {
        strategy.consume(queueElement);
    }
}