package TestRunner;

import com.appium.Main.Common.Configuration;
import com.appium.Main.Factory.DriverFactory;
import com.appium.Main.Setup.AppiumServer;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * One driver per TestNG suite: the suite XML says which device/platform to
 * use, the driver starts once before the first scenario and quits after the last.
 */
public class BaseRunner extends AbstractTestNGCucumberTests {

    @Parameters({"Key", "DeviceName", "PlatformVersion", "PlatformName", "Udid"})
    @BeforeSuite(alwaysRun = true)
    public void setup(String key, String deviceName, String platformVersion, String platformName, @Optional("") String udid) {
        Configuration.InitializeAppDetails(key, deviceName, platformVersion, platformName, udid);
        Configuration.SetExtentProperties();
        Configuration.SetLog4jProperties();
        AppiumServer.startServer();
        Configuration.InitializeGlobalVariables();
    }

    @AfterSuite(alwaysRun = true)
    public void quit() {
        DriverFactory.quitDriver();
        AppiumServer.stopServer();
    }
}
