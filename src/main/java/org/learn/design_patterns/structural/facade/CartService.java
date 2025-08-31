package org.learn.design_patterns.structural.facade;

// Subsystems
class CartService {
    void validateCart(String userId) {
        System.out.println("Cart validated for user: " + userId);
    }
}
