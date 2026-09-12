package com.appium.Main.Factory;

import com.appium.Main.DriverClasses.AndroidList;
import com.appium.Main.DriverClasses.iOSList;
import com.appium.Main.Interface.IList;

public class ListFactory {
    public static IList AppiumList = null;

    public static IList GetListControl(String key) {
        if (AppiumList == null) {
            AppiumList = key.equalsIgnoreCase("iOS") ? new iOSList() : new AndroidList();
        }
        return AppiumList;
    }
}
