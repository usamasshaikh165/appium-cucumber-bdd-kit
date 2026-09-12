package TestRunner;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"StepDefinitions", "TestRunner"},
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:target/cucumber/ios.json"
        },
        tags = "not @android-only and not @ignore"
)
public class iOSRunnerTest extends BaseRunner {
}
