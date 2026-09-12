package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Interface.IComboBox;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * Android dropdowns: tap the combo box, then read the option list under the
 * "ComboBoxValues" locator key, scrolling the list until the value appears.
 */
public class AndroidComboBox implements IComboBox {

    public static final String OPTIONS_KEY = "ComboBoxValues";

    private static AndroidComboBox instance;

    public static AndroidComboBox Instance() {
        if (instance == null) instance = new AndroidComboBox();
        return instance;
    }

    @Override
    public void SelectComboValue(String comboBoxKey, String value) {
        WebElement comboBox = ElementFinder.FindElement(comboBoxKey, DriverFactory.driver);
        Assert.assertNotNull(comboBox, "Combo box " + comboBoxKey + " not found");
        comboBox.click();

        String previous = DriverFactory.driver.getPageSource();
        for (int i = 0; i < 10; i++) {
            GeneralHelper.WaitForVisibilityofElements(3, OPTIONS_KEY);
            for (WebElement option : DriverFactory.driver.findElements(ParseLocators.GetLocator(OPTIONS_KEY))) {
                if (value.equals(option.getText())) {
                    option.click();
                    MyLogger.log.info("Selected '{}' in {}", value, comboBoxKey);
                    return;
                }
            }
            GeneralHelper.SwipeDown();
            String current = DriverFactory.driver.getPageSource();
            if (current.equals(previous)) break;
            previous = current;
        }
        Assert.fail("Value '" + value + "' is not present in dropdown " + comboBoxKey);
    }

    @Override
    public void ValidateComboBoxValues(String comboBoxKey, DataTable table) {
        List<String> options = AndroidList.Instance().GetListTexts(OPTIONS_KEY);
        int failures = 0;
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            if (options.contains(row.get("Value"))) {
                MyLogger.log.info("'{}' exists in {}", row.get("Value"), comboBoxKey);
            } else {
                MyLogger.log.error("'{}' missing from {}. Options: {}", row.get("Value"), comboBoxKey, options);
                failures++;
            }
        }
        Assert.assertEquals(failures, 0, failures + " expected option(s) missing from " + comboBoxKey);
    }
}
