package org.learn.design_patterns.structural.facade;

class PaymentService {
    void processPayment(String userId) {
        System.out.println("Payment processed for user: " + userId);
    }
}
