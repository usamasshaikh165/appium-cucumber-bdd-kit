package com.appium.Main.DriverClasses;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Interface.IDriverGeneralMethods;
import com.appium.Main.JsonClasses.ParseLocators;
import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;

public class Android extends AndroidDriver implements IDriverGeneralMethods {

    public static Android androidDriver;

    public Android(URL remoteAddress, Capabilities capabilities) {
        super(remoteAddress, capabilities);
    }

    @Override
    public void SubmitVerificationCode(String codeFieldKey, String code) {
        List<WebElement> boxes = findElements(ParseLocators.GetLocator(codeFieldKey));
        if (boxes.size() > 1) {
            String[] digits = code.split("");
            for (int i = 0; i < Math.min(boxes.size(), digits.length); i++) {
                boxes.get(i).sendKeys(digits[i]);
            }
        } else {
            WebElement field = ElementFinder.FindElement(codeFieldKey, this);
            field.sendKeys(code);
        }
        MyLogger.log.info("Verification code entered into {}", codeFieldKey);
    }

    @Override
    public void PressBack() {
        pressKey(new KeyEvent(AndroidKey.BACK));
    }
}
