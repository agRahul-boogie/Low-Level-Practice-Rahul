package org.learn.design_patterns.structural.proxy.impl;

import org.learn.design_patterns.structural.proxy.ReportGenerator;

public class RealReportGenerator implements ReportGenerator {
    public String generateReport(String userId) {
        System.out.println("Fetching report from DB for user: " + userId);
        return "Report Data for " + userId;
    }
}
