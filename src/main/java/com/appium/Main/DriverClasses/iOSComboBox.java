package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IComboBox;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class iOSComboBox implements IComboBox {

    private static iOSComboBox instance;

    public static iOSComboBox Instance() {
        if (instance == null) instance = new iOSComboBox();
        return instance;
    }

    @Override
    public void SelectComboValue(String comboBoxKey, String value) {
        WebElement comboBox = ElementFinder.FindElement(comboBoxKey, DriverFactory.driver);
        Assert.assertNotNull(comboBox, "Combo box " + comboBoxKey + " not found");
        comboBox.click();
        for (WebElement option : ElementFinder.GetElementList(comboBoxKey, DriverFactory.driver)) {
            if (value.equals(option.getText())) {
                option.click();
                MyLogger.log.info("Selected '{}' in {}", value, comboBoxKey);
                return;
            }
        }
        Assert.fail("Value '" + value + "' is not present in " + comboBoxKey);
    }

    @Override
    public void ValidateComboBoxValues(String comboBoxKey, DataTable table) {
        List<WebElement> options = ElementFinder.GetElementList(comboBoxKey, DriverFactory.driver);
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            boolean found = options.stream().anyMatch(o -> row.get("Value").equals(o.getText()));
            if (!found) {
                MyLogger.log.error("'{}' missing from {}", row.get("Value"), comboBoxKey);
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " expected option(s) missing from " + comboBoxKey);
    }
}
