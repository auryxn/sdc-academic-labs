package com.stv.factory.factorytests;

import com.stv.design.designtests.BasicTest;
import com.stv.factory.factorypages.FactoryMainPage;
import com.stv.factory.factorypages.FactoryRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FactoryMainTest extends BasicTest {

    @Test(description = "Verify ParaBank logo is displayed on Main Page")
    public void testLogoVisibility() {
        FactoryMainPage mainPage = new FactoryMainPage();
        Assert.assertTrue(mainPage.isLogoDisplayed(), "Logo should be visible on the main page");
    }

    @Test(description = "Verify navigation to Register Page and its title")
    public void testNavigationToRegistration() {
        FactoryMainPage mainPage = new FactoryMainPage();
        FactoryRegisterPage registerPage = mainPage.clickRegisterLink();
        Assert.assertTrue(registerPage.isTitleDisplayed(), "Register page title should be displayed");
        Assert.assertEquals(registerPage.getTitleText(), "Signing up is easy!", "Register page title text is incorrect");
    }
}
