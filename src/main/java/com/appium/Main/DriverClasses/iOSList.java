package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IList;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** iOS lists expose all rows in the accessibility tree, so no scrolling is needed to read them. */
public class iOSList implements IList {

    private static iOSList instance;

    public static iOSList Instance() {
        if (instance == null) instance = new iOSList();
        return instance;
    }

    @Override
    public List<String> GetListTexts(String listKey) {
        List<String> texts = new ArrayList<>();
        for (WebElement e : DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey))) {
            String t = e.getText();
            if (t != null && !t.isBlank()) texts.add(t);
        }
        return texts;
    }

    @Override
    public void ClickListItemByText(String listKey, String text) {
        for (WebElement e : DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey))) {
            if (text.equals(e.getText())) {
                e.click();
                MyLogger.log.info("Clicked list item '{}'", text);
                return;
            }
        }
        Assert.fail("List item '" + text + "' not found in " + listKey);
    }

    @Override
    public void ValidateListValues(String listKey, int index, DataTable table) {
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            List<WebElement> column = ElementFinder.GetElementList(row.get("Key"), DriverFactory.driver);
            String actual = column.size() > index ? column.get(index).getText() : null;
            if (row.get("Value").equals(actual)) {
                MyLogger.log.info("{} = '{}' validated in {}", row.get("Key"), actual, listKey);
            } else {
                MyLogger.log.error("{} expected '{}' but was '{}' in {}", row.get("Key"), row.get("Value"), actual, listKey);
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " list value(s) did not match");
    }
}
