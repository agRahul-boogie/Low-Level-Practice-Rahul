package org.learn.design_patterns.structural.decorator;

import org.learn.design_patterns.structural.decorator.impl.EmailNotifier;
import org.learn.design_patterns.structural.decorator.impl.SMSNotifier;
import org.learn.design_patterns.structural.decorator.impl.SlackNotifier;// Concrete Component


// Usage
public class DecoratorRunner {
    public static void main(String[] args) {
        // Base: only Email
        Notifier emailOnly = new EmailNotifier();
        emailOnly.send("Hello World!");

        System.out.println("----");

        // Email + SMS
        Notifier emailAndSMS = new SMSNotifier(new EmailNotifier());
        emailAndSMS.send("System Alert!");

        System.out.println("----");

        // Email + SMS + Slack
        Notifier all = new SlackNotifier(new SMSNotifier(new EmailNotifier()));
        all.send("Deployment Done!");
    }
}
