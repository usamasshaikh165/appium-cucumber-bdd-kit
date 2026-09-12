package StepDefinitions.Controls;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.BottomSheetFactory;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Map;

import static com.appium.Main.Factory.DriverFactory.driver;

/** Application-agnostic steps: waits, presence checks, app lifecycle, toasts, bottom sheets. */
public class General {

    private static General instance;

    public static General Instance() {
        if (instance == null) instance = new General();
        return instance;
    }

    // ── Waits ────────────────────────────────────────────────────────────────

    @And("Wait for {int}")
    public void Wait(int timeInMilliseconds) {
        GeneralHelper.Wait(timeInMilliseconds);
    }

    @And("Wait for Visibilty till {int} {string}")
    public void WaitForVisibility(int timeInSeconds, String elementKey) {
        Assert.assertTrue(GeneralHelper.WaitForVisibility(timeInSeconds, elementKey),
                elementKey + " did not become visible within " + timeInSeconds + "s");
    }

    @And("Wait until {string} disappears within {int}")
    public void WaitUntilGone(String elementKey, int timeInSeconds) {
        Assert.assertTrue(GeneralHelper.WaitForInvisibility(timeInSeconds, elementKey),
                elementKey + " is still visible after " + timeInSeconds + "s");
    }

    @And("Wait Until Alert is present")
    public void WaitUntilAlertIsPresent() {
        GeneralHelper.WaitUntilAlertIsPresent();
    }

    // ── Presence / state ─────────────────────────────────────────────────────

    @Then("Check the Presence of {string}")
    public void CheckPresenceOfElement(String key) {
        GeneralHelper.CheckPresenceOfElement(key);
    }

    @Then("{string} should not be present")
    public void ShouldNotBePresent(String key) {
        Assert.assertFalse(GeneralHelper.IsElementPresent(key), key + " is present but should not be");
    }

    @Then("Is Control {string} enabled")
    public void IsControlEnabled(String key) {
        Assert.assertTrue(GeneralHelper.IsControlEnabled(key), key + " is disabled");
    }

    @Then("Is Toggle is Acive,Key {string}")
    public void IsToggleActive(String key) {
        Assert.assertTrue(GeneralHelper.IsToggleActive(key), key + " toggle is not active");
    }

    @Given("Validate value exist in specific element, Key {string} and value {string}")
    public void ValidateValueExistInElement(String key, String value) {
        WebElement element = ElementFinder.FindElement(key, driver);
        Assert.assertNotNull(element, key + " not found");
        String actual = element.getText();
        Assert.assertTrue(actual.contains(value), key + " = '" + actual + "' does not contain '" + value + "'");
    }

    // ── Toasts ───────────────────────────────────────────────────────────────

    @And("Validate Toast Message: {string}")
    public void ValidateToastMessage(String value) {
        ValidateToastMessage(value, "ToastMessage");
    }

    @Given("Validate Toast Message: Message {string} JsonObject {string}")
    public void ValidateToastMessage(String value, String toastKey) {
        WebElement message = ElementFinder.FindElement(toastKey, driver);
        Assert.assertNotNull(message, "Toast " + toastKey + " not found");
        Assert.assertEquals(message.getText(), value, "Toast text mismatch");
        MyLogger.log.info("Toast '{}' validated", value);
    }

    // ── Composite input ──────────────────────────────────────────────────────

    @Given("Add following entries in TextBox: {string} and Click Button: {string}")
    public void FillEntriesAndClick(String textBoxKey, String buttonKey, DataTable table) {
        for (Map<String, String> row : table.asMaps(String.class, String.class)) {
            Textbox.Instance().FillTextBox(textBoxKey, row.get("Value"));
            Button.Instance().ClickButton(buttonKey);
            MyLogger.log.info("Entry '{}' added", row.get("Value"));
        }
    }

    @And("Submit verification code {string} into {string}")
    public void SubmitVerificationCode(String code, String codeFieldKey) {
        DriverFactory.appium.SubmitVerificationCode(codeFieldKey, code);
    }

    // ── Bottom sheets ────────────────────────────────────────────────────────

    @Then("Validate Bottom Sheet {string} list {string} contains:")
    public void ValidateBottomSheetValues(String bottomSheetKey, String listKey, DataTable table) {
        BottomSheetFactory.appiumBottomSheet.selectBottomSheetValues(bottomSheetKey, listKey, table);
    }

    @And("Select Bottom Sheet value, bottomSheetType {string}, list {string}, selectedValue {string}")
    public void ChooseBottomSheetValue(String bottomSheetKey, String listKey, String selectedValue) {
        BottomSheetFactory.appiumBottomSheet.choosetBottomSheetValues(bottomSheetKey, listKey, selectedValue);
    }

    // ── App lifecycle / navigation ───────────────────────────────────────────

    @And("Close App")
    public void CloseApp() {
        GeneralHelper.closeApp();
    }

    @And("Launch App")
    public void LaunchApp() {
        GeneralHelper.launchApp();
    }

    @And("Press Back")
    public void PressBack() {
        DriverFactory.appium.PressBack();
    }

    @And("Log Message {string} {string}")
    public void LogMessage(String loggingType, String message) {
        GeneralHelper.LogMessage(loggingType, message);
    }
}
