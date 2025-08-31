package org.learn.design_patterns.structural.decorator;

/**
 *
 * Decorator class created to extend the notification functionality
 */
public abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;

    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }

    public void send(String message) {
        notifier.send(message); // delegate to wrapped notifier
    }
}
