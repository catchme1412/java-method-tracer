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
package com.tracer.util;

import java.util.Properties;

import com.raj.util.PropertyLoader;

/**
 * Manages the list of packages that are to be excluded or included in the trace.
 *
 */
public class PackageTraceConfigManager {

    private static final String INCLUDE_LIST_FILE = "includeList.properties";
    private static final String EXCLUDE_LIST_FILE = "excludeList.properties";
    private static final String INCLUDE_LIST_KEY = "includeList";
    private static final String EXCLUDE_LIST_KEY = "excludeList";

    private static PackageTraceConfigManager instance;
    
    /**
     * The list of java package names that need to be included in the trace.
     */
    private Properties includeList;

    /**
     * The list of java package names that need to be excluded in the trace.
     */
    private Properties excludeList;

    private PackageTraceConfigManager() {
        excludeList = PropertyLoader.load(EXCLUDE_LIST_FILE);
        includeList = PropertyLoader.load(INCLUDE_LIST_FILE);
    }

    public static PackageTraceConfigManager getInstance() {
        if (instance == null) {
            instance = new PackageTraceConfigManager();
        }
        return instance;
    }

    public String[] getIncludeList() {
        return includeList.getProperty(INCLUDE_LIST_KEY).split(",");
    }

    public String[] getExcludeList() {
        return excludeList.getProperty(EXCLUDE_LIST_KEY).split(",");
    }

    public void addInclusionEntry(String entry) {
        String values = includeList.getProperty(INCLUDE_LIST_KEY);
        includeList.put(INCLUDE_LIST_KEY, values + "," + entry.trim());
    }

    public void removeInclusionEntry(String entry) {
        includeList.remove(entry);
    }

    public void addExclusionEntry(String entry) {
        String values = includeList.getProperty(EXCLUDE_LIST_KEY);
        includeList.put(EXCLUDE_LIST_KEY, values + "," + entry.trim());
    }

    public void saveIncludeList() {
        PropertyLoader.save(includeList, INCLUDE_LIST_FILE);
    }

    public void saveExcludeList() {
        PropertyLoader.save(excludeList, EXCLUDE_LIST_FILE);
    }

    public void removeExclusionEntry(String entry) {
        excludeList.remove(entry);
    }
}
