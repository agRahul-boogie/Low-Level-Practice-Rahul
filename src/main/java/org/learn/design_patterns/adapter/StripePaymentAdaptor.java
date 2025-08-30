package org.learn.design_patterns.adapter;

public class StripePaymentAdaptor implements PaymentProcessor{
    private final StripePayment stripePayment;
    private static final String DEFAULT_CURRENCY = "USD";
    public StripePaymentAdaptor(StripePayment stripePayment) {
        this.stripePayment = stripePayment;
    }



    @Override
    public void pay(int amount) {
        System.out.println("Inside Stripe Payment Adaptor");
        stripePayment.makePayment(DEFAULT_CURRENCY, amount);
        System.out.println("Payment done by Stripe!!");
    }
}
