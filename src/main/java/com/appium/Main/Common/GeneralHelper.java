package com.appium.Main.Common;

import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.JsonClasses.App;
import com.appium.Main.JsonClasses.GlobalVariables;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import com.appium.Main.Manager.PropertyManager;
import io.appium.java_client.InteractsWithApps;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static com.appium.Main.Factory.DriverFactory.driver;

/** Waits, gestures and small checks shared by every control. All gestures use W3C pointer actions. */
public class GeneralHelper {

    // ── Gestures ────────────────────────────────────────────────────────────

    public static void swipe(int startX, int startY, int endX, int endY, int millis) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(100)))
                .addAction(finger.createPointerMove(Duration.ofMillis(millis), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipe));
    }

    /** Scrolls the content up (finger moves from lower to upper part of the screen). */
    public static void SwipeDown() {
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        swipe(x, (int) (size.height * 0.70), x, (int) (size.height * 0.30), 600);
    }

    /** Scrolls the content down (finger moves from upper to lower part of the screen). */
    public static void SwipeUP() {
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        swipe(x, (int) (size.height * 0.30), x, (int) (size.height * 0.70), 600);
    }

    public static void SwipeInDirection(String direction, long durationMillis, int count) {
        Dimension size = driver.manage().window().getSize();
        int startX, startY, endX, endY;
        switch (direction.toUpperCase()) {
            case "RIGHT" -> { startY = endY = size.height / 2; startX = (int) (size.width * 0.90); endX = (int) (size.width * 0.05); }
            case "LEFT" -> { startY = endY = size.height / 2; startX = (int) (size.width * 0.05); endX = (int) (size.width * 0.90); }
            case "UP" -> { startX = endX = size.width / 2; startY = (int) (size.height * 0.70); endY = (int) (size.height * 0.30); }
            case "DOWN" -> { startX = endX = size.width / 2; startY = (int) (size.height * 0.30); endY = (int) (size.height * 0.70); }
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        for (int i = 0; i < Math.max(1, count); i++) {
            swipe(startX, startY, endX, endY, (int) durationMillis);
        }
    }

    public static void TapOnElement(WebElement element) {
        if (element == null) {
            MyLogger.log.error("Element is null, unable to tap");
            Assert.fail("Element is null, unable to tap");
        }
        org.openqa.selenium.Point c = element.getLocation();
        Dimension d = element.getSize();
        int x = c.x + d.width / 2;
        int y = c.y + d.height / 2;
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(80)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(tap));
    }

    // ── Waits ───────────────────────────────────────────────────────────────

    public static void Wait(int timeInMilliseconds) {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static boolean WaitForVisibility(int timeInSeconds, String elementKey) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(ParseLocators.GetLocator(elementKey)));
            return true;
        } catch (Exception ex) {
            MyLogger.log.debug("'{}' not visible within {}s", elementKey, timeInSeconds);
            return false;
        }
    }

    public static boolean WaitForVisibilityofElements(int timeInSeconds, String elementKey) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ParseLocators.GetLocator(elementKey)));
            return true;
        } catch (Exception ex) {
            MyLogger.log.debug("'{}' list not visible within {}s", elementKey, timeInSeconds);
            return false;
        }
    }

    public static boolean WaitForInvisibility(int timeInSeconds, String elementKey) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(ParseLocators.GetLocator(elementKey)));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void WaitUntilAlertIsPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
        } catch (Exception ex) {
            MyLogger.log.debug("No alert appeared within 5s");
        }
    }

    // ── Checks ──────────────────────────────────────────────────────────────

    public static void LogMessage(String loggingType, String message) {
        switch (loggingType) {
            case "Debug" -> MyLogger.log.debug(message);
            case "Info" -> MyLogger.log.info(message);
            case "Error" -> MyLogger.log.error(message);
            case "Warn" -> MyLogger.log.warn(message);
            default -> MyLogger.log.error("Invalid log type requested: {}", loggingType);
        }
    }

    public static boolean IsElementPresent(String key) {
        List<WebElement> found = driver.findElements(ParseLocators.GetLocator(key));
        return !found.isEmpty() && found.get(0).isDisplayed();
    }

    public static void CheckPresenceOfElement(String key) {
        WebElement element = ElementFinder.FindElement(key, driver);
        boolean present = element != null && element.isDisplayed();
        MyLogger.log.info("{} control is {}", key, present ? "present" : "not present");
        Assert.assertEquals(present ? GlobalVariables.Success : GlobalVariables.Fail, GlobalVariables.Success,
                key + " is not present on the screen");
    }

    public static boolean IsToggleActive(String key) {
        WebElement element = ElementFinder.FindElement(key, driver);
        String checked = element.getAttribute(App.isIOS() ? "value" : "checked");
        boolean active = "true".equalsIgnoreCase(checked) || "1".equals(checked);
        MyLogger.log.info("Toggle {} is {}", key, active ? "active" : "not active");
        return active;
    }

    public static boolean IsControlEnabled(String key) {
        WebElement element = ElementFinder.FindElement(key, driver);
        boolean enabled = element.isEnabled();
        MyLogger.log.info("Control {} is {}", key, enabled ? "enabled" : "disabled");
        return enabled;
    }

    public static String[] ConvertStringToStringArray(String value, String splitBy) {
        return value.split(splitBy);
    }

    // ── App lifecycle ───────────────────────────────────────────────────────

    public static String appId() {
        return App.isIOS() ? PropertyManager.get("iOSBundleId", "") : PropertyManager.get("AndroidAppPackage", "");
    }

    public static void closeApp() {
        ((InteractsWithApps) driver).terminateApp(appId());
    }

    public static void launchApp() {
        ((InteractsWithApps) driver).activateApp(appId());
    }
}
