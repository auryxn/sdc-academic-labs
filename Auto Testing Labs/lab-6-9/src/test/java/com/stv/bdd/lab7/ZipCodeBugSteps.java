package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class ZipCodeBugSteps extends BasicTest {

    @Given("I am on the ParaBank registration page")
    public void iAmOnTheParaBankRegistrationPage() {
        getDriver().get("https://parabank.parasoft.com/parabank/register.htm");
    }

    @When("I enter non-numeric characters {string} in the Zip Code field")
    public void iEnterNonNumericCharactersInTheZipCodeField(String zipInput) {
        WebElement zipField = getDriver().findElement(By.id("customer.address.zipCode"));
        zipField.clear();
        zipField.sendKeys(zipInput);
    }

    @When("I submit the registration form with valid data")
    public void iSubmitTheRegistrationFormWithValidData() {
        // Fill all required fields with valid data
        getDriver().findElement(By.id("customer.firstName")).sendKeys("Test");
        getDriver().findElement(By.id("customer.lastName")).sendKeys("User");
        getDriver().findElement(By.id("customer.address.street")).sendKeys("123 Main St");
        getDriver().findElement(By.id("customer.address.city")).sendKeys("New York");
        getDriver().findElement(By.id("customer.address.state")).sendKeys("NY");
        // Zip Code is already filled by previous step
        getDriver().findElement(By.id("customer.phoneNumber")).sendKeys("+1234567890");
        getDriver().findElement(By.id("customer.ssn")).sendKeys("123-45-6789");

        // Unique username to avoid duplicate registration errors
        String uniqueUsername = "testuser_" + System.currentTimeMillis();
        getDriver().findElement(By.id("customer.username")).sendKeys(uniqueUsername);
        getDriver().findElement(By.id("customer.password")).sendKeys("TestPass123!");
        getDriver().findElement(By.id("repeatedPassword")).sendKeys("TestPass123!");

        // Submit
        WebElement registerButton = getDriver().findElement(By.cssSelector("input[value='Register']"));
        registerButton.click();
    }

    @Then("the system should reject non-numeric zip codes and display a validation error")
    public void theSystemShouldRejectNonNumericZipCodes() {
        // The bug: system accepts non-numeric zip codes without validation error
        // This test will FAIL — proving the bug exists
        boolean hasError = false;
        try {
            WebElement errorPanel = getDriver().findElement(By.className("error"));
            if (errorPanel.isDisplayed()) {
                String errorText = errorPanel.getText();
                if (errorText.toLowerCase().contains("zip") || errorText.toLowerCase().contains("postal")) {
                    hasError = true;
                }
            }
        } catch (Exception e) {
            // No error panel found — bug is confirmed
        }

        // Assert that validation error IS displayed
        // This assertion will fail, demonstrating the bug
        Assert.assertTrue(hasError,
                "BUG CONFIRMED: Zip Code field accepts non-numeric input without validation. " +
                "Expected: error message about invalid zip code. Actual: no validation error.");
    }
}
