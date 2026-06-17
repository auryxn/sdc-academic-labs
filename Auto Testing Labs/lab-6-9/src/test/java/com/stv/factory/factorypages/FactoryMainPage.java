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
}
