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
import java.util.HashMap;
import java.util.Queue;

public class ProducerImpl extends AbstractProducer {

	public ProducerImpl(Queue theQueue) {
		super(theQueue);
	}

	@Override
	public Collection produce() {
		// TODO Auto-generated method stub
		HashMap hashMap = new HashMap();
		hashMap.put(new Object(), new Object());
		return (Collection) hashMap;
	}

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

	


}