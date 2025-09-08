package org.learn.design_patterns.structural.proxy.impl;

import org.learn.design_patterns.structural.proxy.ReportGenerator;

import java.util.HashMap;
import java.util.Map;

public class ReportGeneratorProxy implements ReportGenerator {
    private RealReportGenerator realReportGenerator;
    private Map<String, String> cache = new HashMap<>();

    public String generateReport(String userId) {
        if (cache.containsKey(userId)) {
            System.out.println("Returning cached report for user: " + userId);
            return cache.get(userId);
        }
        if (realReportGenerator == null) {
            realReportGenerator = new RealReportGenerator();
        }
        String report = realReportGenerator.generateReport(userId);
        cache.put(userId, report);
        return report;
    }
}