# 🕵️ Proxy Pattern – Backend Example & Interview Q&A

## 📝 Quick Revision (Layman’s Definition + Analogy)

- **Definition**: Proxy is a structural design pattern where a **substitute object** controls access to a real object.  
- **Analogy**: A **credit card** is a proxy for your **bank account**. Instead of dealing with the bank directly, you use the card, which may add checks like limits or fraud detection.  
- **Backend Use Case**: Caching expensive DB calls, controlling access to services, lazy loading, logging, monitoring, or remote service stubs.  

---

## 🔹 Backend Example (Caching Proxy)

```java
// Subject Interface
interface ReportGenerator {
    String generateReport(String userId);
}

// Real Subject
class RealReportGenerator implements ReportGenerator {
    public String generateReport(String userId) {
        System.out.println("Fetching report from DB for user: " + userId);
        return "Report Data for " + userId;
    }
}

// Proxy
class ReportGeneratorProxy implements ReportGenerator {
    private RealReportGenerator realReportGenerator;
    private Map<String, String> cache = new HashMap<>();

    public String generateReport(String userId) {
        if (cache.containsKey(userId)) {
            System.out.println("Returning cached report for user: " + userId);
            return cache.get(userId);
        }
        if (realReportGenerator == null) {
            realReportGenerator = new RealReportGenerator();
        }
        String report = realReportGenerator.generateReport(userId);
        cache.put(userId, report);
        return report;
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        ReportGenerator proxy = new ReportGeneratorProxy();
        proxy.generateReport("user123"); // DB call
        proxy.generateReport("user123"); // Cached call
    }
}
```

**Output:**
```
Fetching report from DB for user: user123
Returning cached report for user: user123
```

---

# 🎤 Proxy Pattern – Interview Q&A

### 1. What is the Proxy Pattern?
Proxy is a **structural design pattern** where a class (Proxy) acts as a **substitute for another class (Real Subject)**.  
The proxy implements the same interface and forwards requests, but it can also add **extra behavior** like security checks, logging, caching, or lazy loading.  

---

### 2. How is Proxy different from Facade?
- **Facade** → simplifies a complex subsystem by exposing a unified interface.  
- **Proxy** → controls access to a single object without changing its interface.  

---

### 3. What are real-world backend examples of Proxy?
- **Caching Proxy**: Cache DB queries or API responses (like Redis layer in front of DB).  
- **Protection Proxy**: Check user roles/permissions before accessing a resource.  
- **Remote Proxy**: Stub representing a service in another JVM/machine (e.g., RMI, gRPC stub).  
- **Lazy Initialization Proxy (Virtual Proxy)**: Load a heavy resource (e.g., report, image, big file) only when requested.  

---

### 4. What are the different types of Proxy?
1. **Virtual Proxy** – instantiate resource only when needed.  
2. **Remote Proxy** – represent an object on another machine.  
3. **Protection Proxy** – add security (authorization checks).  
4. **Caching Proxy** – cache results for faster response.  
5. **Logging/Monitoring Proxy** – log calls or measure performance.  

---

### 5. What’s the benefit of Proxy over directly using the real object?
- Adds extra logic without changing the client or real object.  
- Improves **performance** (via caching/lazy loading).  
- Improves **security** (via access checks).  
- Provides **abstraction for remote objects**.  

---

### 6. Does Proxy follow “is-a” or “has-a” relationship?
- Proxy **is-a** Real Subject (because it implements the same interface).  
- But internally, it **has-a** reference to the real object (delegation).  

---

### 7. How does Proxy improve maintainability?
Since client code talks to the **Proxy interface**, you can change the behavior (e.g., add caching, security, logging) without touching the client or the real subject.  

---

### 8. Can Proxy be misused?
Yes. Too many layers of proxies → performance overhead, harder debugging.  
Also, if proxy logic grows too complex, it may duplicate real subject’s responsibility.  

---

### 9. How is Proxy different from Decorator?
- **Proxy**: Controls access to an object. Its main intent is **indirection**.  
- **Decorator**: Adds new **responsibilities/behaviors dynamically** without changing the object’s code.  
- Example:  
  - **Proxy** → cache DB query results.  
  - **Decorator** → add encryption to report output.  

---

### 10. Where have you seen Proxy in Java frameworks?
- **Spring AOP Proxies**: Spring creates proxies around beans to add cross-cutting concerns (transactions, security, logging).  
- **Hibernate Lazy Loading**: Entities are proxied; real DB fetch happens only when accessed.  
- **RMI/gRPC stubs**: Remote calls are proxied by generated classes.  
