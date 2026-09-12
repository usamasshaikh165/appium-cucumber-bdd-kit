# appium-cucumber-bdd-kit

A starter kit for **mobile test automation in Java**: Appium (java-client 10) with Cucumber and TestNG, a **JSON locator repository** shared between Android and iOS, a **keyword-style generic step library** so most scenarios need no new Java, auto-scrolling element lookup, W3C gestures, Extent HTML reports with failure screenshots, a **BDD governance rulebook**, and Claude Code commands that generate feature files from Jira stories.

The sample suite runs against the **built-in Android Settings app**, so it works on any emulator with no APK to download. Point `config.properties` at your own app and replace the sample features and locators.

## What is in the box

| Path | Purpose |
|---|---|
| `src/main/java/com/appium/Main/` | Framework core. `Factory/DriverFactory` starts the platform driver; `Setup/Capabilities` builds UiAutomator2 / XCUITest options from config; `Common/ElementFinder` resolves locator keys and scrolls until the element appears; `Common/GeneralHelper` holds W3C swipes, taps, waits and app lifecycle; `DriverClasses/` implement lists, combo boxes and bottom sheets per platform behind `Interface/` contracts; `JsonClasses/ParseLocators` reads the locator repository. |
| `src/test/java/StepDefinitions/Controls/` | The generic step library: buttons, text boxes, labels, lists, combo boxes, alerts, gestures, waits, toasts. Every step takes a locator key from the JSON repository. |
| `src/test/java/TestRunner/` | `BaseRunner` (one driver per TestNG suite), `Hooks` (relaunch app per scenario, screenshot on failure), Android and iOS Cucumber runners. |
| `src/test/resources/features/` | Sample Gherkin features written to the Section 6 governance format in `CLAUDE.md`. |
| `src/test/resources/JsonFiles/` | `AndroidLocators.json`, `iOSLocators.json` (same keys on both), `GlobalVariables.json`, `App.json`, `ComboBoxValues.json`. |
| `src/test/resources/TestRunner/` | TestNG suite files carrying device parameters. |
| `CLAUDE.md` | Rulebook: design principles, structure, locator and step rules, Java rules, and the Gherkin/Vansah governance section. Claude Code reads it automatically. |
| `.claude/commands/` | `/generate-bdd`, `/generate-bdd-import`, `/vansah-import` for AI-assisted scenario generation from Jira. |
| `.github/workflows/android-tests.yml` | CI on a GitHub-hosted Android emulator. |

## Quick start

Requirements: JDK 17+, Maven, Node.js 18+, Android SDK with an emulator (API 30+), and Appium 2:

```bash
npm install -g appium
appium driver install uiautomator2
```

Run:

```bash
# terminal 1
emulator -avd <your_avd> &
appium --base-path / --relaxed-security

# terminal 2
mvn test                                                   # Android-Emulator.xml suite
mvn test -Dcucumber.filter.tags="@SmokeAutomated"          # only smoke scenarios
mvn test -Dsuite=src/test/resources/TestRunner/iOS-Simulator.xml   # iOS (macOS + Xcode + xcuitest driver)
```

Reports: `target/extent-reports/ExtentReport.html` (Extent, with screenshots on failure), `target/cucumber/*.json`, `target/surefire-reports/`. Per-run logs land in `Logs/`.

## Configuration

`src/main/resources/config.properties` describes the app under test. Every key can be overridden by an environment variable in UPPER_SNAKE_CASE (`ANDROID_APP_PACKAGE`, `NO_RESET`, and so on), so CI never edits the file.

| Key | Meaning |
|---|---|
| `AndroidApp` / `iOSApp` | Path to an APK / .app to install. Leave blank to use an app already on the device. |
| `AndroidAppPackage`, `AndroidAppActivity` | Package and launch activity when `AndroidApp` is blank. |
| `iOSBundleId` | Bundle id when `iOSApp` is blank. |
| `AndroidAutomationName`, `iOSAutomationName` | `UiAutomator2` and `XCUITest`. |
| `NoReset` | Keep app data between sessions. |

Device details live in the TestNG suite XML (`Key`, `DeviceName`, `PlatformVersion`, `PlatformName`, `Udid`). The Appium URL comes from `GlobalVariables.json`, overridable with `APPIUM_SERVER_URL_ANDROID` / `APPIUM_SERVER_URL_IOS`. Set `START_APPIUM=true` (plus `APPIUM_JS`) to have the runner start Appium itself.

## How the framework fits together

