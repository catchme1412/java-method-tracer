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

import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggingThreadGroup extends ThreadGroup {
  private static Logger logger;
  public LoggingThreadGroup(String name) {
    super(name);
//    logger = new LoggingThreadGroup(name);
  }
  public void uncaughtException(Thread t, Throwable e) {
//	  logger.log(Level.ALL, "Exception caught:" + t + " : " + e, e);
	  System.out.println(e);
  }
}
