package org.learn.design_patterns.decorator.impl;

import org.learn.design_patterns.decorator.Notifier;
import org.learn.design_patterns.decorator.NotifierDecorator;

// Concrete Decorators
public class SMSNotifier extends NotifierDecorator {
    public SMSNotifier(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message); // call wrapped notifier
        System.out.println("Sending SMS: " + message);
    }
}
