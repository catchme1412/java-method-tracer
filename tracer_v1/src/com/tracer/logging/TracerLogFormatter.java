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
package com.tracer.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

//This custom formatter formats parts of a log record to a single line
public class TracerLogFormatter extends Formatter
{
        
        // This method is called for every log records
        public String format(LogRecord rec)
        {
                StringBuffer buf = new StringBuffer(1000);
                // Bold any levels >= WARNING
                buf.append("|");

                if (rec.getLevel().intValue() >= Level.WARNING.intValue())
                {
                        buf.append("|");
                        buf.append(rec.getLevel());
                        buf.append("|");
                } else
                {
                        buf.append(rec.getLevel());
                }
                buf.append("|");
                buf.append(calcDate(rec.getMillis()));
                buf.append(' ');
                buf.append(formatMessage(rec));
                buf.append("|\n");
                return buf.toString();
        }

        private String calcDate(long millisecs)
        {
                SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                Date resultdate = new Date(millisecs);
                return date_format.format(resultdate);
        }

        // This method is called just after the handler using this
        // formatter is created
        public String getHead(Handler h)
        {
                return "Head";
        }

        // This method is called just after the handler using this
        // formatter is closed
        public String getTail(Handler h)
        {
                return "Tail";
        }
}
