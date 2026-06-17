package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.util.List;

public class CustomerLookupSteps extends BasicTest {

    private static final Logger logger = LogManager.getLogger(CustomerLookupSteps.class);
    private Actions actions;

    public CustomerLookupSteps() {
        this.actions = new Actions(getDriver());
    }

    // === 0:01 — Main page → click Forgot Login Info ===

    @When("I click the {string} link")
    public void iClickLink(String linkText) {
        getDriver().manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        getDriver().findElement(By.linkText(linkText)).click();
        logger.info("Clicked link: {}", linkText);
    }

    // === 0:16 — Verify Customer Lookup panel is opened ===

    @Then("the Customer Lookup panel should be visible")
    public void customerLookupPanelShouldBeVisible() {
        WebElement panel = getDriver().findElement(By.id("lookupForm"));
        Assert.assertTrue(panel.isDisplayed(), "Customer Lookup panel should be displayed");
        logger.info("Customer Lookup panel is visible");
    }

    // === 0:29 — Verify all fields are empty ===

    @Then("all fields should be empty by default")
    public void allFieldsShouldBeEmpty() {
        List<WebElement> inputs = getDriver().findElements(By.cssSelector("#lookupForm input[type='text']"));
        for (WebElement input : inputs) {
            String value = input.getAttribute("value");
            Assert.assertTrue(value == null || value.isEmpty(),
                    "Field should be empty by default: " + input.getAttribute("name"));
        }
        logger.info("All fields are empty by default");
    }

    // === 0:38 — Verify Find My Login Info button is available ===

    @Then("the {string} button should be available")
    public void buttonShouldBeAvailable(String buttonValue) {
        WebElement button = getDriver().findElement(By.cssSelector("input[value='" + buttonValue + "']"));
        Assert.assertTrue(button.isDisplayed(), "Button should be displayed");
        Assert.assertTrue(button.isEnabled(), "Button should be enabled");
        logger.info("Button '{}' is available", buttonValue);
    }

    // === 0:48 — Click Find My Login Info with empty fields ===

    @When("I click the {string} button with all fields empty")
    public void iClickButtonWithEmptyFields(String buttonValue) {
        getDriver().findElement(By.cssSelector("input[value='" + buttonValue + "']")).click();
        logger.info("Clicked '{}' with empty fields", buttonValue);
    }

    // === 0:48 — Verify error messages appear ===

    @Then("appropriate error messages should appear")
    public void errorMessagesShouldAppear() {
        List<WebElement> errors = getDriver().findElements(By.className("error"));
        Assert.assertFalse(errors.isEmpty(), "Error messages should appear");
        logger.info("Error messages appeared: {} found", errors.size());
    }

    // === 0:56 — Verify amount of messages ===

    @Then("the amount of error messages should match the number of required fields")
    public void errorMessageCountShouldMatch() {
        List<WebElement> errors = getDriver().findElements(By.className("error"));
        // All text fields: firstName, lastName, address, city, state, zipCode, ssn
        int expectedErrors = 7;
        Assert.assertEquals(errors.size(), expectedErrors,
                "Expected " + expectedErrors + " error messages but found " + errors.size());
        logger.info("Error message count matches expected: {}", expectedErrors);
    }

    // === 1:06 — Fill all fields except SSN ===

    @When("I fill all fields except SSN and click {string}")
    public void iFillAllFieldsExceptSsn(String buttonValue) {
        getDriver().findElement(By.name("firstName")).sendKeys("Test");
        getDriver().findElement(By.name("lastName")).sendKeys("User");
        getDriver().findElement(By.name("address.street")).sendKeys("123 Main St");
        getDriver().findElement(By.name("address.city")).sendKeys("New York");
        getDriver().findElement(By.name("address.state")).sendKeys("NY");
        getDriver().findElement(By.name("address.zipCode")).sendKeys("10001");
        // SSN is intentionally left empty
        logger.info("Filled all fields except SSN");
        getDriver().findElement(By.cssSelector("input[value='" + buttonValue + "']")).click();
        logger.info("Clicked '{}' after filling fields", buttonValue);
    }

    // === 1:20 — Only "SSN is required" visible ===

