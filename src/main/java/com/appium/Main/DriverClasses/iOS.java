package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Interface.IDriverGeneralMethods;
import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import java.net.URL;

public class iOS extends IOSDriver implements IDriverGeneralMethods {

    public static iOS iosDriver;

    public iOS(URL remoteAddress, Capabilities capabilities) {
        super(remoteAddress, capabilities);
    }

    @Override
    public void SubmitVerificationCode(String codeFieldKey, String code) {
        WebElement field = ElementFinder.FindElement(codeFieldKey, this);
        field.sendKeys(code);
        MyLogger.log.info("Verification code entered into {}", codeFieldKey);
    }

    @Override
    public void PressBack() {
        // iOS has no hardware back; the conventional equivalent is the nav-bar back button.
        navigate().back();
    }
}
