Feature: Settings Search
  As a device owner
  I want to search Settings by keyword
  So that I can find an option without browsing every section

# ── Happy Path ────────────────────────────────────────────────────

# Test Case Summary: Device Owner searches for Battery and a matching result is listed
# Precondition: Settings app is open on its home screen
@SmokeAutomated @High @ABK-102 @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner searches for Battery and sees a matching result
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "SettingsSearchBar"
  And Fill TextBox "SettingsSearchInput" "Battery"
  Then List "SearchResultTitles" should contain "Battery"

# ── Validation / Negative ────────────────────────────────────────

# Test Case Summary: Device Owner searches for a nonsense term and no titled results are listed
# Precondition: Settings app is open on its home screen
@RegressionAutomated @Medium @ABK-102 @ClaudeGeneratedTest @smokeBDD
Scenario: Device Owner searches for a nonsense term and gets no results
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "SettingsSearchBar"
  And Fill TextBox "SettingsSearchInput" "zqxjkvbnm"
  Then "SearchResultTitles" should not be present

# ── Edge Cases ───────────────────────────────────────────────────

# Test Case Summary: Device Owner searches several common terms and each returns at least one result
# Precondition: Settings app is open on its home screen
@RegressionAutomated @Low @ABK-102 @ClaudeGeneratedTest @smokeBDD
Scenario Outline: Device Owner searches for "<term>" and sees "<result>" in the results
  Given Wait for Visibilty till 15 "SettingsSearchBar"
  When Click Button "SettingsSearchBar"
  And Fill TextBox "SettingsSearchInput" "<term>"
  Then List "SearchResultTitles" should contain "<result>"

  Examples:
    | term    | result   |
    | Display | Display  |
    | Sound   | Sound & vibration |
    | Storage | Storage  |
