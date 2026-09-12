package StepDefinitions.Controls;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class Labels {

    private static Labels instance;

    public static Labels Instance() {
        if (instance == null) instance = new Labels();
        return instance;
    }

    @And("Get text of label {string}")
    public String GetText(String key) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Label " + key + " not found");
        String text = element.getText();
        MyLogger.log.info("Text of {} is '{}'", key, text);
        return text;
    }

    @Then("Validate text of label key {string} value {string}")
    public void ValidateValue(String key, String value) {
        String actual = GetText(key).replaceAll("\\r?\\n", " ").trim();
        String expected = value.replaceAll("\\r?\\n", " ").trim();
        Assert.assertEquals(actual, expected, "Label " + key + " text mismatch");
    }

    @Then("Validate label {string} contains {string}")
    public void ValidateContains(String key, String value) {
        String actual = GetText(key);
        Assert.assertTrue(actual.contains(value), "Label " + key + " = '" + actual + "' does not contain '" + value + "'");
    }
}
