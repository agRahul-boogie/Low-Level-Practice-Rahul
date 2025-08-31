package org.learn.design_patterns.composite;

import org.learn.design_patterns.composite.impl.Permission;
import org.learn.design_patterns.composite.impl.Role;

public class CompositeRunner {
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
