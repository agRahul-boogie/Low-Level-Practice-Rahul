# There are few Good to know things


## Few signs which we should know(at least when it comes to class diagram)
### If we are going to revise System design from refactoring guru then this is must.

```declarative
-------------------------
|      ClassName        | --> put class name here
-------------------------
| - attribute1: Type    |
| + attribute2: Type    | --> put variable here
-------------------------
| + method1(param): Type|
| - method2(): void     | --> put methods here
-------------------------

Below are signs represented various access level of attribute/methods
+ → public

- → private

# → protected

~ → package/default

```

### Other symbols which we should know


![Alt text](/src/main/java/org/learn/generic/UmlSigns.png)


A -----> B   -----> meaninng A is simply implementing B

the second A --->B  means A is extending B, A is-a B means A is B's kind, like Dog is-a Animal

A--------B  meaning B's interface is injected into A

A---------B(solid line)  means A and B call each other

rest of them are self understanding
second last: aggregation implies a relationship where the child can exist independently of the parent. for example Aircraft can exist without Airline.

last: Composition example : Room doesn’t exist meaningfully without a House.

![Alt text](/src/main/java/org/learn/generic/Interface.png)
Representation of interface and it's methods above

--------------------------------





