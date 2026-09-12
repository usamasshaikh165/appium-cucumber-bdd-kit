package com.appium.Main.JsonClasses;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadFile {
    public static FileReader GetJsonFile(String fileName) {
        try {
            return new FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON file not found: " + fileName, e);
        }
    }
}
