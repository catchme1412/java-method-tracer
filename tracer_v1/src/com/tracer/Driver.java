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
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.JFrame;

import com.model.producerconsumer.TraceStrategy;
import com.tracer.gui.TracerControlApplet;

public class Driver {

    private static Logger logger = Logger.getLogger(Driver.class.getName());
    
    public static void main(String[] args) throws InterruptedException, SecurityException, IOException {
//        FileHandler fileHTML = new FileHandler("Logging.html");
//        TracerLogFormatter formatter = new TracerLogFormatter();
//        fileHTML.setFormatter(formatter);
//        logger.addHandler(fileHTML);
        
        String machine = "localhost";
        int port = 5000;
        if (args.length > 1) {
            machine = args[0];
            port = Integer.parseInt(args[1]);
        }

        logger.info("Tracer starting....");

        try {
            Tracer tracer = null;
            TraceStrategy traceStrategy = new TraceStrategy();
            tracer = new Tracer(machine, port, traceStrategy);
            tracer.start();
            addShutdownHook(tracer);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        //startApplet();

    }
    
    public static void startApplet() {
     // Create an instance of the applet class.
        JApplet applet = new TracerControlApplet();

        // Send the applet an init() message.
        applet.init();

        // Construct a JFrame.
        final JFrame frame =
                new JFrame("Tracer");

        // Transfer the applet's context pane to the JFrame.
        frame.setContentPane(applet.getContentPane());

        // Transfer the applet's menu bar into the JFrame.
        // This line can be omitted if the applet
        // does not create a menu bar.
        frame.setJMenuBar(applet.getJMenuBar());

        // Make the application shut down when the user clicks
        // on the close button.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame.
        // To pack the frame as tightly as possible
        // replace the setSize() message with the following.
        // frame.pack();
        frame.setSize(300, 300);

        // Set the location of the frame.
       // frame.setLocation(FrameX, FrameY);

        // Show the frame.
        frame.setVisible(true);

        // Invoke the applet's start() method.
        // This line can be omitted if the applet
        // does not define a start method.
        applet.start();
    }

    private static void addShutdownHook(final Tracer tracer) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("\n\nStarting clean shutdown...");
                try {
                    tracer.stop();
                    logger.info("\n\nClean shutdown complete.");
                } catch (Throwable e) {
                    logger.warning("Clean shutdown failed due to " + e.getMessage());
                }
            }
        });
    }
}
