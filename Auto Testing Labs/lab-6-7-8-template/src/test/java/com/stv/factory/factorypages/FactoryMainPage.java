package com.stv.factory.factorypages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import java.util.List;

public class FactoryMainPage extends FactoryPage {

    @FindBy(css = "img[alt='ParaBank']")
    private WebElement paraBankLogo;

    @FindBy(linkText = "Register")
    private WebElement registerLink;

    @FindBys({
        @FindBy(id = "loginPanel"),
        @FindBy(name = "username")
    })
    private WebElement loginUsernameField;

    @FindBy(name = "password")
    private WebElement loginPasswordField;

    @FindBy(css = "input[value='Log In']")
    private WebElement loginButton;

    @FindBy(linkText = "Log Out")
    private WebElement logoutLink;

    @FindBy(linkText = "About Us")
    private WebElement aboutUsLink;

    @FindBy(css = "p.error")
    private WebElement loginError;

    @FindBy(linkText = "Contact Us")
    private WebElement contactUsLink;

    @FindBy(linkText = "Accounts Overview")
    private WebElement accountsOverviewLink;

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
        return loginUsernameField.isDisplayed();
    }

    public void login(String username, String password) {
        loginUsernameField.clear();
        loginUsernameField.sendKeys(username);
        loginPasswordField.clear();
        loginPasswordField.sendKeys(password);
        loginButton.click();
    }

    public String getLoginErrorText() {
        return loginError.getText();
    }

    public String getLoginErrorColor() {
        return loginError.getCssValue("color");
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
        return loginUsernameField.getAttribute("value");
    }

    public String getPasswordFieldValue() {
        return loginPasswordField.getAttribute("value");
    }
}

