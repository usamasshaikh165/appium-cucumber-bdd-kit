package StepDefinitions.Controls;

import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.appium.java_client.HidesKeyboard;
import io.cucumber.java.en.Then;

public class Keyboard {

    private static Keyboard instance;

    public static Keyboard Instance() {
        if (instance == null) instance = new Keyboard();
        return instance;
    }

    @Then("Hide Keyboard")
    public void HideKeyboard() {
        try {
            ((HidesKeyboard) DriverFactory.driver).hideKeyboard();
        } catch (Exception e) {
            MyLogger.log.debug("Keyboard was not shown or could not be hidden: {}", e.getMessage());
        }
    }
}
