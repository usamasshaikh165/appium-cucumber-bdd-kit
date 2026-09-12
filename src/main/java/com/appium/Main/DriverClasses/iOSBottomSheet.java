package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IbottomSheet;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class iOSBottomSheet implements IbottomSheet {

    private static iOSBottomSheet instance;

    public static iOSBottomSheet Instance() {
        if (instance == null) instance = new iOSBottomSheet();
        return instance;
    }

    @Override
    public void selectBottomSheetValues(String bottomSheetKey, String listKey, DataTable table) {
        ElementFinder.FindElement(bottomSheetKey, DriverFactory.driver).click();
        List<WebElement> items = DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey));
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            boolean found = items.stream().anyMatch(i -> row.get("Value").equals(i.getText()));
            if (!found) {
                MyLogger.log.error("'{}' missing from bottom sheet", row.get("Value"));
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " expected value(s) missing from bottom sheet");
    }

    @Override
    public void choosetBottomSheetValues(String bottomSheetKey, String listKey, String selectedValue) {
        ElementFinder.FindElement(bottomSheetKey, DriverFactory.driver).click();
        for (WebElement item : DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey))) {
            if (selectedValue.equals(item.getText())) {
                item.click();
                MyLogger.log.info("'{}' selected in bottom sheet", selectedValue);
                return;
            }
        }
        Assert.fail("'" + selectedValue + "' not present in bottom sheet " + listKey);
    }
}
