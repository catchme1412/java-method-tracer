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

import java.util.logging.Logger;

/**
 * Implementation of Producer Consumer pattern. The class creates separate
 * threads for producer and consumer and waits until it is done. Any exception
 * occurs at producer or consumer is captured by this class itself.
 * 
 * @author rkvelayudhan
 * 
 */
public class ProducerConsumerModel implements Runnable {
    
    private static Logger logger = Logger.getLogger("ProducerConsumerModel");

    private Producer producer;

    private Consumer consumer;

    private ConsumerStrategy consumerStrategy;

    private Thread consumerThread;

    private Thread producerThread;

    private Thread.UncaughtExceptionHandler exceptionHandler;

    /**
     * Parent thread for producer and consumer
     */
    private Thread producerConsumerModelThread;

    private volatile boolean done;

    public ProducerConsumerModel(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
        exceptionHandler = new UncaughtExceptionHandler();
        consumerThread = new Thread(consumer);
        consumerThread.setName("consumer");
        consumerThread.setUncaughtExceptionHandler(exceptionHandler);
        producerThread = new Thread(producer);
        producerThread.setName("producer");
        producerThread.setUncaughtExceptionHandler(exceptionHandler);

        producerConsumerModelThread = new Thread(this);
        producerConsumerModelThread.setName("producer-consumer-parent");
    }

    @Override
    public void run() {

        System.out.println("producer consumer model started");
        consumerThread.setPriority(Thread.MAX_PRIORITY);
        producerThread.setPriority(Thread.MIN_PRIORITY);
        consumerThread.setName("ConsumerThread");
        producerThread.setName("ProducerThread");
        consumerThread.start();
        producerThread.start();
        logger.info("Producer status :" + producerThread.getState());
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            consumerThread.join();
            producerThread.join();
        } catch (InterruptedException e) {
            logger.severe("Producer Consumer Model error:" + e);
            e.printStackTrace();
        }
        
//        while (!done) {
//            try {
//                Thread.sleep(2000);
//                
//                logger.info("Producer:" + producerThread.getState());
//                logger.info("Consumer:" + consumerThread.getState());
//                logger.info("Queue size:" + producer.currentQueueSize());
//                
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        

        logger.info("ProducerConsumerModel done..");
    }

    public void setConsumerStrategy(ConsumerStrategy consumerStrategy) {
        this.consumerStrategy = consumerStrategy;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public ConsumerStrategy getConsumerStrategy() {
        return consumerStrategy;
    }

    public void start() {
        producerConsumerModelThread.setUncaughtExceptionHandler(exceptionHandler);
        producerConsumerModelThread.start();
    }

    public void end() {
        done = true;
        consumer.setDone(done);
        producer.setDone(done);
        producer.stop();
        consumer.stop();
        producerThread.interrupt();
        consumerThread.interrupt();
    }

    private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            end();
            System.out.println("Problem in " + t.getName() + " : " + e.getMessage());
            e.printStackTrace();
        }

    }
}
