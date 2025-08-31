package org.learn.design_patterns.structural.adapter;

public class StripePayment {
    public void makePayment(String currency, double amount) {
        System.out.println("Paid " + amount + " " + currency + " using Stripe.");
    }
}