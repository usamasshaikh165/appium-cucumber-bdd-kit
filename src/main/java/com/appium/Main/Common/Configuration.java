package com.appium.Main.Common;

import com.appium.Main.Factory.BottomSheetFactory;
import com.appium.Main.Factory.ComboBoxFactory;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Factory.ListFactory;
import com.appium.Main.JsonClasses.App;
import com.appium.Main.JsonClasses.GlobalVariables;
import org.apache.logging.log4j.ThreadContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Configuration {

    public static void InitializeGlobalVariables() {
        GlobalVariables.Instance().SetGlobalVariables();
        ListFactory.GetListControl(App.key);
        ComboBoxFactory.GetComboBoxControl(App.key);
        BottomSheetFactory.setBottomSheetInstance(App.key);
        DriverFactory.getDriver();
    }

    public static void SetExtentProperties() {
        System.setProperty("extent.reporter.spark.start", "true");
        System.setProperty("extent.reporter.spark.out",
                "Reports/" + App.key + "_" + App.DeviceName + "_" + App.PlatformVersion + "-Report.html");
    }

    public static void SetLog4jProperties() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        ThreadContext.put("ROUTINGKEY", App.key + "_" + App.DeviceName + "_" + App.PlatformVersion + "_" + date);
    }

    public static void InitializeAppDetails(String key, String deviceName, String platformVersion, String platformName, String udid) {
        App.GetAppDetails(key, deviceName, platformVersion, platformName, udid);
    }
}
