# Chain of Responsibility – Backend Cheat Sheet

## 1. Definition & Understanding

* **Definition:** Chain of Responsibility (CoR) allows a request to pass along a chain of handlers. Each handler decides whether to **process** the request or **pass it along**.
* **Understanding:** Decouples sender and receiver; supports dynamic configuration of handlers; simplifies complex if-else logic.

## 1.1 Layman’s Analogy

Think of a customer service call center:

* **Level 1:** Support Agent

* **Level 2:** Team Lead

* **Level 3:** Manager

A query escalates up the chain until someone can handle it.
## 2. Loan Approval Example

```java
abstract class LoanHandler {
    protected LoanHandler nextHandler;
    public void setNextHandler(LoanHandler next) { this.nextHandler = next; }
    abstract void handleRequest(double amount);
}

class JuniorOfficer extends LoanHandler {
    @Override
    void handleRequest(double amount) {
        if(amount <= 10000) System.out.println("Junior approved: " + amount);
        else if(nextHandler != null) nextHandler.handleRequest(amount);
    }
}

class SeniorOfficer extends LoanHandler {
    @Override
    void handleRequest(double amount) {
        if(amount <= 50000) System.out.println("Senior approved: " + amount);
        else if(nextHandler != null) nextHandler.handleRequest(amount);
    }
}

class Manager extends LoanHandler {
    @Override
    void handleRequest(double amount) {
        if(amount > 50000) System.out.println("Manager approved: " + amount);
    }
}

// Usage
JuniorOfficer junior = new JuniorOfficer();
SeniorOfficer senior = new SeniorOfficer();
Manager manager = new Manager();
junior.setNextHandler(senior);
senior.setNextHandler(manager);

junior.handleRequest(7500);    // Junior approves
junior.handleRequest(30000);   // Senior approves
junior.handleRequest(100000);  // Manager approves
```

## 3. Real-World Backend Examples
### A. Loan Approval System

* Already discussed: Junior → Senior → Manager.

* Helps avoid huge if-else blocks for approval thresholds.

### B. Middleware / HTTP Filters
In frameworks like Spring Boot:

* Filters, interceptors, or middleware form a chain.
* Each filter can process the request or pass it to the next filter.
* Example: Authentication → Authorization → Logging → Business Logic

```java
public interface Filter {
void doFilter(Request req, FilterChain chain);
}

class AuthFilter implements Filter {
public void doFilter(Request req, FilterChain chain) {
if(req.isAuthorized()) chain.doFilter(req);
else System.out.println("Unauthorized!");
}
}
```
* This is literally CoR in web backend systems.

### C. Exception Handling / Validation

* Multiple validators can check an object in a chain:
* Null check → Format check → Business rule check → Save object
* 
## 4. Key Points

* Handlers can be configured dynamically at runtime.
* Useful in approval flows, middleware, logging, validation.
* Simplifies long if-else chains and improves extensibility.

## 5. Basic Interview Questions & Answers

**Q1. What is the main intent of the Chain of Responsibility pattern?**

* **Answer:** Decouple sender and receiver by passing requests along a chain of handlers, letting each handler decide whether to process it.

**Q2. How is a chain of handlers typically configured?**

* **Answer:** Each handler has a reference to the next handler. The chain can be configured dynamically at runtime by linking handlers in a specific order.

**Q3. How does CoR improve maintainability compared to long if-else statements?**

* **Answer:** CoR removes hardcoded conditional logic, making it easier to add, remove, or modify handlers without changing existing code.

**Q4. Can the chain order be changed at runtime? How?**

* **Answer:** Yes. The chain is just a linked list of handlers, so you can dynamically assign `nextHandler` references based on config, environment, or business rules.

**Q5. Give a backend example where CoR is commonly used.**

* **Answer:** Loan approval systems, HTTP middleware/filters, logging chains, validation pipelines, or remote request handling.


## 6. Benefits

* Decoupling: Sender doesn’t know which handler will process request.
* Extensibility: Add new handlers without changing existing ones.
* Flexibility: Dynamic chains at runtime.
* Simplifies logic: Avoid long nested if-else or switch-case chains.

## 7. Trade-offs / Considerations
* Chain length: Too long a chain can impact performance.
* Responsibility clarity: Hard to know which handler will finally process the request.
* Order matters: Handlers must be linked in proper order to work correctly.