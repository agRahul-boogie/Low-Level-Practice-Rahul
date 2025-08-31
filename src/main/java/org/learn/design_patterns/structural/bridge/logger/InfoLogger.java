package org.learn.design_patterns.structural.bridge.logger;

import org.learn.design_patterns.structural.bridge.logwriter.LogWriter;

public class InfoLogger extends Logger {
    public InfoLogger(LogWriter logWriter) {
        super(logWriter);
    }

    public void log(String message) {
        logWriter.write("[INFO]: " + message);
    }
}