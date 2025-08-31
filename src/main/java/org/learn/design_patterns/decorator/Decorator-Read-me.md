# 🎨 Decorator Pattern – Backend Example


## 📝 Quick Revision (Layman’s Definition + Analogy)

- **Definition**: Decorator Pattern is about **wrapping an object to add extra behavior without changing its original code**.
- **Analogy**: Think of a **coffee**:
    - Plain coffee = base object
    - Milk, sugar, whipped cream = decorators
    - You can stack them in any order, and each adds extra features.

👉 In backend terms, you can wrap a service with decorators like **logging**, **caching**, or **authentication** — without altering the base service.

## 🔹 Refactoring Guru Definition
Decorator is a structural design pattern that lets you attach new behaviors to objects by placing these objects inside special wrapper objects that contain the behaviors.

“Wrapper” is the alternative nickname for the Decorator pattern that clearly expresses the main idea of the pattern. A wrapper is an object that can be linked with some target object. The wrapper contains the same set of methods as the target and delegates to it all requests it receives. However, the wrapper may alter the result by doing something either before or after it passes the request to the target.

### Applicability
**Use the Decorator pattern when you need to be able to assign extra behaviors to objects at runtime without breaking the code that uses these objects.**
    
* The Decorator lets you structure your business logic into layers, create a decorator for each layer and compose objects with various combinations of this logic at runtime. The client code can treat all these objects in the same way, since they all follow a common interface.

**Use the pattern when it’s awkward or not possible to extend an object’s behavior using inheritance.**

* Many programming languages have the final keyword that can be used to prevent further extension of a class. For a final class, the only way to reuse the existing behavior would be to wrap the class with your own wrapper, using the Decorator pattern


## 🔹 Problem Statement (Backend Context)
You’re building a **notification service** for a system.

- Initially, it only sends **Email notifications**.
- Later, requirements come:
    - Send **SMS** notifications.
    - Send **Slack** messages.
    - Sometimes send **multiple types of notifications together** (e.g., Email + SMS).

👉 You want to add these features **without changing the core `Notifier` code**.  
👉 Also, you want flexibility: sometimes use only Email, sometimes Email+SMS, sometimes Email+Slack, etc.

---

## 🔹 Decorator Pattern Solution
- Define a base interface `Notifier` with `send(String message)`.
- `EmailNotifier` = base implementation.
- `NotifierDecorator` = abstract class that wraps another `Notifier`.
- `SMSNotifier` and `SlackNotifier` = concrete decorators that add extra behavior before/after delegating.

---

## 🔹 UML (Simplified)  
    Notifier (interface)
        ▲
        |
    EmailNotifier ← Base
        ▲
        |
    NotifierDecorator (abstract)
        ▲
    ┌───┴────────┐
    SMSNotifier SlackNotifier



---

## 🔹 Example in Java

```java
// Component
interface Notifier {
    void send(String message);
}

// Concrete Component
class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Sending EMAIL: " + message);
    }
}

// Decorator
abstract class NotifierDecorator implements Notifier {
    protected Notifier notifier;
    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }
    public void send(String message) {
        notifier.send(message); // delegate to wrapped notifier
    }
}

// Concrete Decorators
class SMSNotifier extends NotifierDecorator {
    public SMSNotifier(Notifier notifier) {
        super(notifier);
    }
    public void send(String message) {
        super.send(message); // call wrapped notifier
        System.out.println("Sending SMS: " + message);
    }
}

class SlackNotifier extends NotifierDecorator {
    public SlackNotifier(Notifier notifier) {
        super(notifier);
    }
    public void send(String message) {
        super.send(message);
        System.out.println("Sending Slack: " + message);
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        // Base: only Email
        Notifier emailOnly = new EmailNotifier();
        emailOnly.send("Hello World!");

        System.out.println("----");

        // Email + SMS
        Notifier emailAndSMS = new SMSNotifier(new EmailNotifier());
        emailAndSMS.send("System Alert!");

        System.out.println("----");

        // Email + SMS + Slack
        Notifier all = new SlackNotifier(new SMSNotifier(new EmailNotifier()));
        all.send("Deployment Done!");
    }
}
```
```
Sending EMAIL: Hello World!
----
Sending EMAIL: System Alert!
Sending SMS: System Alert!
----
Sending EMAIL: Deployment Done!
Sending SMS: Deployment Done!
Sending Slack: Deployment Done!
```


## 🔹 Benefits

- Add new behavior without modifying existing code.

- Combine behaviors dynamically (flexible).

- Follows Open/Closed Principle.

## 🔹 Interview Follow-up Questions

1. How is Decorator different from Adapter and Composite?

   - Adapter → makes two incompatible interfaces work together. 
   - Composite → treats part and whole uniformly (tree structures). 
   - Decorator → adds extra behavior dynamically without changing the class.

2. Where can Decorator be used in backend systems?
   - Adding caching to a service (wrap service with cache logic). 
   - Adding logging or metrics to DAO/service. 
   - Adding authentication or authorization checks before service calls.

3. What are the trade-offs?
   - Can lead to lots of small classes. 
   - Debugging can be harder since many wrappers are chained.