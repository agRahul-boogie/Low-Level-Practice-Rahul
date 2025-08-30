package org.learn.design_patterns.adapter;

public class PaypalPayment implements PaymentProcessor{



    @Override
    public void pay(int amount) {
        System.out.println("Paying via Paypal");
    }
}
