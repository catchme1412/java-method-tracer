package com.raj.tracer.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;

public class MethodEntryRequestCriteria extends EventRequestCriteria {

	private List<String> inclusionFilterList;
	private List<String> exclusionFilterList;

	public MethodEntryRequestCriteria(String inclusionFilter) {
		inclusionFilterList = new ArrayList<>();
		inclusionFilterList.add(inclusionFilter);
	}

	public MethodEntryRequestCriteria(String inclusionFilter[]) {
		inclusionFilterList = new ArrayList<>();
		inclusionFilterList.addAll(Arrays.asList(inclusionFilter));
	}

	@Override
	public EventRequest createEventRequest(EventRequestManager eventRequestManager) {
		MethodEntryRequest r = eventRequestManager.createMethodEntryRequest();
		for (String classFilter : inclusionFilterList) {
			r.addClassFilter(classFilter);
		}
		return r;
	}

	public List<String> getInclusionFilterList() {
		return (List<String>) Collections.unmodifiableCollection(inclusionFilterList);
	}

	public void setInclusionFilterList(List<String> inclusionFilterList) {
		this.inclusionFilterList = inclusionFilterList;
	}

	public void addExclusionFilter(String classFilter) {
		exclusionFilterList.add(classFilter);
	}

	public void addInclusionFilter(String classFilter) {
		inclusionFilterList.add(classFilter);
	}

	public List<String> getExclusionFilterList() {
		return (List<String>) Collections.unmodifiableCollection(exclusionFilterList);
	}

	public void setExclusionFilterList(List<String> exclusionFilterList) {
		this.exclusionFilterList = exclusionFilterList;
	}

}
