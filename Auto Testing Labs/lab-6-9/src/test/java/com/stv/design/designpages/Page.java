package com.stv.design.designpages;

import com.stv.framework.core.drivers.MyDriver;
import org.openqa.selenium.WebDriver;


public abstract class Page {

    protected static WebDriver getDriver() {
        return MyDriver.getDriver();
    }
}
