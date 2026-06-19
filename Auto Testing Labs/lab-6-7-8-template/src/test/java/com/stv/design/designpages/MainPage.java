package com.stv.design.designpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/* Created by tatti on 5/25/2024*/
public class MainPage extends Page {
    private final By PARABANK_LOGO = By.cssSelector("img[alt='ParaBank']");
    private final By ADMIN_LOGO = By.cssSelector("img.admin");

    public boolean isMainLogoDisplayed() {
        WebElement registerLink = getDriver().findElement(PARABANK_LOGO);
        return registerLink.isDisplayed();
    }

    public MainPage clickOnAdminLogo() {
        WebElement trustButton = getDriver().findElement(ADMIN_LOGO);
        trustButton.click();
        return this;
    }
}
