package com.appium.Main.Factory;

import com.appium.Main.DriverClasses.Android;
import com.appium.Main.DriverClasses.iOS;
import com.appium.Main.Interface.IDriverGeneralMethods;
import com.appium.Main.JsonClasses.App;
import com.appium.Main.JsonClasses.GlobalVariables;
import com.appium.Main.Logger.MyLogger;
import com.appium.Main.Setup.Capabilities;
import io.appium.java_client.AppiumDriver;

import java.net.URL;

public class DriverFactory {

    public static AppiumDriver driver = null;
    public static IDriverGeneralMethods appium = null;

    public static AppiumDriver getDriver() {
        try {
            if (App.isIOS()) {
                iOS.iosDriver = new iOS(new URL(GlobalVariables.iOSServerURL), Capabilities.forIOS());
                driver = iOS.iosDriver;
                appium = iOS.iosDriver;
                MyLogger.log.info("iOS driver started against {}", GlobalVariables.iOSServerURL);
            } else if (App.isAndroid()) {
                Android.androidDriver = new Android(new URL(GlobalVariables.AndroidServerURL), Capabilities.forAndroid());
                driver = Android.androidDriver;
                appium = Android.androidDriver;
                MyLogger.log.info("Android driver started against {}", GlobalVariables.AndroidServerURL);
            } else {
                throw new IllegalStateException("App.key must be 'Android' or 'iOS', was: " + App.key);
            }
        } catch (Exception ex) {
            MyLogger.log.error("Unable to start the driver", ex);
            throw new RuntimeException(ex);
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
                appium = null;
                Android.androidDriver = null;
                iOS.iosDriver = null;
            }
        }
    }
}
