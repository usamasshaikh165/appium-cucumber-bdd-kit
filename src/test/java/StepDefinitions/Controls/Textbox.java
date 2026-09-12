package StepDefinitions.Controls;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.JsonClasses.App;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Map;

public class Textbox {

    private static Textbox instance;

    public static Textbox Instance() {
        if (instance == null) instance = new Textbox();
        return instance;
    }

    @Given("Fill Values in Multiple Text Boxes as following table")
    public void FillMultipleTextBoxes(DataTable table) {
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            FillTextBox(row.get("Key"), row.get("Value"));
        }
    }

    @Given("Fill TextBox {string} {string}")
    public void FillTextBox(String key, String value) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Textbox " + key + " not found, cannot enter '" + value + "'");
        element.click();
        element.sendKeys(value);
        if (App.isIOS()) {
            element.sendKeys(Keys.ENTER);
        } else {
            Keyboard.Instance().HideKeyboard();
        }
        MyLogger.log.info("'{}' entered into {}", value, key);
    }

    @Given("Fill Multiple Textboxes {string} {string}")
    public void FillMultipleTextBox(String keys, String values) {
        String[] keyList = keys.split(",");
        String[] valueList = values.split(",");
        Assert.assertEquals(keyList.length, valueList.length, "Give one value per key");
        for (int i = 0; i < keyList.length; i++) {
            FillTextBox(keyList[i].trim(), valueList[i].trim());
        }
    }

    @And("Get text of Textbox {string}")
    public String GetTextOfTextbox(String key) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Textbox " + key + " not found");
        String text = element.getText();
        MyLogger.log.info("Text of {} is '{}'", key, text);
        return text;
    }

    @Then("Validate Textbox Value {string} {string}")
    public void ValidateTextboxValue(String key, String value) {
        Assert.assertEquals(GetTextOfTextbox(key), value, "Textbox " + key + " value mismatch");
    }

    @Given("Clear textbox {string}")
    public void ClearTextBox(String key) {
        WebElement element = ElementFinder.FindElement(key, DriverFactory.driver);
        Assert.assertNotNull(element, "Textbox " + key + " not found");
        element.clear();
    }
}
