package org.learn.design_patterns.bridge.logwriter;

public class DatabaseLogWriter implements LogWriter {
    public void write(String message) {
        // Pretend to save in DB
        System.out.println("[Database] " + message);
    }
}