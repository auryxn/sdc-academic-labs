package com.stv.framework.core.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MyDriver {
    private static WebDriver driver;
    private MyDriver(){

    }

    public static WebDriver getDriver() {

        if (driver == null) {
            setChromeDriver();
//            setFireFox();
        }
        return driver;
    }

    private static void setChromeDriver() {
        driver = new ChromeDriver();
    }

    /**
     * The method can be used to run tests in Fire fox
     */
    private static void setFireFox() {
//        String exePath =  "C:\\drivers\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver", exePath);
        driver = new FirefoxDriver();
    }
}
