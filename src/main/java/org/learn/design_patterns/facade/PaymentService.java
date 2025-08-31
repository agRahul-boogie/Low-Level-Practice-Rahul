package org.learn.design_patterns.facade;

class PaymentService {
    void processPayment(String userId) {
        System.out.println("Payment processed for user: " + userId);
    }
}
