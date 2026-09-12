package com.appium.Main.Common;

import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.JsonClasses.App;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Finds elements by locator key. If an element is not on screen the finder
 * scrolls down until the page stops changing, then scrolls back up, before
 * giving up. This makes long scrolling screens transparent to step authors.
 */
public class ElementFinder {

    private static final int MAX_SCROLLS = 8;

    public static WebElement FindElement(String key, AppiumDriver driver) {
        return find(key, null, driver);
    }

    public static WebElement FindElement(String key, WebElement parent) {
        return find(key, parent, DriverFactory.driver);
    }

    private static WebElement find(String key, WebElement parent, AppiumDriver driver) {
        WebElement element = tryFind(key, parent, driver, 5);
        if (element != null) return element;

        String previous = driver.getPageSource();
        for (int i = 0; i < MAX_SCROLLS; i++) {
            GeneralHelper.SwipeDown();
            element = tryFind(key, parent, driver, 1);
            if (element != null) return element;
            String current = driver.getPageSource();
            if (current.equals(previous)) break;   // reached the end
            previous = current;
        }
        for (int i = 0; i < MAX_SCROLLS; i++) {
            GeneralHelper.SwipeUP();
            element = tryFind(key, parent, driver, 1);
            if (element != null) return element;
            String current = driver.getPageSource();
            if (current.equals(previous)) break;   // reached the top
            previous = current;
        }
        MyLogger.log.error("Cannot find element '{}' even after scrolling the screen", key);
        return null;
    }

    private static WebElement tryFind(String key, WebElement parent, AppiumDriver driver, int waitSeconds) {
        try {
            if (parent == null) {
                GeneralHelper.WaitForVisibility(waitSeconds, key);
                WebElement element = driver.findElement(ParseLocators.GetLocator(key));
                MyLogger.log.info("{} element found", key);
                return element;
            }
            WebElement element = parent.findElement(ParseLocators.GetLocator(key));
            MyLogger.log.info("{} element found inside parent", key);
            return element;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<WebElement> GetElementList(String key, AppiumDriver driver) {
        try {
            GeneralHelper.WaitForVisibilityofElements(2, key);
            return driver.findElements(ParseLocators.GetLocator(key));
        } catch (Exception e) {
            MyLogger.log.error("Cannot find list '{}'", key, e);
            return List.of();
        }
    }

    public static List<WebElement> GetElementList(String key, WebElement parent) {
        try {
            GeneralHelper.WaitForVisibilityofElements(2, key);
            return parent.findElements(ParseLocators.GetLocator(key));
        } catch (Exception e) {
            MyLogger.log.error("Cannot find list '{}' inside parent", key, e);
            return List.of();
        }
    }
}
