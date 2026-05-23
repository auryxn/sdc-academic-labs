package com.stv.bdd.steps;

import com.stv.factory.factorypages.FactoryLoginPage;
import com.stv.factory.factorypages.FactoryMainPage;
import com.stv.factory.factorypages.FactoryRegisterPage;
import com.stv.framework.core.drivers.MyDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import static com.stv.framework.core.lib.ParaBankPageURLs.START_URL;

public class ParaBankSteps {

    private final FactoryMainPage mainPage = new FactoryMainPage();
    private FactoryRegisterPage registerPage;
    private FactoryLoginPage loginPage;

    @Given("I am on the ParaBank main page")
    public void iAmOnTheParaBankMainPage() {
        MyDriver.getDriver().get(START_URL);
    }

    @Then("the ParaBank logo should be visible")
    public void theParaBankLogoShouldBeVisible() {
        Assert.assertTrue(mainPage.isLogoDisplayed(), "Logo should be visible");
    }

    @When("I click the Register link")
    public void iClickTheRegisterLink() {
        registerPage = mainPage.clickRegisterLink();
    }

    @Then("{string} registration title should be displayed")
    public void registrationTitleShouldBeDisplayed(String expectedTitle) {
        Assert.assertTrue(registerPage.isTitleDisplayed(), "Register title should be displayed");
        Assert.assertEquals(registerPage.getTitleText(), expectedTitle);
    }

    @When("I enter username {string}")
    public void iEnterUsername(String username) {
        loginPage = new FactoryLoginPage();
        loginPage.enterUsername(username);
    }

    @When("I enter password {string}")
    public void iEnterPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("I click Log In")
    public void iClickLogIn() {
        loginPage.clickLogin();
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String text) {
        String errorText = loginPage.getErrorMessageText();
        Assert.assertTrue(errorText.toLowerCase().contains(text.toLowerCase()),
                "Error should contain '" + text + "', but was: " + errorText);
    }
}
