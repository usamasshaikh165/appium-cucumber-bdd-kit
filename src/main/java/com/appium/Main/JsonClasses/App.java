package com.appium.Main.JsonClasses;

import org.json.simple.JSONObject;

import java.io.FileReader;

/** Device / platform under test. Populated from TestNG suite parameters or App.json. */
public class App extends ReadFile {
    public static String key;             // "Android" or "iOS"
    public static String PlatformName;
    public static String PlatformVersion;
    public static String DeviceName;
    public static String Udid;

    private static App instance;

    public static App GetAppDetails(String key, String deviceName, String platformVersion, String platformName, String udid) {
        if (instance == null) {
            instance = new App(key, deviceName, platformVersion, platformName, udid);
        }
        return instance;
    }

    public App(String key, String deviceName, String platformVersion, String platformName, String udid) {
        App.key = key;
        App.DeviceName = deviceName;
        App.PlatformVersion = platformVersion;
        App.PlatformName = platformName;
        App.Udid = udid;
    }

    public static void ParseAppJson(String appFilePath) {
        FileReader reader = GetJsonFile(appFilePath);
        JSONObject obj = JsonHelper.ParseJson(reader);
        key = obj.get("Key").toString();
        PlatformName = obj.get("Platform_Name").toString();
        PlatformVersion = obj.get("Platform_Version").toString();
        DeviceName = obj.get("DeviceName").toString();
        Udid = obj.get("Udid").toString();
    }

    public static boolean isAndroid() {
        return "Android".equalsIgnoreCase(key);
    }

    public static boolean isIOS() {
        return "iOS".equalsIgnoreCase(key);
    }
}
