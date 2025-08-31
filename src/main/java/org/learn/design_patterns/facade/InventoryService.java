package org.learn.design_patterns.facade;

class InventoryService {
    void updateStock(String userId) {
        System.out.println("Inventory updated for user: " + userId);
    }
}
