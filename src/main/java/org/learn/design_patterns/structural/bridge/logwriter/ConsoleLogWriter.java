package org.learn.design_patterns.structural.bridge.logwriter;

public class ConsoleLogWriter implements LogWriter {
    public void write(String message) {
        System.out.println("[Console] " + message);
    }
}
