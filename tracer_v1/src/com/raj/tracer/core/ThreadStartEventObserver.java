package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raj.tracer.core.event.AbstractEventObserver;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.request.ThreadStartRequest;

public class ThreadStartEventObserver extends AbstractEventObserver {

	private Map<ThreadGroup, List<Thread>> parentChildRelationalInfo;
	
	public ThreadStartEventObserver(ThreadStartRequestCriteria methodEntryRequestCriteria) {
		super(methodEntryRequestCriteria.getOnEventAction());
		parentChildRelationalInfo = new HashMap<ThreadGroup, List<Thread>>();
	}

	@Override
	public void execute(Event event) {
		getOnEventAction().execute(event);
		ThreadStartEvent threadStartEvent = (ThreadStartEvent)event;
		try {
			ThreadReference owningThread = threadStartEvent.thread().owningThread();
			if (owningThread != null) {
//				System.out.println("Owner:" +owningThread.uniqueID());
//				List<Long> list = parentChildRelationalInfo.get(owningThread.uniqueID());
//				if (list == null) {
//					parentChildRelationalInfo.put(threadStartEvent.thread().uniqueID(), new ArrayList<Long>());
//				} else {
//					list.add(threadStartEvent.thread().uniqueID());
//				}
			} else {
				List<Thread> list = parentChildRelationalInfo.get(-1);
				if (list == null) {
					list = new ArrayList();
//					parentChildRelationalInfo.put(owningThread.threadGroup(), list);
				}
//				list.add(threadStartEvent.thread().uniqueID());
			}
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
		System.out.println("DDDDDDDDDDDD");
		System.out.println("XXXXXXXXXXXXX"+parentChildRelationalInfo);
	}

	@Override
	public boolean isMatch(Event event) {
		if (event.request() instanceof ThreadStartRequest) {
			return true;
		}
		return false;
	}

}
