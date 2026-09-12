Feature: Settings Navigation
  As a device owner
  I want to move between Settings sections
  So that I can reach the option I need

# ── Happy Path ────────────────────────────────────────────────────

# Test Case Summary: Device Owner opens Settings and sees the Network & internet entry on the home screen
# Precondition: Settings app is installed and the device is unlocked
@SmokeAutomated @High @ABK-101 @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner opens Settings and sees the Network & internet entry
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  Then Check the Presence of "NetworkInternetItem"

# Test Case Summary: Device Owner taps Network & internet and the Internet option is displayed
# Precondition: Settings app is open on its home screen
@SmokeAutomated @High @ABK-101 @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner opens Network & internet and sees the Internet option
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "NetworkInternetItem"
  Then Check the Presence of "InternetItem"
  And Press Back
  And Check the Presence of "NetworkInternetItem"

# ── Business Rules & Restrictions ────────────────────────────────

# Test Case Summary: Device Owner scrolls to the About entry at the bottom of Settings and opens it
# Precondition: Settings app is open on its home screen
@RegressionAutomated @Medium @ABK-101 @BusinessCase @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner scrolls to the About entry and opens the device details
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "AboutDeviceItem"
  Then Check the Presence of "DeviceNameLabel"
  And Press Back

# ── Edge Cases ───────────────────────────────────────────────────

# Test Case Summary: Device Owner relaunches Settings and lands on the home screen again
# Precondition: Settings app has been navigated away from its home screen
@RegressionAutomated @Low @ABK-101 @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner closes and relaunches Settings and is back on the home screen
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "NetworkInternetItem"
  And Close App
  And Launch App
  Then Check the Presence of "SettingsSearchBar"
