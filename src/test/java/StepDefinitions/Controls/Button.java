package StepDefinitions.Controls;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class Button {

    private static Button instance;

    public static Button Instance() {
        if (instance == null) instance = new Button();
        return instance;
    }

    @And("Get Button Text {string}")
    public String GetButtonText(String key) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Button " + key + " not found");
        return element.getText();
    }

    @And("Click Button {string}")
    public void ClickButton(String key) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Button " + key + " not found");
        element.click();
        MyLogger.log.info("{} button clicked", key);
    }
}
