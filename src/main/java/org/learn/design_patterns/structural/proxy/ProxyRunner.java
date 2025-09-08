package org.learn.design_patterns.structural.proxy;

import org.learn.design_patterns.structural.proxy.impl.ReportGeneratorProxy;

public class ProxyRunner {
    public static void main(String[] args) {
        ReportGenerator proxy = new ReportGeneratorProxy();
        proxy.generateReport("user123"); // DB call
        proxy.generateReport("user123"); // Cached call
    }
}