package com.appium.Main.JsonClasses;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonHelper {

    public static JSONArray GetJsonArray(JSONObject obj, String key) {
        return (JSONArray) obj.get(key);
    }

    public static JSONObject ParseJson(FileReader reader) {
        try {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
