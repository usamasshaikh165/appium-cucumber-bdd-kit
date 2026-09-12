package StepDefinitions.Controls;

import com.appium.Main.Factory.ComboBoxFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class ComboBox {

    private static ComboBox instance;

    public static ComboBox Instance() {
        if (instance == null) instance = new ComboBox();
        return instance;
    }

    @Given("Select ComboBoxValue {string} {string}")
    public void SelectComboBoxValue(String comboBoxKey, String value) {
        ComboBoxFactory.AppiumCombBox.SelectComboValue(comboBoxKey, value);
    }

    @Given("Select Multiple ComboBoxValues {string} {string}")
    public void SelectMultiple(String comboBoxKeys, String values) {
        String[] keys = comboBoxKeys.split(",");
        String[] vals = values.split(",");
        Assert.assertEquals(keys.length, vals.length, "Give one value per combo box key");
        for (int i = 0; i < keys.length; i++) {
            ComboBoxFactory.AppiumCombBox.SelectComboValue(keys[i].trim(), vals[i].trim());
        }
    }

    @Then("Validate ComboBox: {string} Values:")
    public void ValidateComboBoxValues(String comboBoxKey, DataTable table) {
        ComboBoxFactory.AppiumCombBox.ValidateComboBoxValues(comboBoxKey, table);
    }
}