    @Then("only one message {string} should be visible")
    public void onlyOneMessageShouldBeVisible(String expectedMessage) {
        List<WebElement> errors = getDriver().findElements(By.className("error"));
        Assert.assertEquals(errors.size(), 1, "Expected exactly 1 error message");
        String actualText = errors.get(0).getText().trim();
        Assert.assertTrue(actualText.contains(expectedMessage),
                "Error message should contain '" + expectedMessage + "', but was: " + actualText);
        logger.info("Single error message visible: '{}'", actualText);
    }

    // === 1:31 — Previous messages disappeared ===

    @Then("the previous error messages should have disappeared")
    public void previousErrorsShouldHaveDisappeared() {
        List<WebElement> errors = getDriver().findElements(By.className("error"));
        for (WebElement err : errors) {
            String text = err.getText().toLowerCase();
            Assert.assertFalse(text.contains("first name") || text.contains("last name") ||
                            text.contains("address") || text.contains("city") ||
                            text.contains("state") || text.contains("zip"),
                    "Previous error messages should have disappeared, but found: " + err.getText());
        }
        logger.info("Previous error messages have disappeared");
    }

    // === 1:44 — SSN is required still visible ===

    @When("I click the {string} button again")
    public void iClickButtonAgain(String buttonValue) {
        getDriver().findElement(By.cssSelector("input[value='" + buttonValue + "']")).click();
        logger.info("Clicked '{}' button again", buttonValue);
    }

    @Then("the SSN is required message should still be visible")
    public void ssnRequiredMessageShouldStillBeVisible() {
        List<WebElement> errors = getDriver().findElements(By.className("error"));
        Assert.assertEquals(errors.size(), 1, "Expected exactly 1 error message");
        String actualText = errors.get(0).getText().trim();
        Assert.assertTrue(actualText.contains("Social Security Number is required"),
                "Error message should contain 'SSN is required', but was: " + actualText);
        logger.info("SSN is required message still visible: '{}'", actualText);
    }

    // === 1:53 — Enter SSN ===

    @When("I enter any SSN number and click {string}")
    public void iEnterSsnAndClick(String buttonValue) {
        getDriver().findElement(By.name("ssn")).sendKeys("123-45-6789");
        logger.info("Entered SSN");
        getDriver().findElement(By.cssSelector("input[value='" + buttonValue + "']")).click();
        logger.info("Clicked '{}' after entering SSN", buttonValue);
    }

    // === 2:03 — Customer not found error ===

    @Then("the error {string} should be displayed")
    public void errorShouldBeDisplayed(String expectedError) {
        WebElement errorPanel = getDriver().findElement(By.className("error"));
        Assert.assertTrue(errorPanel.isDisplayed(), "Error panel should be displayed");
        String actualText = errorPanel.getText().trim();
        Assert.assertTrue(actualText.contains(expectedError),
                "Expected error to contain '" + expectedError + "', but was: '" + actualText + "'");
        logger.info("Error displayed: '{}'", actualText);
    }

    // === 2:21 — Click logo → home page ===

    @When("I click on the ParaBank logo")
    public void iClickOnLogo() {
        getDriver().findElement(By.className("logo")).click();
        logger.info("Clicked on ParaBank logo");
    }

    @Then("the home page should be visible")
    public void homePageShouldBeVisible() {
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("index.htm"),
                "Should be on home page, URL: " + currentUrl);
        logger.info("Home page is visible: {}", currentUrl);
    }

    // === 2:27 — Mouse over logo → tooltip ===

    @When("I mouse over the ParaBank logo")
    public void iMouseOverLogo() {
        WebElement logo = getDriver().findElement(By.className("logo"));
        actions.moveToElement(logo).perform();
        logger.info("Mouse over ParaBank logo");
    }

    @Then("a tooltip with text {string} should be visible")
    public void tooltipShouldBeVisible(String expectedTooltip) {
        WebElement logo = getDriver().findElement(By.className("logo"));
        String title = logo.getAttribute("title");
        // Also check common tooltip attributes
        String alt = logo.getAttribute("alt");

        boolean found = (title != null && title.contains(expectedTooltip)) ||
                        (alt != null && alt.contains(expectedTooltip));

        Assert.assertTrue(found,
                "Tooltip should contain '" + expectedTooltip + "'. title='" + title + "', alt='" + alt + "'");
        logger.info("Tooltip found: '{}'", title != null ? title : alt);
    }
}
