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

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @see http://www.glenmccl.com/perfj_009.htm
 * 
 *
 */
public class TracerOutput {
    private static final int BUFSIZ = 4096*2;
    private FileOutputStream fos;
    private byte buf[] = new byte[BUFSIZ];
    private int left;
    private int pos;
    private static boolean need_cr = false;

    // figure out whether we have \r or \r\n

    static {
        String s = System.getProperty("line.separator");
        need_cr = (s.length() >= 1 && s.charAt(0) == '\r');
    }

    // open a file

    public TracerOutput(String fn) throws IOException {
        fos = new FileOutputStream(fn);
        left = BUFSIZ;
        pos = 0;
    }

    // open standard output

    public TracerOutput() throws IOException {
        fos = new FileOutputStream(FileDescriptor.out);
        left = BUFSIZ;
        pos = 0;
        need_cr = false;
    }

    // close a file

    public synchronized void close() throws IOException {
        flush();
        fos.close();
        fos = null;
    }

    // flush output

    public synchronized void flush() throws IOException {
        if (pos > 0)
            fos.write(buf, 0, pos);
        left = BUFSIZ;
        pos = 0;
    }

    // output a character

    public synchronized void putc(int c) throws IOException {

        // flush output buffer if needed

        if (left <= 0)
            flush();

        // handle simple case

        if (c != '\n' || !need_cr) {
            left--;
            buf[pos++] = (byte) c;
        }

        // handle \r\n

        else {
            left--;
            buf[pos++] = '\r';
            if (left <= 0)
                flush();
            left--;
            buf[pos++] = '\n';
        }
    }

    // output a line

    public synchronized void println(String s) throws IOException {
        int len = (s == null ? 0 : s.length());

        // empty string

        if (len < 1)
            return;

        // whole string will fit in buffer

        if (len + 1 <= left) {
            if (len >= 2) {
                s.getBytes(0, len - 1, buf, pos);
                pos += len - 1;
                left -= len - 1;
            }
            putc(s.charAt(len - 1));
        }

        // whole string won't fit, do a character at a time

        else {
            for (int i = 0; i < len; i++)
                putc(s.charAt(i));
        }
    }
    
    public static void main(String args[])
    {
            TracerOutput fout = null;
            String s;

            try {
                    fout = new TracerOutput();
            }
            catch (Throwable e) {
                    System.err.println("* file opening error *");
            }

            try {
                    int N = 10000;
                    long t = System.currentTimeMillis();
                    for (int i = 1; i <= N; i++)
                            fout.println("xxxxxxxxxxxxxxx\n");
                    long f = System.currentTimeMillis() - t;
                    System.out.println("fout:" + f);
                    t = System.currentTimeMillis();
                    for (int i = 1; i <= N; i++)
                    System.out.print("xxxxxxxxxxxxxxx\n");
                    System.out.println("sop:" + (System.currentTimeMillis() - t) + " fout:" + f);
                    
                    
            }
            catch (Throwable e) {
                    System.err.println("*** file I/O error ***");
            }

            try {
                    fout.close();
            }

            catch (Throwable e) {
                    System.err.println("* file closing error *");
            }
    }
}