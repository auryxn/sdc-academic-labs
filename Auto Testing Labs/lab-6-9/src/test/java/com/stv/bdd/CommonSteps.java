package com.stv.bdd;

import com.stv.design.designtests.BasicTest;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

public class CommonSteps extends BasicTest {

    private WebDriver driver = getDriver();

    @Given("I am on the ParaBank home page")
    public void iAmOnTheParaBankHomePage() {
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
    }
}
