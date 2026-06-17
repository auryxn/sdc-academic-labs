package com.stv.bdd.stepdefinitions;

import com.stv.design.designtests.BasicTest;
import com.stv.factory.factorypages.FactoryMainPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class BillPaySteps extends BasicTest {

    private static final Logger logger = LogManager.getLogger(BillPaySteps.class);
    private WebDriver driver = getDriver();
    private FactoryMainPage mainPage = new FactoryMainPage();

    @Given("I am logged into ParaBank with valid credentials")
    public void iAmLoggedIntoParaBank() {
        logger.info("Navigating to ParaBank and logging in");
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
        driver.findElement(By.name("username")).sendKeys("john");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.cssSelector("input[value='Log In']")).click();

        WebElement welcomeMsg = driver.findElement(By.className("smallText"));
        Assert.assertTrue(welcomeMsg.isDisplayed(), "Login should be successful");
        logger.info("Successfully logged in as john");
    }

    @When("I navigate to the Bill Pay page")
    public void iNavigateToBillPay() {
        logger.info("Navigating to Bill Pay");
        driver.findElement(By.linkText("Bill Pay")).click();
    }

    @When("I enter payee name {string}")
    public void iEnterPayeeName(String name) {
        driver.findElement(By.name("payee.name")).sendKeys(name);
        logger.debug("Entered payee name: {}", name);
    }

    @When("I enter payee address {string}")
    public void iEnterPayeeAddress(String address) {
        driver.findElement(By.name("payee.address.street")).sendKeys(address);
    }

    @When("I enter payee phone number {string}")
    public void iEnterPayeePhone(String phone) {
        driver.findElement(By.name("payee.phoneNumber")).sendKeys(phone);
    }

    @When("I enter payee account number {string}")
    public void iEnterPayeeAccount(String account) {
        driver.findElement(By.name("payee.accountNumber")).sendKeys(account);
        driver.findElement(By.name("verifyAccount")).sendKeys(account);
    }

    @When("I enter payment amount {string}")
    public void iEnterPaymentAmount(String amount) {
        driver.findElement(By.name("amount")).sendKeys(amount);
        logger.debug("Entered payment amount: {}", amount);
    }

    @When("I click the Send Payment button")
    public void iClickSendPayment() {
        driver.findElement(By.cssSelector("input[value='Send Payment']")).click();
        logger.info("Clicked Send Payment button");
    }

    @Then("I should see a bill payment confirmation message")
    public void iShouldSeeConfirmation() {
        WebElement confirmation = driver.findElement(By.xpath("//*[contains(text(),'Bill Payment Complete')]"));
        Assert.assertTrue(confirmation.isDisplayed(), "Bill payment confirmation should be displayed");
        logger.info("Bill payment confirmed successfully");
    }
}
