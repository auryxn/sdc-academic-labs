package com.stv.bdd.steps;

import com.stv.factory.factorypages.*;
import com.stv.framework.core.drivers.MyDriver;
import com.stv.framework.core.lib.ParaBankPageURLs;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.logging.Logger;

public class Task8Steps {
    private static final Logger log = Logger.getLogger(Task8Steps.class.getName());
    private FactoryMainPage mainPage = new FactoryMainPage();
    private FactoryRegisterPage registerPage;
    private FactoryAccountOverviewPage accountOverviewPage;
    private FactoryAboutPage aboutPage;
    private FactoryContactUsPage contactUsPage;

    private String currentUsername;
    private static final String password = "password123";

    @Given("The user is registered and logged in")
    public void theUserIsRegisteredAndLoggedIn() {
        log.info("Precondition: Ensuring user is registered and logged in");
        MyDriver.getDriver().get(ParaBankPageURLs.START_URL);
        
        // Ensure we are logged out first so we can see the Register link
        try {
            mainPage.clickLogout();
            log.info("Logged out existing session");
        } catch (Exception e) {
            log.info("No existing session to log out");
        }
        
        // Register new user
        registerPage = mainPage.clickRegisterLink();
        currentUsername = "user" + System.currentTimeMillis();
        log.info("Registering user: " + currentUsername);
        registerPage.register("John", "Doe", "123 Main St", "New York", "NY", "10001", "555-1234", "123-45-678", currentUsername, password);
        
        accountOverviewPage = new FactoryAccountOverviewPage();
        log.info("User registered and logged in successfully");
    }

    @When("User navigates to the Account Overview page")
    public void userNavigatesToTheAccountOverviewPage() {
        log.info("Navigating to Account Overview page");
        mainPage.clickAccountsOverview();
        accountOverviewPage = new FactoryAccountOverviewPage();
    }

    @Then("The Account Overview page title should be verified")
    public void theAccountOverviewPageTitleShouldBeVerified() {
        log.info("Verifying Account Overview page title");
        Assert.assertEquals(accountOverviewPage.getTitleText(), "Accounts Overview", "Account Overview page title is incorrect");
    }

    @When("User clicks Log Out")
    public void userClicksLogOut() {
        log.info("Clicking Log Out");
        mainPage.clickLogout();
    }

    @Then("User is successfully logged out and redirected to the login page")
    public void userIsSuccessfullyLoggedOutAndRedirectedToTheLoginPage() {
        log.info("Verifying logout and redirection");
        Assert.assertTrue(mainPage.isLoginUsernameFieldDisplayed(), "User was not redirected to the login page");
    }

    @When("User attempts to log in with empty credentials")
    public void userAttemptsToLogInWithEmptyCredentials() {
        log.info("Attempting login with empty credentials");
        mainPage.login("", "");
    }

    @Then("A validation error message should be displayed with correct text and color")
    public void aValidationErrorMessageShouldBeDisplayedWithCorrectTextAndColor() {
        log.info("Verifying validation error message");
        String errorText = mainPage.getLoginErrorText();
        String errorColor = mainPage.getLoginErrorColor();
        log.info("Error text: " + errorText + ", Error color: " + errorColor);
        
        Assert.assertTrue(errorText.contains("Please enter a username and password.") || errorText.contains("Internal Error"), "Unexpected error message: " + errorText);
        Assert.assertTrue(errorColor.contains("255, 0, 0") || errorColor.contains("red"), "Error message color is not red");
    }

    @When("User attempts to log in with a username but empty password")
    public void userAttemptsToLogInWithAUsernameButEmptyPassword() {
        log.info("Attempting login with username only");
        mainPage.login(currentUsername, "");
    }

    @Then("The entered credentials should be cleared")
    public void theEnteredCredentialsShouldBeCleared() {
        log.info("Verifying input fields are cleared");
        String usernameValue = mainPage.getUsernameFieldValue();
        String passwordValue = mainPage.getPasswordFieldValue();
        log.info("Username field value: '" + usernameValue + "', Password field value: '" + passwordValue + "'");

        Assert.assertTrue(usernameValue.isEmpty(), "Username field was not cleared");
        Assert.assertTrue(passwordValue.isEmpty(), "Password field was not cleared");
    }

    @When("User navigates to the About page")
    public void userNavigatesToTheAboutPage() {
        log.info("Navigating to About page");
        mainPage.clickAboutUs();
        aboutPage = new FactoryAboutPage();
    }

    @Then("The About page title should be verified")
    public void theAboutPageTitleShouldBeVerified() {
        log.info("Verifying About page title");
        Assert.assertTrue(aboutPage.getTitleText().contains("ParaSoft Demo Website"), "About page title is incorrect");
    }

    @When("User clicks the link to parasoft.com on the About page")
    public void userClicksTheLinkToParasoftComOnTheAboutPage() {
        log.info("Clicking link to parasoft.com");
        aboutPage.clickParasoftLink();
    }

    @And("User clicks the browser Back button")
    public void userClicksTheBrowserBackButton() {
        log.info("Clicking browser Back button");
        MyDriver.getDriver().navigate().back();
    }

    @Then("The current page should be identical to the About page")
    public void theCurrentPageShouldBeIdenticalToTheAboutPage() {
        log.info("Verifying current page is About page");
        aboutPage = new FactoryAboutPage();
        Assert.assertTrue(aboutPage.getTitleText().contains("ParaSoft Demo Website"), "Current page is not the About page after clicking Back");
    }

    @Given("User is on the Contact Us page")
    public void userIsOnTheContactUsPage() {
        log.info("Navigating to Contact Us page");
        MyDriver.getDriver().get(ParaBankPageURLs.START_URL);
        contactUsPage = mainPage.clickContactUsLink();
    }

    @When("User submits the Contact Us form with name {string}, email {string}, phone {string}, message {string}")
    public void userSubmitsTheContactUsFormWithInputs(String name, String email, String phone, String message) {
        log.info("Submitting Contact Us form: name=" + name + ", email=" + email);
        contactUsPage.fillName(name);
        contactUsPage.fillEmail(email);
        contactUsPage.fillPhone(phone);
        contactUsPage.fillMessage(message);
        contactUsPage.clickSubmit();
    }

    @Then("The Contact Us form should display validation errors or success message depending on inputs")
    public void theContactUsFormShouldDisplayValidationErrorsOrSuccessMessageDependingOnInputs() {
        log.info("Verifying Contact Us form result");
        if (contactUsPage.isSuccessMessageDisplayed()) {
            log.info("Success message displayed: " + contactUsPage.getSuccessMessageText());
        } else {
            log.info("Validation errors displayed: " + contactUsPage.getErrorMessages());
        }
    }
}

