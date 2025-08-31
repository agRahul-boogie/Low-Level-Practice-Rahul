package org.learn.design_patterns.structural.decorator.impl;

import org.learn.design_patterns.structural.decorator.Notifier;
import org.learn.design_patterns.structural.decorator.NotifierDecorator;

public class SlackNotifier extends NotifierDecorator {
    public SlackNotifier(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message);
        System.out.println("Sending Slack: " + message);
    }
}
