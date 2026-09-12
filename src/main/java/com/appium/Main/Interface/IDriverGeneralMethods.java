package com.appium.Main.Interface;

/** Platform-specific behaviours that every driver wrapper must provide. */
public interface IDriverGeneralMethods {
    /** Types a one-time code into either a single field or one box per digit. */
    void SubmitVerificationCode(String codeFieldKey, String code);

    /** Hardware/system back navigation. */
    void PressBack();
}
