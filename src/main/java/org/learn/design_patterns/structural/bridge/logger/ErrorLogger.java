package org.learn.design_patterns.structural.bridge.logger;

import org.learn.design_patterns.structural.bridge.logwriter.LogWriter;

public class ErrorLogger extends Logger {
    public ErrorLogger(LogWriter logWriter) {
        super(logWriter);
    }

    public void log(String message) {
        logWriter.write("[ERROR]: " + message);
    }
}




