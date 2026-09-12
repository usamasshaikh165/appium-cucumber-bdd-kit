package com.appium.Main.Setup;

import com.appium.Main.JsonClasses.App;
import com.appium.Main.Manager.PropertyManager;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Builds W3C capabilities from config.properties (plus environment overrides)
 * and the device details held in App. Either an installable app path or an
 * already-installed package/bundle id can be used.
 */
public class Capabilities {

    public static UiAutomator2Options forAndroid() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(App.PlatformName)
                .setPlatformVersion(App.PlatformVersion)
                .setDeviceName(App.DeviceName)
                .setAutomationName(PropertyManager.get("AndroidAutomationName", "UiAutomator2"))
                .setNoReset(Boolean.parseBoolean(PropertyManager.get("NoReset", "true")))
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setAdbExecTimeout(Duration.ofSeconds(60));

        if (App.Udid != null && !App.Udid.isBlank()) {
            options.setUdid(App.Udid);
        }

        String app = PropertyManager.get("AndroidApp", "");
        if (!app.isBlank()) {
            options.setApp(resolve(app));
        } else {
            options.setAppPackage(PropertyManager.get("AndroidAppPackage", ""));
            options.setAppActivity(PropertyManager.get("AndroidAppActivity", ""));
        }
        return options;
    }

    public static XCUITestOptions forIOS() {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName(App.PlatformName)
                .setPlatformVersion(App.PlatformVersion)
                .setDeviceName(App.DeviceName)
                .setAutomationName(PropertyManager.get("iOSAutomationName", "XCUITest"))
                .setNoReset(Boolean.parseBoolean(PropertyManager.get("NoReset", "true")))
                .setNewCommandTimeout(Duration.ofSeconds(120));

        if (App.Udid != null && !App.Udid.isBlank()) {
            options.setUdid(App.Udid);
        }

        String app = PropertyManager.get("iOSApp", "");
        if (!app.isBlank()) {
            options.setApp(resolve(app));
        } else {
            options.setBundleId(PropertyManager.get("iOSBundleId", ""));
        }
        return options;
    }

    private static String resolve(String path) {
        Path p = Paths.get(path);
        return p.isAbsolute() ? p.toString() : Paths.get(System.getProperty("user.dir")).resolve(p).normalize().toString();
    }
}
