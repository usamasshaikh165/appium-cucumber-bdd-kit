package com.appium.Main.Manager;

import com.appium.Main.Logger.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties from the classpath once. Any key can be overridden
 * by an environment variable of the same name (e.g. ANDROID_APP_PACKAGE for
 * AndroidAppPackage) so CI never has to edit the file.
 */
public class PropertyManager {
    private static final Properties props = new Properties();

    public Properties getProps(String filename) {
        if (props.isEmpty()) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
                if (is == null) {
                    throw new IllegalStateException(filename + " not found on the classpath");
                }
                props.load(is);
                MyLogger.log.info("Loaded {}", filename);
            } catch (IOException e) {
                MyLogger.log.fatal("Failed to load {}", filename, e);
                throw new RuntimeException(e);
            }
        }
        return props;
    }

    /** Property value with an environment-variable override (UPPER_SNAKE_CASE of the key). */
    public static String get(String key, String fallback) {
        String env = System.getenv(toEnvName(key));
        if (env != null && !env.isBlank()) return env;
        String value = new PropertyManager().getProps("config.properties").getProperty(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    static String toEnvName(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }
}
