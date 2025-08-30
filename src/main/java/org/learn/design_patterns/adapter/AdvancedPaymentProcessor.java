package org.learn.design_patterns.adapter;


/**
 * I will create this if partner starts accepting multiple currencies for payment and we want to let user pay via multiple currencies.
 */
public interface AdvancedPaymentProcessor extends PaymentProcessor{
    void pay(int amount, String currency);
}
