package com.stv.bdd;

import com.stv.design.designtests.BasicTest;
import io.cucumber.java.en.Given;

public class CommonSteps extends BasicTest {

    @Given("I am on the ParaBank home page")
    public void iAmOnTheParaBankHomePage() {
        getDriver().get("https://parabank.parasoft.com/parabank/index.htm");
    }
}