1. **Locators.json is the contract.** Each element is a key with a strategy and value:

   ```json
   "NetworkInternetItem": { "locator": "XPath", "value": "//android.widget.TextView[@text='Network & internet']" }
   ```

   Strategies: `Id`, `XPath`, `Class`, `Name`, `AccessibilityId`, `UiAutomator` (Android), `Predicate`, `ClassChain` (iOS). Use the **same key names** in `AndroidLocators.json` and `iOSLocators.json` and the feature files run unchanged on both platforms.

2. **Feature files use the generic steps** and pass locator keys:

   ```gherkin
   When Click Button "SettingsSearchBar"
   And Fill TextBox "SettingsSearchInput" "Battery"
   Then List "SearchResultTitles" should contain "Battery"
   ```

3. **ElementFinder does the scrolling.** If a key is not on screen it swipes down until the page stops changing, then back up, before failing. Long screens need no extra steps.

4. **One driver per suite, fresh app per scenario.** `BaseRunner` starts the driver once; the `Before` hook terminates and relaunches the app so scenarios never share state.

### Generic steps available

| Area | Steps |
|---|---|
| Buttons | `Click Button {key}`, `Get Button Text {key}`, `Tap On Control {key}` |
| Text | `Fill TextBox {key} {value}`, `Fill Multiple Textboxes {keys} {values}`, `Fill Values in Multiple Text Boxes as following table`, `Clear textbox {key}`, `Validate Textbox Value {key} {value}`, `Get text of Textbox {key}` |
| Labels | `Get text of label {key}`, `Validate text of label key {key} value {value}`, `Validate label {key} contains {value}` |
| Lists | `List {key} should contain {text}`, `List {key} should not be empty`, `Click list item in {key} with text {text}`, `Validate the following values of {key} at index {n}` |
| Dropdowns | `Select ComboBoxValue {key} {value}`, `Select Multiple ComboBoxValues {keys} {values}`, `Validate ComboBox: {key} Values:` |
| Bottom sheets | `Select Bottom Sheet value, bottomSheetType {key}, list {key}, selectedValue {value}`, `Validate Bottom Sheet {key} list {key} contains:` |
| Alerts | `Accept Alert`, `Dismiss Alert`, `Get Alert Text`, `Validate Alert Text {text}` |
| Gestures | `Swipe Down`, `Swipe Up`, `Swipe the screen, Direction {dir}, Duration {ms}, count {n}` |
| Waits and checks | `Wait for {ms}`, `Wait for Visibilty till {s} {key}`, `Wait until {key} disappears within {s}`, `Check the Presence of {key}`, `{key} should not be present`, `Is Control {key} enabled`, `Is Toggle is Acive,Key {key}`, `Validate value exist in specific element, Key {key} and value {value}` |
| App and misc | `Launch App`, `Close App`, `Press Back`, `Hide Keyboard`, `Validate Toast Message: {text}`, `Submit verification code {code} into {key}`, `Log Message {level} {text}` |

## Writing tests the kit's way

1. Capture locators from the live app with Appium Inspector and add them to both locator files under the same key.
2. Write the feature file under `src/test/resources/features/<Area>/` following Section 6 of `CLAUDE.md`: `# Test Case Summary:` and `# Precondition:` above every scenario, then tags in this order:

   ```
   @<SmokeAutomatable|SmokeAutomated|RegressionAutomatable|RegressionAutomated|NonAutomatable> @<High|Medium|Low> @<STORY_ID> [@BusinessCase] @ClaudeGeneratedTest @smokeBDD
   ```

3. Only when the generic steps cannot express a flow, add a step class under `src/test/java/StepDefinitions/<Area>/` that composes `Button`, `Textbox`, `Labels` and friends. Never call the driver directly from a step class.
4. Tag `@android-only` or `@ios-only` where a scenario is platform-specific.

## Generating scenarios with Claude Code

With Claude Code open in this repository and the Atlassian MCP configured, `/generate-bdd` reads a Jira story, builds the coverage matrix, writes the feature file and step stubs, and updates `VansahConfig.json`; `/generate-bdd-import` also imports to Vansah; `/vansah-import` imports an existing feature file. The Vansah importer itself lives in the sibling kits (`selenium-specflow-bdd-kit`, `playwright-bdd-kit`) and works with any feature file.

## Notes on this version

- Migrated from java-client 7 / Selenium 3 to **java-client 10 / Selenium 4.49**. `MobileElement` is gone, gestures use W3C `PointerInput`, waits use `Duration`, capabilities use `UiAutomator2Options` / `XCUITestOptions`.
- Appium 2 is expected at base path `/` (Appium 1's `/wd/hub` is not used).
- CI uses `reactivecircus/android-emulator-runner` on `ubuntu-latest` with KVM enabled.
