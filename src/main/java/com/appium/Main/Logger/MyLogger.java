package com.appium.Main.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyLogger {
    public static final Logger log = LogManager.getLogger(MyLogger.class);

    public static void logInfo(String info) {
        log.info(info);
    }
}
