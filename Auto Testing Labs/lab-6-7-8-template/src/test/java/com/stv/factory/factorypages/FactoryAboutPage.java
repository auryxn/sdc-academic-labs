package com.stv.factory.factorypages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FactoryAboutPage extends FactoryPage {

    @FindBy(className = "title")
    private WebElement title;

    @FindBy(linkText = "www.parasoft.com")
    private WebElement parasoftLink;

    public String getTitleText() {
        return title.getText();
    }

    public void clickParasoftLink() {
        parasoftLink.click();
    }
}

