package org.learn.design_patterns.structural.composite.impl;

import org.learn.design_patterns.structural.composite.PermissionComponent;

public class Permission implements PermissionComponent {
    private final String name;

    public Permission(String name) {
        this.name = name;
    }

    @Override
    public void showPermissions() {
        System.out.println("Permission: " + name);
    }
}
