package com.appium.Main.Interface;

import io.cucumber.datatable.DataTable;

import java.util.List;

public interface IList {
    /** Text of every item matched by the list key, scrolling to the end of the list. */
    List<String> GetListTexts(String listKey);

    /** Taps the first item whose text equals the given value. */
    void ClickListItemByText(String listKey, String text);

    /** Checks Key/Value pairs against child elements of the item at the given index. */
    void ValidateListValues(String listKey, int index, DataTable table);
}
