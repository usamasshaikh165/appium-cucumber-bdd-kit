package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IList;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AndroidList implements IList {

    private static AndroidList instance;

    public static AndroidList Instance() {
        if (instance == null) instance = new AndroidList();
        return instance;
    }

    /** Collects every item's text while scrolling until the page stops changing. */
    @Override
    public List<String> GetListTexts(String listKey) {
        Set<String> texts = new LinkedHashSet<>();
        String previous = DriverFactory.driver.getPageSource();
        for (int i = 0; i < 10; i++) {
            for (WebElement e : DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey))) {
                String t = e.getText();
                if (t != null && !t.isBlank()) texts.add(t);
            }
            GeneralHelper.SwipeDown();
            String current = DriverFactory.driver.getPageSource();
            if (current.equals(previous)) break;
            previous = current;
        }
        return new ArrayList<>(texts);
    }

    @Override
    public void ClickListItemByText(String listKey, String text) {
        String previous = DriverFactory.driver.getPageSource();
        for (int i = 0; i < 10; i++) {
            for (WebElement e : DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey))) {
                if (text.equals(e.getText())) {
                    e.click();
                    MyLogger.log.info("Clicked list item '{}'", text);
                    return;
                }
            }
            GeneralHelper.SwipeDown();
            String current = DriverFactory.driver.getPageSource();
            if (current.equals(previous)) break;
            previous = current;
        }
        Assert.fail("List item '" + text + "' not found in " + listKey);
    }

    @Override
    public void ValidateListValues(String listKey, int index, DataTable table) {
        List<WebElement> items = ElementFinder.GetElementList(listKey, DriverFactory.driver);
        Assert.assertTrue(items.size() > index, "List " + listKey + " has no item at index " + index);
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            WebElement child = ElementFinder.FindElement(row.get("Key"), items.get(index));
            String actual = child == null ? null : child.getText();
            if (row.get("Value").equals(actual)) {
                MyLogger.log.info("{} = '{}' validated in {}", row.get("Key"), actual, listKey);
            } else {
                MyLogger.log.error("{} expected '{}' but was '{}' in {}", row.get("Key"), row.get("Value"), actual, listKey);
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " list value(s) did not match");
    }

    public List<String> getElementStringList(List<WebElement> elements) {
        List<String> out = new ArrayList<>();
        for (WebElement e : elements) {
            String t = e.getText();
            if (t != null && !"N/A".equals(t)) out.add(t);
        }
        return out;
    }
}
