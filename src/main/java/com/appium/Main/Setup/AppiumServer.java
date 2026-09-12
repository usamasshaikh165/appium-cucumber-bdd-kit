package com.appium.Main.Setup;

import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;

/**
 * Optional: start Appium from the tests instead of running `appium` yourself.
 * Set APPIUM_JS (path to appium's main.js) and, if node is not on PATH, NODE_PATH.
 * Skipped entirely when START_APPIUM is not "true".
 */
public class AppiumServer {
    public static AppiumDriverLocalService service;

    public static void startServer() {
        if (!"true".equalsIgnoreCase(System.getenv("START_APPIUM"))) {
            MyLogger.log.info("START_APPIUM not set; expecting an Appium server to be running already");
            return;
        }
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.BASEPATH, "/")
                .withArgument(GeneralServerFlag.RELAXED_SECURITY);

        String node = System.getenv("NODE_PATH");
        if (node != null && !node.isBlank()) builder.usingDriverExecutable(new File(node));
        String appiumJs = System.getenv("APPIUM_JS");
        if (appiumJs != null && !appiumJs.isBlank()) builder.withAppiumJS(new File(appiumJs));

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        MyLogger.log.info("Appium started at {}", service.getUrl());
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            MyLogger.log.info("Appium stopped");
        }
    }
}
