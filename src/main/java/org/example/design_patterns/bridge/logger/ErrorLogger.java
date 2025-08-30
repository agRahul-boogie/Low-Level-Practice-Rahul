package org.example.design_patterns.bridge.logger;

import org.example.design_patterns.bridge.logwriter.LogWriter;

public class ErrorLogger extends Logger {
    public ErrorLogger(LogWriter logWriter) {
        super(logWriter);
    }

    public void log(String message) {
        logWriter.write("[ERROR]: " + message);
    }
}




