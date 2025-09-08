package org.learn.design_patterns.behavioral.ChainOfResponsibility;

public abstract class LoanHandler {
    public LoanHandler nextHandler;
    public void setNextHandler(LoanHandler next) { this.nextHandler = next; }
    public abstract void handleRequest(double amount);
}