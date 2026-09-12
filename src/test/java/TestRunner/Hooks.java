package TestRunner;

import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Logger.MyLogger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    /** Every scenario starts from the app's entry screen. */
    @Before
    public void relaunchApp() {
        try {
            GeneralHelper.closeApp();
        } catch (Exception ignored) {
            // app was not running
        }
        GeneralHelper.launchApp();
    }

    /** Attach a screenshot to the report whenever a scenario fails. */
    @After
    public void screenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.driver != null) {
            try {
                byte[] png = ((TakesScreenshot) DriverFactory.driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", scenario.getName());
            } catch (Exception e) {
                MyLogger.log.warn("Could not capture failure screenshot: {}", e.getMessage());
            }
        }
    }
}
