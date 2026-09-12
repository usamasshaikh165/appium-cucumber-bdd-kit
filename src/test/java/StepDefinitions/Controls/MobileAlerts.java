package StepDefinitions.Controls;

import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.java.en.Then;
import org.openqa.selenium.Alert;
import org.testng.Assert;

public class MobileAlerts {

    private static MobileAlerts instance;

    public static MobileAlerts Instance() {
        if (instance == null) instance = new MobileAlerts();
        return instance;
    }

    private Alert alert() {
        GeneralHelper.WaitUntilAlertIsPresent();
        return DriverFactory.driver.switchTo().alert();
    }

    @Then("Accept Alert")
    public void AcceptAlert() {
        try {
            alert().accept();
        } catch (Exception ex) {
            Assert.fail("Unable to accept alert: " + ex.getMessage());
        }
    }

    @Then("Dismiss Alert")
    public void DismissAlert() {
        try {
            alert().dismiss();
        } catch (Exception ex) {
            Assert.fail("Unable to dismiss alert: " + ex.getMessage());
        }
    }

    @Then("Get Alert Text")
    public String GetAlertText() {
        try {
            String text = alert().getText();
            MyLogger.log.info("Alert text: {}", text);
            return text;
        } catch (Exception ex) {
            Assert.fail("Unable to read alert text: " + ex.getMessage());
            return "";
        }
    }

    @Then("Validate Alert Text {string}")
    public void ValidateAlertText(String text) {
        Assert.assertEquals(GetAlertText(), text, "Alert text mismatch");
    }
}
