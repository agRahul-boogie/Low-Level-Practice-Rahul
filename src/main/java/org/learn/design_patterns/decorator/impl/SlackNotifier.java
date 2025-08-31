package org.learn.design_patterns.decorator.impl;

import org.learn.design_patterns.decorator.Notifier;
import org.learn.design_patterns.decorator.NotifierDecorator;

public class SlackNotifier extends NotifierDecorator {
    public SlackNotifier(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message);
        System.out.println("Sending Slack: " + message);
    }
}
