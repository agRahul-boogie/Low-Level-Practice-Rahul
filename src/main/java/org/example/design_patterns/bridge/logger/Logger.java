package org.example.design_patterns.bridge.logger;

import org.example.design_patterns.bridge.logwriter.LogWriter;

public abstract class Logger {
    protected LogWriter logWriter;

    public Logger(LogWriter logWriter) {
        this.logWriter = logWriter;
    }

    public abstract void log(String message);
}