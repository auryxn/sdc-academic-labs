package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import com.stv.factory.factorypages.FactoryMainPage;
import com.stv.factory.factorypages.FactoryRegisterPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class LoginSteps extends BasicTest {

    private FactoryMainPage mainPage = new FactoryMainPage();

    @When("I enter username {string} and password {string}")
    public void iEnterUsernameAndPassword(String username, String password) {
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    @When("I click the Login button")
    public void iClickTheLoginButton() {
        WebElement loginButton = driver.findElement(By.cssSelector("input[value='Log In']"));
        loginButton.click();
    }

    @Then("I should see the Account Overview page with a welcome message")
    public void iShouldSeeTheAccountOverviewPageWithAWelcomeMessage() {
        WebElement welcomeMessage = driver.findElement(By.className("smallText"));
        Assert.assertTrue(welcomeMessage.isDisplayed(), "Welcome message should be displayed");
        Assert.assertTrue(welcomeMessage.getText().contains("Welcome"), "Welcome message should contain 'Welcome'");
    }
}
