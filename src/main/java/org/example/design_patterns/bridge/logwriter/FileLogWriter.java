package org.example.design_patterns.bridge.logwriter;

public class FileLogWriter implements LogWriter {
    public void write(String message) {
        // Pretend to write to file
        System.out.println("[File] " + message);
    }
}