package org.learn.design_patterns.structural.facade;

class NotificationService {
    void sendConfirmation(String userId) {
        System.out.println("Confirmation email sent to user: " + userId);
    }
}
