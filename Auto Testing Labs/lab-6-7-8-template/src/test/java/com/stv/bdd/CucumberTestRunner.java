package com.stv.bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;

import static com.stv.framework.core.drivers.MyDriver.getDriver;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.stv.bdd.steps",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    @AfterClass
    public void tearDown() {
        getDriver().quit();
    }
}

