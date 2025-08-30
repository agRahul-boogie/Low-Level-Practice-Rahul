# 🏗 Bridge Pattern – Backend Example

## 🔹 Problem Statement  
You are designing a **logging system** for your microservices.  

- You have different **log levels**: `ErrorLogger`, `InfoLogger`, etc.  
- You want to support multiple **log outputs**: `ConsoleLogWriter`, `FileLogWriter`, `DatabaseLogWriter`.  

If you use simple inheritance, you’ll end up with:  
- `ConsoleErrorLogger`, `FileErrorLogger`, `DatabaseErrorLogger`  
- `ConsoleInfoLogger`, `FileInfoLogger`, `DatabaseInfoLogger`  
- and so on… 🚨 **class explosion**.  
- **Basically for each combination we have to create new subclass which will increase in number very rapidly**

---

## 🔹 Bridge Pattern Solution  
- Define a `LogWriter` interface (abstraction for output).  
- Define `Logger` hierarchy (abstraction for log levels).  
- Each `Logger` **uses** a `LogWriter` → decoupling message type from message destination.  

---

## 🔹 Example in Java  

```java
// Implementor
Mentioned in directory 
```

---

## 🔹 Benefits
- No class explosion → you can mix any log type with any log destination.  
- New log destinations (`KafkaLogWriter`) or log types (`DebugLogger`) can be added **independently**.  
- Keeps system extensible and flexible.  

---

## 🔹 Follow-up Questions (Interview)
1. How is this different from Adapter?  
   - Adapter is for making an existing class fit a required interface.  
   - Bridge is for **separating two changing dimensions** (log type and log destination).  

2. Where else can Bridge be applied in backend systems?  
   - Payment systems → `PaymentType` (CreditCard, UPI) + `PaymentGateway` (Stripe, Razorpay).  
   - Messaging → `Message` (Email, SMS) + `Channel` (SMTP, Twilio, Kafka).  
   - Caching → `Cache` (Local, Distributed) + `Storage` (Redis, Memcached, DB).  


### Another Example of Bridge Using UML

* We have Remote, we can have different type of remote, basic advance etc.
* We can devices, various kinds like Radio or TV.
* Remote interface will take device as input and based on its method provide functionality in abstract class
* Or it can provide more or less functionality in sub-classes(Remote).

![Alt text](/src/main/java/org/example/design_patterns/bridge/BridgeExample.png)