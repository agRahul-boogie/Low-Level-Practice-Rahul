package org.learn.design_patterns.bridge;

import org.learn.design_patterns.bridge.logger.ErrorLogger;
import org.learn.design_patterns.bridge.logger.InfoLogger;
import org.learn.design_patterns.bridge.logger.Logger;
import org.learn.design_patterns.bridge.logwriter.ConsoleLogWriter;
import org.learn.design_patterns.bridge.logwriter.DatabaseLogWriter;

public class BridgeMain {
    public static void main(String[] args) {
        Logger errorLogger = new ErrorLogger(new ConsoleLogWriter());
        Logger infoLogger = new InfoLogger(new DatabaseLogWriter());

        errorLogger.log("Something went wrong!");
        infoLogger.log("Service started successfully.");
    }
}