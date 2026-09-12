package com.appium.Main.Factory;

import com.appium.Main.DriverClasses.AndroidComboBox;
import com.appium.Main.DriverClasses.iOSComboBox;
import com.appium.Main.Interface.IComboBox;

public class ComboBoxFactory {
    public static IComboBox AppiumCombBox = null;

    public static IComboBox GetComboBoxControl(String key) {
        if (AppiumCombBox == null) {
            AppiumCombBox = key.equalsIgnoreCase("iOS") ? new iOSComboBox() : new AndroidComboBox();
        }
        return AppiumCombBox;
    }
}
