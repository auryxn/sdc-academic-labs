package com.stv.framework.core.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waiters {

    public static void waitForElementVisible(WebDriver driver, Duration duration, WebElement webElement) {
        new WebDriverWait(driver, duration).pollingEvery(Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOf(webElement));
    }
}
