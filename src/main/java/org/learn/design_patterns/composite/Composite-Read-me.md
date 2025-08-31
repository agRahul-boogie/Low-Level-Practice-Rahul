# 📖 Composite Pattern – Simple Definition

The Composite Pattern lets you treat individual objects and groups of objects in the same way.

It’s used for tree-like structures where a whole is made up of parts, and the parts can themselves be wholes.

If we have a hierarchy where
* **Base** objects exist (simple things), and
* **Complex** objects are formed by combining those base and complex objects,

then instead of making the caller treat them differently, we let the complex object implement the same interface as the base.
This way, the caller always deals with one common interface, making the design simpler and more consistent.

💡 Example in plain words:
A File and a Folder can both be asked to “show their name.” A file just prints its name, while a folder prints its name and asks all its children to do the same.
But to the caller, both look the same because they implement the same interface.



## 🌱 Tiny Example (File System)

File = a leaf (cannot contain other objects).

Folder = a composite (can contain files or other folders).

Both File and Folder share the same interface:

```java


interface FileSystemComponent {
void showName();
}

class File implements FileSystemComponent {
private String name;
public File(String name) { this.name = name; }
public void showName() {
System.out.println("File: " + name);
}
}

class Folder implements FileSystemComponent {
private String name;
private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) { this.name = name; }

    public void add(FileSystemComponent c) { children.add(c); }

    public void showName() {
        System.out.println("Folder: " + name);
        for (FileSystemComponent c : children) {
            c.showName();
        }
    }
}
```

Usage

```java


public class Main {
public static void main(String[] args) {
File f1 = new File("resume.docx");
File f2 = new File("budget.xlsx");

        Folder personal = new Folder("Personal");
        personal.add(f1);
        personal.add(f2);

        Folder root = new Folder("Root");
        root.add(personal);
        root.showName();
    }
}
```
```
Output
Folder: Root
Folder: Personal
File: resume.docx
File: budget.xlsx
```

👉 Key Idea: Both File and Folder can be treated as FileSystemComponent.
That’s the Composite pattern in action.

# 🌳 Composite Pattern – Backend Example

## 🔹 Problem Statement (Backend Context)  
You’re building a **role-based access control (RBAC)** system.  

- A **User** can be assigned individual **Permissions** (e.g., `READ`, `WRITE`).  
- A **Role** (like `Admin`, `Editor`, `Viewer`) is essentially a **collection of permissions**.  
- Roles can also contain **other roles** (e.g., `SuperAdmin` contains `Admin` + extra permissions).  

You want a **uniform way** to treat both **single permissions** and **groups of permissions** (roles).  

---

## 🔹 Composite Pattern Solution  
- Define a common interface `PermissionComponent` with method `showPermissions()`.  
- `Permission` (leaf) implements this interface.  
- `Role` (composite) also implements this interface, but internally manages a collection of `PermissionComponent` objects.  

This way, you can treat a single `Permission` or a `Role` uniformly.  

---

## 🔹 UML (simplified)
```
PermissionComponent (interface)
        ▲
  ┌─────┴─────┐
Permission   Role
   (Leaf)   (Composite -> contains multiple PermissionComponents)
```

---

## 🔹 Example in Java  

```java
// Component
interface PermissionComponent {
    void showPermissions();
}

// Leaf
class Permission implements PermissionComponent {
    private String name;
    public Permission(String name) {
        this.name = name;
    }
    public void showPermissions() {
        System.out.println("Permission: " + name);
    }
}

// Composite
class Role implements PermissionComponent {
    private String name;
    private List<PermissionComponent> permissions = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    public void add(PermissionComponent component) {
        permissions.add(component);
    }

    public void remove(PermissionComponent component) {
        permissions.remove(component);
    }

    public void showPermissions() {
        System.out.println("Role: " + name);
        for (PermissionComponent pc : permissions) {
            pc.showPermissions();
        }
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        // Permissions
        Permission read = new Permission("READ");
        Permission write = new Permission("WRITE");
        Permission delete = new Permission("DELETE");

        // Roles
        Role editor = new Role("Editor");
        editor.add(read);
        editor.add(write);

        Role admin = new Role("Admin");
        admin.add(editor);   // Admin contains Editor role
        admin.add(delete);

        // Display all permissions for Admin
        admin.showPermissions();
    }
}
```

---

## 🔹 Output
```
Role: Admin
Role: Editor
Permission: READ
Permission: WRITE
Permission: DELETE
```

---

## 🔹 Benefits
- Treats **individual objects** (permissions) and **composites** (roles) uniformly.  
- Easy to add new permission types or nested roles.  
- Great for **hierarchical structures** (menus, file systems, organizations, RBAC).  

---

## 🔹 Follow-up Questions (Interview)
1. Where else can Composite be applied in backend systems?  
   - **File system APIs** → File (leaf) vs Folder (composite).  
   - **Organization hierarchy** → Employee (leaf) vs Manager (composite).  
   - **Catalogs** → Product (leaf) vs Category (composite).  

2. How is this different from Decorator?  
   - **Composite** → tree structure (whole-part hierarchy).  
   - **Decorator** → adds extra behavior dynamically without a hierarchy.  

3. What are the trade-offs?  
   - Makes hierarchy traversal easy, but can complicate object creation.  
