package com.stv.factory.factorypages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FactoryAccountOverviewPage extends FactoryPage {

    @FindBy(className = "title")
    private WebElement title;

    public String getTitleText() {
        return title.getText();
    }
}

