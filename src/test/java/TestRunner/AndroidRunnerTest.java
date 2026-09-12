package TestRunner;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"StepDefinitions", "TestRunner"},
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "json:target/cucumber/android.json"
        },
        tags = "not @ios-only and not @ignore"
)
public class AndroidRunnerTest extends BaseRunner {
}
