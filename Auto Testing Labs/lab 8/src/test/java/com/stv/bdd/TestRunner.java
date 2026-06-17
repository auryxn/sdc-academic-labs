package com.stv.bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.stv.bdd.stepdefinitions",
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LogManager.getLogger(TestRunner.class);

    @BeforeSuite
    public void beforeSuite() {
        logger.info("========================================");
        logger.info("  Lab 8 - BDD Individual Task Started   ");
        logger.info("  Scenario: Customer Lookup Form Flow   ");
        logger.info("========================================");
    }

    @AfterSuite
    public void afterSuite() {
        logger.info("========================================");
        logger.info("  Lab 8 - BDD Individual Task Complete  ");
        logger.info("========================================");
        logger.info("HTML Report: target/cucumber-report.html");
        logger.info("Extent Report: target/ExtentReport.html");
    }
}
