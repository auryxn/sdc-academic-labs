package com.stv.factory.factorypages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class FactoryContactUsPage extends FactoryPage {

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "phone")
    private WebElement phoneField;

    @FindBy(id = "message")
    private WebElement messageField;

    @FindBy(css = "input[type='submit'][value='Send to Customer Care']")
    private WebElement submitButton;

    @FindBy(css = ".error")
    private List<WebElement> errorMessages;

    @FindBy(xpath = "//*[@id='rightPanel']/p[1]")
    private WebElement successMessage;

    public void fillName(String name) {
        nameField.sendKeys(name);
    }

    public void fillEmail(String email) {
        emailField.sendKeys(email);
    }

    public void fillPhone(String phone) {
        phoneField.sendKeys(phone);
    }

    public void fillMessage(String message) {
        messageField.sendKeys(message);
    }

    public void clickSubmit() {
        submitButton.click();
    }

    public List<String> getErrorMessages() {
        return errorMessages.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getSuccessMessageText() {
        return successMessage.getText();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

