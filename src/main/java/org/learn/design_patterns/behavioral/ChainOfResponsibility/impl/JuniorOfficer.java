package org.learn.design_patterns.behavioral.ChainOfResponsibility.impl;

import org.learn.design_patterns.behavioral.ChainOfResponsibility.LoanHandler;

public class JuniorOfficer extends LoanHandler {

    @Override
    public void handleRequest(double amount) {
        if(amount <= 10000) System.out.println("Junior approved: " + amount);
        else if(nextHandler != null) nextHandler.handleRequest(amount);
    }
}