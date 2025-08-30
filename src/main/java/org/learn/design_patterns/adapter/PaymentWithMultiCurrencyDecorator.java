package org.learn.design_patterns.adapter;


/*
if I have currency conversion module and if only single partner allow multi currency then let's we can convert currency at our end only and use default usd interface to make payment
 */
public class PaymentWithMultiCurrencyDecorator {


    private final PaymentProcessor paymentProcessor;

    public PaymentWithMultiCurrencyDecorator(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }


    public void pay(int amount, int currency) {
        paymentProcessor.pay(convertToUSD(amount));
    }

    private int convertToUSD(int amountInInr) {
        return (int)(amountInInr / 80.0); // rough conversion
    }
}
