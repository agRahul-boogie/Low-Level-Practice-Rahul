package org.learn.design_patterns.behavioral.ChainOfResponsibility;

import org.learn.design_patterns.behavioral.ChainOfResponsibility.impl.JuniorOfficer;
import org.learn.design_patterns.behavioral.ChainOfResponsibility.impl.Manager;
import org.learn.design_patterns.behavioral.ChainOfResponsibility.impl.SeniorOfficer;

public class CoRRunner {

    public static void main(String[] args) {
        JuniorOfficer junior = new JuniorOfficer();
        SeniorOfficer senior = new SeniorOfficer();
        Manager manager = new Manager();
        junior.setNextHandler(senior);
        senior.setNextHandler(manager);


        junior.handleRequest(7500); // Junior approves
        junior.handleRequest(30000); // Senior approves
        junior.handleRequest(100000); // Manager approves
    }
}
