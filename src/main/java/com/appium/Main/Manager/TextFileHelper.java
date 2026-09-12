package com.appium.Main.Manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileHelper {

    public static ArrayList<String> GetArrayListFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public static void WriteLinesToFile(List<String> lines, String filename) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, true))) {
            for (String line : lines) {
                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void WriteLineToFile(String line, String filename) {
        WriteLinesToFile(List.of(line), filename);
    }
}
