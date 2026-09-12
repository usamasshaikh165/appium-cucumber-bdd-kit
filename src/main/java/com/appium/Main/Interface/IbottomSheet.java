package com.appium.Main.Interface;

import io.cucumber.datatable.DataTable;

public interface IbottomSheet {
    void selectBottomSheetValues(String bottomSheetKey, String listKey, DataTable table);
    void choosetBottomSheetValues(String bottomSheetKey, String listKey, String selectedValue);
}
