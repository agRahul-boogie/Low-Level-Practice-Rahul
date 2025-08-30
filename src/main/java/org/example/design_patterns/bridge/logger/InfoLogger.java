package org.example.design_patterns.bridge.logger;

import org.example.design_patterns.bridge.logwriter.LogWriter;

public class InfoLogger extends Logger {
    public InfoLogger(LogWriter logWriter) {
        super(logWriter);
    }

    public void log(String message) {
        logWriter.write("[INFO]: " + message);
    }
}