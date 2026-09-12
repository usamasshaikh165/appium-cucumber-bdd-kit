package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IbottomSheet;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class AndroidIBottomSheet implements IbottomSheet {

    private static AndroidIBottomSheet instance;

    public static AndroidIBottomSheet Instance() {
        if (instance == null) instance = new AndroidIBottomSheet();
        return instance;
    }

    @Override
    public void selectBottomSheetValues(String bottomSheetKey, String listKey, DataTable table) {
        ElementFinder.FindElement(bottomSheetKey, DriverFactory.driver).click();
        List<WebElement> items = DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey));
        List<String> values = AndroidList.Instance().getElementStringList(items);
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            if (values.contains(row.get("Value"))) {
                MyLogger.log.info("'{}' exists in bottom sheet", row.get("Value"));
            } else {
                MyLogger.log.error("'{}' missing from bottom sheet. Values: {}", row.get("Value"), values);
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " expected value(s) missing from bottom sheet");
    }

    @Override
    public void choosetBottomSheetValues(String bottomSheetKey, String listKey, String selectedValue) {
        ElementFinder.FindElement(bottomSheetKey, DriverFactory.driver).click();
        GeneralHelper.Wait(800);
        List<WebElement> items = DriverFactory.driver.findElements(ParseLocators.GetLocator(listKey));
        List<String> values = AndroidList.Instance().getElementStringList(items);
        int index = values.indexOf(selectedValue);
        Assert.assertTrue(index >= 0, "'" + selectedValue + "' not present in bottom sheet " + listKey);
        items.get(index).click();
        MyLogger.log.info("'{}' selected in bottom sheet", selectedValue);
    }
}
