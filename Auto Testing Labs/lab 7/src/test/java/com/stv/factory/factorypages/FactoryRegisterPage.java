package com.stv.factory.factorypages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FactoryRegisterPage extends FactoryPage {

    @FindBy(className = "title")
    private WebElement title;

    public String getTitleText() {
        return title.getText();
    }
    
    public boolean isTitleDisplayed() {
        return title.isDisplayed();
    }
}
