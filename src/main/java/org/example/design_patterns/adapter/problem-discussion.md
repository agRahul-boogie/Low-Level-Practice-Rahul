# Design Pattern Practice Problems

## Adapter Pattern – Problem 1

**Problem**  
You are building a payment processing system for your e-commerce app.

- The app exposes a standard interface:

```java
public interface PaymentProcessor {
    void pay(int amount);
}
```

- You already integrated PayPal against this interface.
- Now you must also support Stripe, but Stripe’s SDK exposes this API:

```java
public class StripePayment {
    public void makePayment(String currency, double amount) {
        System.out.println("Paid " + amount + " " + currency + " using Stripe.");
    }
}
```

The `StripePayment` API is **not compatible** with your `PaymentProcessor` interface, and you cannot modify the SDK.

**Follow-up Question**  
What if Stripe supports **multiple currencies**, but your `PaymentProcessor` interface remains `void pay(int amount)`?  
How would you extend the design so clients can use multi-currency **without breaking existing code**?


## Adapter Pattern – Problem 2 (Multi‑currency Extension)

**Problem**  
Continuing from Problem 1:

- Your existing interface still exposes `void pay(int amount)` (assume USD by default).
- Stripe now supports **multiple currencies**.
- You want to enable multi-currency for new clients **while keeping backward compatibility** for old ones.

**Follow-up Question**  
Which approach would you choose and why?
1. Change the existing interface (breaking change).
2. Introduce an extended interface (e.g., adds `pay(int amount, String currency)`) and let only capable processors implement it.
3. Keep the current interface and add a decorator/wrapper that performs currency conversion or adds logging before delegating.  



***Solution***
Interview Simulation: Adapter + Multi-Currency

I:
You’ve designed a PaymentProcessor interface with pay(int amount) for USD payments. Now only Stripe supports multi-currency. Would you extend the interface to expose pay(int amount, String currency)?

Y:
That depends on the business requirement.

If our system only wants to process USD, then I would not extend the interface. My StripePaymentAdapter would just default to USD, and from the client’s perspective, Stripe behaves the same as PayPal.

But if the business actually wants to allow customers to pay in INR, EUR, GBP, etc., then I would introduce an extended interface (like AdvancedPaymentProcessor) that supports pay(int amount, String currency) — and only Stripe would implement that.

I:
But since only Stripe supports it, wouldn’t creating an extended interface just for one provider be over-engineering?

Y:
That’s a valid concern. If Stripe is the only multi-currency provider, and there’s no business requirement to expose those currencies, then I’d avoid over-design and just stick with the USD-only interface.
However, if the business explicitly says “we want to give customers the option to pay in multiple currencies,” then hiding Stripe’s capability behind a USD-only method doesn’t make sense — I’d expose it through an extended interface.
So, it’s really about balancing YAGNI (don’t over-design) with meeting business needs.

I:
What if you wanted to support multi-currency without extending the interface? Could you do that?

Y:
Yes — that’s where a decorator could come in.
If most of our processors only handle USD, but we want to offer multi-currency checkout to customers, I can wrap a USD-only processor in a CurrencyConversionDecorator. That decorator converts INR/EUR → USD before delegating to PayPal or Stripe.
But this only makes sense if we have a currency conversion module. If we don’t, then I’d rely on Stripe’s native multi-currency support instead.

I:
So, what would you do today if Stripe is the only one with multi-currency?

Y (final answer):
“Today, I would not extend the global interface, since that would over-complicate things for just one provider. I’d keep PaymentProcessor as is, let Stripe’s adapter default to USD, and if the business wants multi-currency, I’d either expose it through an extended interface or use a decorator with a conversion service. For now, keep it simple, but design in a way that can grow if more partners support multi-currency later.”

👉 This shows the interviewer:

You consider business requirements.

You avoid over-engineering.

You understand both Adapter and Decorator roles.

You give a pragmatic now vs. future answer.