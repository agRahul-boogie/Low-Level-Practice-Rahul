package org.learn.design_patterns.facade;

// Facade
class CheckoutFacade {
    private final CartService cartService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;

    public CheckoutFacade(CartService c, PaymentService p, InventoryService i, NotificationService n) {
        this.cartService = c;
        this.paymentService = p;
        this.inventoryService = i;
        this.notificationService = n;
    }

    public void checkout(String userId) {
        cartService.validateCart(userId);
        paymentService.processPayment(userId);
        inventoryService.updateStock(userId);
        notificationService.sendConfirmation(userId);
    }
}
