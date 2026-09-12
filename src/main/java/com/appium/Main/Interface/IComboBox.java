package com.appium.Main.Interface;

import io.cucumber.datatable.DataTable;

public interface IComboBox {
    void SelectComboValue(String comboBoxKey, String value);
    void ValidateComboBoxValues(String comboBoxKey, DataTable table);
}
