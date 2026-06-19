package com.stv.bdd.steps;

import com.stv.factory.factorypages.FactoryMainPage;
import com.stv.factory.factorypages.FactoryRegisterPage;
import com.stv.framework.core.drivers.MyDriver;
import com.stv.framework.core.lib.ParaBankPageURLs;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class MainPageSteps {
    private FactoryMainPage mainPage = new FactoryMainPage();
    private FactoryRegisterPage registerPage;

    @Given("User is on the Main page")
    public void userIsOnTheMainPage() {
        MyDriver.getDriver().get(ParaBankPageURLs.START_URL);
    }

    @When("User clicks on the Register link")
    public void userClicksOnTheRegisterLink() {
        registerPage = mainPage.clickRegisterLink();
    }

    @Then("User is redirected to the Registration page")
    public void userIsRedirectedToTheRegistrationPage() {
        Assert.assertTrue(registerPage.isTitleDisplayed(), "Registration page title is not displayed");
        Assert.assertEquals(registerPage.getTitleText(), "Signing up is easy!", "Registration page title text is incorrect");
    }

    @Then("ParaBank logo should be displayed")
    public void parabankLogoShouldBeDisplayed() {
        Assert.assertTrue(mainPage.isLogoDisplayed(), "ParaBank logo is not displayed");
    }
}

