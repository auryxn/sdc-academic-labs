package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FactoryMainPage extends FactoryPage {

    // --- Статические элементы (инициализируются один раз через PageFactory) ---
    @FindBy(css = "img[alt='ParaBank']")
    private WebElement paraBankLogo;

    @FindBy(linkText = "Register")
    private WebElement registerLink;

    @FindBy(linkText = "Log Out")
    private WebElement logoutLink;

    @FindBy(linkText = "About Us")
    private WebElement aboutUsLink;

    @FindBy(linkText = "Contact Us")
    private WebElement contactUsLink;

    @FindBy(linkText = "Accounts Overview")
    private WebElement accountsOverviewLink;

    // --- Динамические элементы (ищем каждый раз через getDriver, чтобы избежать StaleElementReference) ---

    public FactoryContactUsPage clickContactUsLink() {
        contactUsLink.click();
        return new FactoryContactUsPage();
    }

    public boolean isLogoDisplayed() {
        return paraBankLogo.isDisplayed();
    }

    public FactoryRegisterPage clickRegisterLink() {
        registerLink.click();
        return new FactoryRegisterPage();
    }

    public boolean isLoginUsernameFieldDisplayed() {
        return getDriver().findElement(By.cssSelector("input[name='username']")).isDisplayed();
    }

    public void login(String username, String password) {
        WebElement userField = getDriver().findElement(By.cssSelector("input[name='username']"));
        WebElement passField = getDriver().findElement(By.cssSelector("input[name='password']"));
        userField.clear();
        userField.sendKeys(username);
        passField.clear();
        passField.sendKeys(password);
        getDriver().findElement(By.cssSelector("input[value='Log In']")).click();
    }

    public String getLoginErrorText() {
        return getDriver().findElement(By.cssSelector("p.error")).getText();
    }

    public String getLoginErrorColor() {
        return getDriver().findElement(By.cssSelector("p.error")).getCssValue("color");
    }

    public void clickLogout() {
        logoutLink.click();
    }

    public void clickAboutUs() {
        aboutUsLink.click();
    }

    public void clickAccountsOverview() {
        accountsOverviewLink.click();
    }

    public String getUsernameFieldValue() {
        return getDriver().findElement(By.cssSelector("input[name='username']")).getAttribute("value");
    }

    public String getPasswordFieldValue() {
        return getDriver().findElement(By.cssSelector("input[name='password']")).getAttribute("value");
    }
}
