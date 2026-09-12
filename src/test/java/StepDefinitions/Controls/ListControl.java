package StepDefinitions.Controls;

import com.appium.Main.Factory.ListFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.List;

public class ListControl {

    private static ListControl instance;

    public static ListControl Instance() {
        if (instance == null) instance = new ListControl();
        return instance;
    }

    @Then("Validate the following values of {string} at index {int}")
    public void ValidateListValues(String listKey, int index, DataTable table) {
        ListFactory.AppiumList.ValidateListValues(listKey, index, table);
    }

    @And("Click list item in {string} with text {string}")
    public void ClickListItem(String listKey, String text) {
        ListFactory.AppiumList.ClickListItemByText(listKey, text);
    }

    @Then("List {string} should contain {string}")
    public void ListShouldContain(String listKey, String text) {
        List<String> texts = ListFactory.AppiumList.GetListTexts(listKey);
        Assert.assertTrue(texts.contains(text), "List " + listKey + " does not contain '" + text + "'. Items: " + texts);
    }

    @Then("List {string} should not be empty")
    public void ListShouldNotBeEmpty(String listKey) {
        Assert.assertFalse(ListFactory.AppiumList.GetListTexts(listKey).isEmpty(), "List " + listKey + " is empty");
    }
}
