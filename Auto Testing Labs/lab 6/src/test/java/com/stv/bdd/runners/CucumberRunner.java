package com.stv.bdd.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.stv.bdd.steps",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        tags = "@Smoke"
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
}
