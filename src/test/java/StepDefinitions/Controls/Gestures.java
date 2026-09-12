package StepDefinitions.Controls;

import com.appium.Main.Common.ElementFinder;
import com.appium.Main.Common.GeneralHelper;
import com.appium.Main.Factory.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class Gestures {

    private static Gestures instance;

    public static Gestures Instance() {
        if (instance == null) instance = new Gestures();
        return instance;
    }

    public void swipe(int startX, int startY, int endX, int endY, int millis) {
        GeneralHelper.swipe(startX, startY, endX, endY, millis);
    }

    @And("Swipe Down")
    public void SwipeDown() {
        GeneralHelper.SwipeDown();
    }

    @And("Swipe Up")
    public void SwipeUp() {
        GeneralHelper.SwipeUP();
    }

    @When("Swipe the screen, Direction {string}, Duration {long}, count {int}")
    public void SwipeInDirection(String direction, long durationMillis, int count) {
        GeneralHelper.SwipeInDirection(direction, durationMillis, count);
    }

    @And("Tap On Control {string}")
    public void TapOnElement(String key) {
        GeneralHelper.TapOnElement(ElementFinder.FindElement(key, DriverFactory.driver));
    }
}
