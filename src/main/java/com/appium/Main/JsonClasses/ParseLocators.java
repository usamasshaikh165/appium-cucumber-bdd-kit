package com.appium.Main.JsonClasses;

import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.AppiumBy;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;

import java.io.FileReader;

/**
 * Locator repository: every element the tests touch is a named key in
 * AndroidLocators.json / iOSLocators.json with a "locator" strategy and a "value".
 * Supported strategies: Id, XPath, Class, Name, AccessibilityId, UiAutomator (Android),
 * Predicate and ClassChain (iOS).
 */
public class ParseLocators extends ReadFile {

    private static JSONObject cache;
    private static String cachedFor;

    private static JSONObject repository() {
        String file = App.isIOS() ? GlobalVariables.iOSLocatorsFile : GlobalVariables.AndroidLocatorsFile;
        if (cache == null || !file.equals(cachedFor)) {
            FileReader reader = GetJsonFile(file);
            cache = JsonHelper.ParseJson(reader);
            cachedFor = file;
        }
        return cache;
    }

    /** Force a re-read, e.g. after editing the JSON during a debugging session. */
    public static void reload() {
        cache = null;
    }

    public static JSONObject GetLocatorObject(String key) {
        JSONObject obj = (JSONObject) repository().get(key);
        if (obj == null) {
            throw new IllegalArgumentException("Locator key '" + key + "' not found in " + cachedFor);
        }
        return obj;
    }

    public static By GetLocator(String key) {
        JSONObject obj = GetLocatorObject(key);
        String strategy = obj.get("locator").toString();
        String value = obj.get("value").toString();
        switch (strategy) {
            case "Id": return By.id(value);
            case "XPath": return By.xpath(value);
            case "Class": return By.className(value);
            case "Name": return By.name(value);
            case "AccessibilityId": return AppiumBy.accessibilityId(value);
            case "UiAutomator": return AppiumBy.androidUIAutomator(value);
            case "Predicate": return AppiumBy.iOSNsPredicateString(value);
            case "ClassChain": return AppiumBy.iOSClassChain(value);
            default:
                MyLogger.log.error("Locator strategy '{}' for key '{}' is not supported", strategy, key);
                throw new IllegalArgumentException("Unsupported locator strategy: " + strategy);
        }
    }
}
