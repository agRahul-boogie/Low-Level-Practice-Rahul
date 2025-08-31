package org.learn.design_patterns.structural.composite.impl;

import org.learn.design_patterns.structural.composite.PermissionComponent;

import java.util.ArrayList;
import java.util.List;

public class Role implements PermissionComponent {
    private final String name;
    private final List<PermissionComponent> permissions = new ArrayList<>();

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
