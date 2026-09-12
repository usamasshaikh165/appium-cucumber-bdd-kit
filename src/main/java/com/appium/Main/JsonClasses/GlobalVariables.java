package com.appium.Main.JsonClasses;

import org.json.simple.JSONObject;

import java.io.FileReader;

/**
 * Framework-level paths and endpoints, read from GlobalVariables.json.
 * Environment variables APPIUM_SERVER_URL_ANDROID / APPIUM_SERVER_URL_IOS override
 * the server URLs so CI can point at a grid or a device farm without editing files.
 */
public class GlobalVariables extends ReadFile {
    public static final String GLOBALS_FILE = "src/test/resources/JsonFiles/GlobalVariables.json";

    public static String AndroidLocatorsFile;
    public static String iOSLocatorsFile;
    public static String ComboBoxValueFile;
    public static String AppJsonFile;
    public static String iOSServerURL;
    public static String AndroidServerURL;
    public static String Success;
    public static String Fail;

    private static GlobalVariables instance;

    public static GlobalVariables Instance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public void SetGlobalVariables() {
        FileReader reader = GetJsonFile(GLOBALS_FILE);
        JSONObject obj = JsonHelper.ParseJson(reader);
        AndroidLocatorsFile = obj.get("AndroidLocatorsFile").toString();
        iOSLocatorsFile = obj.get("iOSLocatorsFile").toString();
        ComboBoxValueFile = obj.get("ComboBoxValueFile").toString();
        AppJsonFile = obj.get("AppJsonFile").toString();
        AndroidServerURL = env("APPIUM_SERVER_URL_ANDROID", obj.get("AndroidServerURL").toString());
        iOSServerURL = env("APPIUM_SERVER_URL_IOS", obj.get("iOSServerURL").toString());
        Success = obj.get("Success").toString();
        Fail = obj.get("Fail").toString();
    }

    private static String env(String name, String fallback) {
        String v = System.getenv(name);
        return v == null || v.isBlank() ? fallback : v;
    }
}
