package org.learn.design_patterns.structural.bridge.logwriter;

public class FileLogWriter implements LogWriter {
    public void write(String message) {
        // Pretend to write to file
        System.out.println("[File] " + message);
    }
}