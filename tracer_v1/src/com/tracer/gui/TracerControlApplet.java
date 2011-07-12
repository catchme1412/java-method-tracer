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
package com.tracer.gui;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.JApplet;
import javax.swing.JLabel;

import com.tracer.Tracer;

/*
 <applet code="HelloWorldApplet" width=100 height=100>
 </applet>
 */

//every applet must extend from java.applet.Applet class
public class TracerControlApplet extends JApplet {

    Tracer tracer = null;
    
    public void init() {
        setLayout(new GridLayout(0,2));

        Label lab1 = new Label("Machine:");
        add(lab1);
        TextField t1 = new TextField(10);
        t1.setText("localhost");
        add(t1);
        
        Label lab2 = new Label("Port:");
        add(lab2);
        TextField t2 = new TextField(6);
        t2.setText("5000");
        add(t2);
        
        Label lab3 = new Label("Include:");
        add(lab3);
        TextField t3 = new TextField(6);
        t3.setText("com.orbitz.*");
        add(t3);
        
        Label lab4= new Label("Exclude:");
        add(lab4);
        TextField t4 = new TextField(6);
        t4.setText("java.util.*");
        add(t4);
        
        Label lab5= new Label("Status:");
        add(lab5);
        TextField t5 = new TextField(100);
        //t5.setText("java.util.*");
        add(t5);
        
        
        Button b = new Button("Start");
        b.setSize(10,20);
        add(b);
        
        
    }
    
    private static void addALine(String text, Container container) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(label);
    }

    
    
    /*
     * Override paint method. paint method is called every time the applet needs
     * to redisplay it's output. For example, when applet is first displayed or
     * applet window is minimized and then restored.
     */
    public void paint(Graphics g) {

        
        /*
         * Use void drawString(String str, int x, int y) method to print the
         * string at specified location x and y.
         */
       // g.drawString("Hello World", 150, 150);
    }
}