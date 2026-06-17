package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import com.stv.factory.factorypages.FactoryMainPage;
import com.stv.factory.factorypages.FactoryRegisterPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class RegistrationSteps extends BasicTest {

    private FactoryMainPage mainPage = new FactoryMainPage();

    @When("I click the Register link")
    public void iClickTheRegisterLink() {
        getDriver().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        getDriver().findElement(org.openqa.selenium.By.linkText("Register")).click();
    }

    @Then("I should see the registration page with title {string}")
    public void iShouldSeeTheRegistrationPageWithTitle(String expectedTitle) {
        FactoryRegisterPage registerPage = new FactoryRegisterPage();
        Assert.assertTrue(registerPage.isTitleDisplayed(), "Register page title should be displayed");
        Assert.assertEquals(registerPage.getTitleText(), expectedTitle, "Register page title text is incorrect");
    }
}
