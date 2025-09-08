package org.learn.design_patterns.behavioral.ChainOfResponsibility.impl;

import org.learn.design_patterns.behavioral.ChainOfResponsibility.LoanHandler;

public class Manager extends LoanHandler {
    @Override
    public void handleRequest(double amount) {
        if(amount > 50000) System.out.println("Manager approved: " + amount);
    }
}