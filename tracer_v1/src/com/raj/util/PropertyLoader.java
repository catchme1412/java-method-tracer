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
package com.raj.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class PropertyLoader {
    private static final boolean THROW_ON_LOAD_FAILURE = true;
    private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
    private static final String SUFFIX = ".properties";

    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extension. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * 
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     * 
     * @param name
     *            classpath resource name [may not be null]
     * @param loader
     *            classloader through which to load the resource [null is
     *            equivalent to the application loader]
     * 
     * @return resource converted to java.util.Properties [may be null if the
     *         resource was not found and THROW_ON_LOAD_FAILURE is false]
     * @throws IllegalArgumentException
     *             if the resource was not found and THROW_ON_LOAD_FAILURE is
     *             true
     */
    public static Properties loadProperties(String name, ClassLoader loader) {
        if (name == null)
            throw new IllegalArgumentException("null input: name");

        if (name.startsWith("/"))
            name = name.substring(1);

        if (name.endsWith(SUFFIX))
            name = name.substring(0, name.length() - SUFFIX.length());

        Properties result = null;

        InputStream in = null;
        try {
            if (loader == null)
                loader = ClassLoader.getSystemClassLoader();

            name = name.replace('.', '/');

            if (!name.endsWith(SUFFIX))
                name = name.concat(SUFFIX);

            // Returns null on lookup failures:
            in = loader.getResourceAsStream(name);
            if (in != null) {
                result = new Properties();
                result.load(in); // Can throw IOException
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Throwable ignore) {
                    ignore.printStackTrace();
                }
        }

        if (THROW_ON_LOAD_FAILURE && (result == null)) {
            throw new IllegalArgumentException("could not load [" + name + "]" + " as "
                    + (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
        }

        return result;
    }

    /**
     * Saves the <code>out</code> to the file <code>fileName</code> 
     * @param out
     * @param fileName
     * @return returns is saved.
     */
    public static boolean save(Properties out, String fileName) {
        boolean isSaved = false;
        // Write properties file.
        try {
            out.store(new FileOutputStream(fileName), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSaved;
    }
    
    public static void put(Properties prop, String key, String value) {
        prop.put(key, value);
    }
    
    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     */
    public static Properties load(final String name) {
        return loadProperties(name, Thread.currentThread().getContextClassLoader());
    }

    public static void main(String[] args) {
        System.out.println(load("excludeList.properties"));
    }

} // End of class

