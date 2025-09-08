# 🎯 Structural Design Patterns – Revision Cheat Sheet

This file contains quick definitions, backend-friendly examples, and analogies for structural design patterns we covered.

---

## 1. Adapter Pattern
- **Intent:** Convert one interface into another expected by the client.
- **When to use:** When two incompatible systems need to work together.
- **Backend Example:** Wrap a 3rd party payment API (`Stripe`) to match your internal `PaymentProcessor` interface.
- **Analogy:** Power adapter (EU plug to US socket).

---

## 2. Bridge Pattern
- **Intent:** Decouple abstraction from implementation so they can vary independently.
- **When to use:** When you have multiple dimensions of variation.
- **Backend Example:** Notification system → Abstraction (`Notification`) vs. Implementation (`EmailSender`, `SMSSender`).
- **Analogy:** Remote control (abstraction) works with different TV brands (implementation).

---

## 3. Composite Pattern
- **Intent:** Treat individual objects and compositions of objects uniformly.
- **When to use:** When you have hierarchical structures (part-whole).
- **Backend Example:** Organization structure → Employee (leaf) vs Department (composite).
- **Analogy:** Files and folders in a filesystem.

---

## 4. Decorator Pattern
- **Intent:** Add behavior dynamically to objects without altering their code.
- **When to use:** When you want flexible runtime augmentation instead of subclassing.
- **Backend Example:** Wrap a service with logging, caching, or compression decorators.
- **Analogy:** Adding toppings to a pizza, one layer at a time.

---

## 5. Facade Pattern
- **Intent:** Provide a simplified interface to a complex subsystem.
- **When to use:** When you want to hide complexity from the client.
- **Backend Example:** `OrderFacade` hides complexity of interacting with `InventoryService`, `PaymentService`, and `ShippingService`.
- **Analogy:** A restaurant waiter who takes your order instead of talking directly to the chef, cashier, etc.

---

## 6. Proxy Pattern
- **Intent:** Provide a placeholder or surrogate to control access to another object.
- **When to use:** When you want to add access control, caching, lazy loading, or remote calls.
- **Backend Example:**
    - Spring AOP Proxies (`@Transactional`, `@Cacheable`)
    - Hibernate Lazy Loading
    - gRPC/RMI stubs
- **Analogy:** A personal assistant who speaks on behalf of their boss.

---

# 🔍 Comparison Table

| Pattern    | Main Purpose | Key Idea | Backend Example | Analogy |
|------------|-------------|----------|----------------|---------|
| **Adapter** | Compatibility | Convert one interface to another | Stripe API adapter | Power plug adapter |
| **Bridge**  | Abstraction-Implementation separation | Decouple abstraction from implementation | Notifications (Email/SMS) | Remote & TV |
| **Composite** | Hierarchical structure | Treat part & whole uniformly | Employee & Department | Files & Folders |
| **Decorator** | Add new behavior dynamically | Wrap object without subclassing | Logging/Caching wrappers | Pizza toppings |
| **Facade** | Simplify subsystem | Unified higher-level interface | OrderFacade for services | Restaurant waiter |
| **Proxy** | Control access | Placeholder with extra logic | Spring AOP, Hibernate, gRPC stub | Assistant / Spokesperson |

---
