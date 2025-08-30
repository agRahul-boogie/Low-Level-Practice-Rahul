package org.example.design_patterns.bridge.logwriter;

public class ConsoleLogWriter implements LogWriter {
    public void write(String message) {
        System.out.println("[Console] " + message);
    }
}
