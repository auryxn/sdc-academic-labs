package com.stv.factory.factorytests;

import com.stv.design.designtests.BasicTest;
import com.stv.factory.factorypages.FactoryContactUsPage;
import com.stv.factory.factorypages.FactoryMainPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FactoryContactUsTest extends BasicTest {

    @Test(description = "Verify PAR-3: Contact Us form should validate required fields and not allow empty submission")
    public void testContactUsEmptyFieldsValidation() {
        FactoryMainPage mainPage = new FactoryMainPage();
        FactoryContactUsPage contactUsPage = mainPage.clickContactUsLink();

        // Step 1: Leave all fields empty and click submit
        contactUsPage.clickSubmit();

        // Step 2: Verify error messages are present for missing fields
        Assert.assertTrue(contactUsPage.getErrorMessages().size() > 0, 
                "Validation error messages should be displayed for empty required fields");

        // The bug PAR-3 stated empty submissions succeed. 
        // If error messages are shown, the bug might be fixed, but the test still covers the *required* behavior.
        Assert.assertFalse(contactUsPage.isSuccessMessageDisplayed() && contactUsPage.getSuccessMessageText().contains("Thank you"), 
                "BUG PAR-3 DETECTED (or validation failed): Contact Us form allowed submission with empty fields!");
    }

    @Test(description = "Verify Contact Us form can be submitted with valid data")
    public void testContactUsValidSubmission() {
        FactoryMainPage mainPage = new FactoryMainPage();
        FactoryContactUsPage contactUsPage = mainPage.clickContactUsLink();

        contactUsPage.fillName("Test User");
        contactUsPage.fillEmail("test@example.com");
        contactUsPage.fillPhone("123456789");
        contactUsPage.fillMessage("This is a test message.");
        contactUsPage.clickSubmit();

        Assert.assertTrue(contactUsPage.isSuccessMessageDisplayed(), "Success message should be displayed for valid submission");
        Assert.assertTrue(contactUsPage.getSuccessMessageText().contains("Thank you Test User"), "Success message should contain the user name");
    }
}

