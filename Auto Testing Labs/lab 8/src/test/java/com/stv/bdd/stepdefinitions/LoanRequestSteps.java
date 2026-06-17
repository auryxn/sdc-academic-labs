package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class LoanRequestSteps extends BasicTest {

    private static final Logger logger = LogManager.getLogger(LoanRequestSteps.class);
    private WebDriver driver = getDriver();

    @When("I navigate to the Request Loan page")
    public void iNavigateToRequestLoan() {
        logger.info("Navigating to Request Loan page");
        driver.findElement(By.linkText("Request Loan")).click();
    }

    @When("I enter loan amount {string}")
    public void iEnterLoanAmount(String amount) {
        driver.findElement(By.id("amount")).sendKeys(amount);
        logger.debug("Entered loan amount: {}", amount);
    }

    @When("I enter down payment {string}")
    public void iEnterDownPayment(String downPayment) {
        driver.findElement(By.id("downPayment")).sendKeys(downPayment);
        logger.debug("Entered down payment: {}", downPayment);
    }

    @When("I click the Apply Now button")
    public void iClickApplyNow() {
        driver.findElement(By.cssSelector("input[value='Apply Now']")).click();
        logger.info("Clicked Apply Now button");
    }

    @Then("I should see a loan response with status {string}")
    public void iShouldSeeLoanResponse(String expectedStatus) {
        WebElement statusElement = driver.findElement(By.id("loanStatus"));
        String actualStatus = statusElement.getText().trim();
        Assert.assertEquals(actualStatus, expectedStatus,
                "Loan status should match. Expected: " + expectedStatus + ", Actual: " + actualStatus);
        logger.info("Loan status: {} (expected: {})", actualStatus, expectedStatus);
    }
}
