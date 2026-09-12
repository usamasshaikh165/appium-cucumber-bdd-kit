package com.appium.Main.Factory;

import com.appium.Main.DriverClasses.AndroidIBottomSheet;
import com.appium.Main.DriverClasses.iOSBottomSheet;
import com.appium.Main.Interface.IbottomSheet;

public class BottomSheetFactory {
    public static IbottomSheet appiumBottomSheet = null;

    public static IbottomSheet setBottomSheetInstance(String key) {
        if (appiumBottomSheet == null) {
            appiumBottomSheet = key.equalsIgnoreCase("iOS") ? new iOSBottomSheet() : new AndroidIBottomSheet();
        }
        return appiumBottomSheet;
    }
}
