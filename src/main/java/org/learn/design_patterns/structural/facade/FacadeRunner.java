package org.learn.design_patterns.structural.facade;

// Usage
public class FacadeRunner {
    public static void main(String[] args) {
        CheckoutFacade facade = new CheckoutFacade(
            new CartService(),
            new PaymentService(),
            new InventoryService(),
            new NotificationService()
        );

        facade.checkout("user123");
    }
}