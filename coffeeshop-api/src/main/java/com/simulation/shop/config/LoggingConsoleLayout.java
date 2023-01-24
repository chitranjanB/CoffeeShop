package com.simulation.shop.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

public class LoggingConsoleLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuffer sbuf = new StringBuffer(128);
        sbuf.append("-- ");
        sbuf.append("[");
        sbuf.append(event.getLevel());
        sbuf.append("]");
        sbuf.append(event.getLoggerName());
        sbuf.append(" - ");
        sbuf.append(event.getFormattedMessage().replaceAll("\n", "\n\t"));
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        return sbuf.toString();
    }
}