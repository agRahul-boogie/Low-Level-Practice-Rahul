package org.learn.design_patterns.structural.decorator.impl;

import org.learn.design_patterns.structural.decorator.Notifier;

public class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Sending EMAIL: " + message);
    }
}