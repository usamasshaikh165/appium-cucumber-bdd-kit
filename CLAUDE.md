# CLAUDE.md — Mobile Automation Rulebook (Appium · Cucumber · TestNG · Java)
> **Stack:** Java 17 · Appium java-client 9 · Selenium 4 · Cucumber 7 · TestNG
> **Scope:** How this kit is structured, how to add tests, locator and step rules, BDD governance

---

## SECTION 1 — Test Design Principles

- Every scenario is **independent**: the `Before` hook relaunches the app, so never rely on state left by an earlier scenario.
- One scenario validates **one user behaviour**. Do not chain unrelated journeys.
- Assert what the user sees (a label, a list item, a toast), never implementation detail (resource ids, class names) in step text.
- Flakiness is a defect. Fix the wait or the locator; never add `Wait for 5000` as a fix.
- Never hardcode credentials, device ids, or URLs in code or feature files. They come from `config.properties`, `GlobalVariables.json`, the TestNG suite XML, or environment variables.

## SECTION 2 — Framework Structure

| Layer | Location | Rule |
|---|---|---|
| Feature files | `src/test/resources/features/<Area>/` | Business language only, using the generic steps in Section 3. |
| Generic step library | `src/test/java/StepDefinitions/Controls/` | Keyword-style steps that take a **locator key**. Extend here only with steps that make sense for any app. |
| App-specific steps | `src/test/java/StepDefinitions/<Area>/` | Create when a flow cannot be expressed with the generic steps. Reuse `Button`, `Textbox`, `Labels` instances instead of calling the driver directly. |
| Locator repository | `src/test/resources/JsonFiles/AndroidLocators.json`, `iOSLocators.json` | Every element has a key. **Same keys on both platforms** so feature files stay platform-neutral. |
| Core | `src/main/java/com/appium/Main/` | Driver factory, capabilities, `ElementFinder` (auto-scroll), `GeneralHelper` (W3C gestures, waits), platform control implementations behind interfaces. |
| Runners | `src/test/java/TestRunner/` + `src/test/resources/TestRunner/*.xml` | One driver per TestNG suite. Device details are suite parameters. |

## SECTION 3 — Locator Rules

- Locator strategies, in order of preference: `AccessibilityId`, `Id`, `UiAutomator` (Android) / `Predicate`, `ClassChain` (iOS), then `XPath`. Avoid absolute XPaths.
- Capture locators from the live app with Appium Inspector. Never write a locator from memory.
- Keys are `PascalCase` and describe the element's role, not its position: `SaveButton`, `EmailInput`, `OrderRowTitles`.
- An element the finder must scroll to on Android may use a `UiAutomator` `UiScrollable(...).scrollIntoView(...)` value; `ElementFinder` also swipes automatically when a plain locator is not on screen.
- The key `ToastMessage` is reserved for the platform toast, `ComboBoxValues` for the open option list of any dropdown.

## SECTION 4 — Feature File Rules

- Steps use the generic library; the argument is always a locator key or a literal user-visible value.
- Tag `@android-only` / `@ios-only` when a scenario cannot run on both platforms. `@ignore` skips a scenario entirely.
- Follow the metadata block and tag order in Section 6 on every scenario.

## SECTION 5 — Java Rules

- Java 17 features are allowed (`switch` expressions, `var` in local scope, records for data).
- Use `WebElement`; `MobileElement` no longer exists in java-client 9.
- Gestures go through `GeneralHelper` (W3C `PointerInput` sequences). `TouchAction` is removed and must not be reintroduced.
- Waits use `WebDriverWait` with `Duration`. `Thread.sleep` only inside `GeneralHelper.Wait`, and only as a last resort.
- Log through `MyLogger.log` with parameterised messages (`"{} clicked", key`), never string concatenation in log calls.
- Assertions use TestNG `Assert` with a message that names the locator key.

## SECTION 6 — BDD Test Case Governance (Gherkin / Vansah)

> Applies to every `.feature` file in this project. Supplements Section 1 (test design) and Section 4 (feature files)
### 6.1 Mandatory Per-Scenario

Every scenario **must** have this comment block immediately above its tags, in this exact order:

```gherkin
# Test Case Summary: <one sentence — what this scenario verifies>
# Precondition: <specific system state required before the test runs, or N/A>
```

Rules:
- `# Test Case Summary:` — one sentence, present tense, states the observable outcome being verified.
- `# Precondition:` — sourced directly from the Jira story. Must capture the full criteria before the test can execute:
  - Story has a **Preconditions heading** → use that content
  - Story **explicitly uses the word "precondition"** anywhere in Description, AC text, or Definition of Done → extract and use it. **Sections labeled "Dependencies", "Assumptions", "Non-Functional Requirements", or "Notes" do NOT count — these are not preconditions.**
  - **`Given` clauses inside the Acceptance Criteria ARE a valid precondition source** (project rule, 2026-08-28) — use the `Given` clause of the AC that the scenario covers. This previously said the opposite; it was changed because our ACs are consistently written in Given/When/Then form, so the `Given` **is** the story's own statement of the state required before the action, and treating it as unusable stalled every story on a runtime prompt for something the story already said. Take the `Given` as written — reword only for readability, never to add a condition the AC did not state.
  - Story has **no Preconditions heading, no literal "precondition" text, AND no usable AC `Given` clause** → **prompt the user at runtime**: "No precondition was found in the story. Please provide the precondition for this scenario, or type N/A if none applies." — do NOT write `N/A` automatically without asking
  - NEVER infer, assume, or invent a precondition not written in the story
- These two lines map directly to Vansah's **Test Case Summary** and **Precondition** tabs on import.

### 6.2 Tag Format

Tags must appear in this exact order on the line immediately after the metadata block:

```
@<Smoke|Regression> @<Automatable|Non-Automatable> @<High|Medium|Low> @<STORY_ID> [@BusinessCase] @ClaudeGeneratedTest @smokeBDD
```

> **Important distinction — Labels vs Priority:**
> - **Labels** (map to the Vansah *Labels* field): `@Smoke`, `@Regression`, `@Automatable`, `@Non-Automatable`, `@BusinessCase`, `@ClaudeGeneratedTest`, `@smokeBDD`
> - **Priority** (maps to the Vansah *Priority* field): `@High`, `@Medium`, `@Low` — this is a separate Vansah concept from Labels and must be treated independently

---

**Label rules:**

| Label | Rule |
|---|---|
| `@Smoke` | All `@High` (P1) scenarios |
| `@Regression` | All `@Medium` (P2) and `@Low` (P3) scenarios |
| Never both | `@Smoke` and `@Regression` must never appear on the same scenario |
| `@Automatable` | Outcome is deterministic and observable — see decision logic below |
| `@Non-Automatable` | Requires human judgment or has an uncontrollable dependency — see decision logic below |
| Never both | `@Automatable` and `@Non-Automatable` must never appear on the same scenario |
| `@[STORY_ID]` | Jira story number for traceability (e.g. `@ABK-1234`) |
| `@BusinessCase` | Optional — add only to scenarios under the "Business Rules & Restrictions" section; omit from Happy Path, Validation, Edge Cases, and E2E scenarios |
| `@ClaudeGeneratedTest` | Always present on every Claude-generated scenario — used to distinguish AI-generated test cases from manually written ones in Vansah |
| `@smokeBDD` | Always last, on every scenario — used by Vansah import tooling |

**`@Smoke` vs `@Regression` — decision logic:**

Decision is based on the scenario's priority tag:

| Priority | Label |
|---|---|
| `@High` (P1) | `@Smoke` |
| `@Medium` (P2) | `@Regression` |
| `@Low` (P3) | `@Regression` |

- All P1 (`@High`) scenarios are labelled `@Smoke`.
- All P2 (`@Medium`) and P3 (`@Low`) scenarios are labelled `@Regression`.
- The full regression suite covers all priorities — P1 smoke cases are included when running regression.

---

**`@Automatable` vs `@Non-Automatable` — decision logic (applies to Frontend, Backend, and API products):**

Ask: *"Can the outcome of this scenario be verified by a tool without human intervention?"*

Mark `@Automatable` if the outcome is observable via **any** of these:
- UI element state, text, navigation, or visibility (Frontend)
- API response body, status code, or header (API / Backend)
- Database record, field value, or row existence (Backend)
- Log entry, audit trail record, or event emission (Backend / API)
- Email or notification content via a test hook or mailbox API (e.g. Mailosaur)

Mark `@Non-Automatable` if **any** of these apply:
- Requires a human to visually judge correctness (e.g. layout aesthetics, print output)
- Depends on a live third-party system with no sandbox or test hook (live payment gateway, live telecom SMS, uncontrolled external API)
- Outcome is non-deterministic with no controllable seed or mock (true randomness, real-time market data)
- Requires CAPTCHA or anti-bot challenge that blocks automation by design
- Requires manual infrastructure access (physical server inspection, data centre check)

> **Default to `@Automatable`** — if the scenario does not match any `@Non-Automatable` condition above, it is automatable regardless of product type.

**Priority tag rules:**

| Priority Tag | Rule |
|---|---|
| `@High` | Happy path, E2E lifecycle, data-loss risk |
| `@Medium` | Role restriction, validation, secondary feature paths |
| `@Low` | Edge cases, cosmetic, rarely exercised paths |
| Exactly one | Every scenario must carry exactly one priority tag — never zero, never two |

### 6.3 Coverage Categories

Section 1.2 requires happy path, validation, and boundary/edge cases. For Gherkin scenarios the full required coverage is:

| Category | Minimum | Notes |
|---|---|---|
| Happy path | 1 | Primary success flow — tag `@Smoke @High` |
| Per acceptance criterion | 1 per AC | Every numbered AC in the Jira story |
| Per user role | 1 per role | Where behaviour differs between roles |
| Workflow states | 1 per state | Pending, Approved, Rejected, etc. |
| Positive data variations | 1+ | Valid alternate inputs that should succeed |
| Negative — required field missing | 1 per field | Each mandatory field left blank |
| Negative — invalid format | 1 per field | Wrong type / format input |
| Boundary values | 3 | Below limit, at limit, above limit |
| Role restriction | 1 per rule | Action blocked for unauthorised role |
| Business restriction | 1 per rule | Duplicate submission, guardian lock, etc. |
| Workflow ordering | 1 | Step N cannot occur before step N−1 |
| Rejection path | 1 per approver | Each approver who can reject |
| Edge cases | 1+ | Empty state, max-length, special characters |
| Full E2E lifecycle | 1 | Submission → all approvals → final state — tag `@Smoke @High` |
| Data-driven (Outline) | 1 outline | Minimum 3 rows in the `Examples:` table |

### 6.4 Scenario Title Rules

- Titles must be **business-readable and role-explicit**.
- Follow the pattern: `[Role] [performs action] and [observable outcome]`
- GOOD: `Admin User submits order and receives confirmation`
- GOOD: `Standard User applies filter and list updates immediately`
- GOOD: `Guest User enters invalid credentials and sees error message`
- BAD: `Test approve button` / `Verify scenario 3` / `Check the form`

### 6.5 Gherkin Discipline

- `Given` — system state / precondition (never an action). **Always required** — every scenario must include a `Given` step that establishes the system state or precondition before the action.
- `When` — the user action or triggering event. **Always required** — every scenario must have at least one `When`.
- `Then` — the observable, user-facing outcome (specific — never vague like "it works"). **Always required**.
- `And` — continuation of the previous keyword type.
- Never jump from `Given` directly to `Then` — there must always be a `When` before `Then`.
- No technical language in step text: no CSS selectors, API paths, button IDs, HTTP methods.
- Steps must be **unambiguous** — one interpretation only; avoid words like "appropriate", "correct", "properly", "some".
- Steps must be **suitable for automation** — outcomes must be deterministic and observable in the UI or API; avoid subjective or human-judgment-only assertions.

### 6.6 Vansah Field Mapping

| Feature file element | Vansah field | Type |
|---|---|---|
| `# Test Case Summary: <text>` | Test Case Summary tab | Metadata |
| `# Precondition: <text>` (or `N/A`) | Precondition tab | Metadata |
| `@High` / `@Medium` / `@Low` | Priority field | **Priority** (not a Label) |
| `@Smoke` / `@Regression` | Labels field | Label |
| `@Automatable` / `@Non-Automatable` | Labels field | Label |
| `@BusinessCase` | Labels field | Label (optional) |
| `@ClaudeGeneratedTest` | Labels field | Label (always present on Claude-generated scenarios) |
| `@smokeBDD` | Labels field | Label (always present) |
| Given / When / Then / And steps | Test Script — BDD - GHERKIN | Test Steps |

### 6.7 Feature File Section Grouping

Use these section comments to group scenarios. Only include groups that apply to the feature:

```gherkin
# ── Happy Path ────────────────────────────────────────────────────
# ── Role-Based Access ─────────────────────────────────────────────
# ── Business Rules & Restrictions ────────────────────────────────
# ── Validation / Negative ────────────────────────────────────────
# ── Edge Cases ───────────────────────────────────────────────────
# ── Workflow States ───────────────────────────────────────────────
# ── End-to-End ───────────────────────────────────────────────────
```

### 6.8 BDD Governance Review Checklist

**Mandatory components**
- [ ] Every scenario has `# Test Case Summary:` comment.
- [ ] Every scenario has `# Precondition:` comment; value sourced from story or confirmed with user at runtime — never left blank, never auto-written as `N/A` without prompting.
- [ ] Every scenario has exactly one priority tag (`@High` / `@Medium` / `@Low`) — maps to Vansah **Priority field**, not Labels.
- [ ] Every scenario has the correct label tags (`@Smoke`/`@Regression`, `@Automatable`/`@Non-Automatable`, story ID, `@ClaudeGeneratedTest`, `@smokeBDD`).

**Tag format**
- [ ] Tags are in the mandatory order: suite label → automation label → priority tag → story tag → `@BusinessCase` (if applicable) → `@ClaudeGeneratedTest` → `@smokeBDD`.
- [ ] Exactly one of `@Smoke` / `@Regression` per scenario — `@Smoke` for all `@High` (P1) scenarios; `@Regression` for all `@Medium` (P2) and `@Low` (P3) scenarios (Label).
- [ ] Exactly one of `@Automatable` / `@Non-Automatable` per scenario — decision based on Section 6.2 logic; default is `@Automatable` (Label).
- [ ] Exactly one of `@High` / `@Medium` / `@Low` per scenario (Priority — separate from Labels).
- [ ] `@smokeBDD` is the last tag on every scenario (Label).
- [ ] Jira story tag (`@ABK-XXXX`) present on every scenario.

**Step quality**
- [ ] Every scenario includes a `Given` step that establishes the system state or precondition.
- [ ] Every scenario has at least one `When` and one `Then`.
- [ ] No scenario jumps from `Given` directly to `Then` without a `When`.
- [ ] Steps follow a logical sequential flow — no missing or out-of-order actions.
- [ ] Expected results (`Then` statements) are clearly and specifically defined — not vague.
- [ ] No ambiguity in step language — one interpretation only; no words like "appropriate", "correct", "properly".
- [ ] No technical language (selectors, HTTP methods, API paths) in step text.
- [ ] Steps are suitable for automation — outcomes are deterministic and observable.

**Scenario quality**
- [ ] Scenario aligns with and is traceable to the Jira story requirement or AC.
- [ ] All data-driven cases use `Scenario Outline` with minimum 3 `Examples:` rows.
- [ ] Scenario titles are business-readable and identify the role performing the action.

**Coverage**
- [ ] Coverage matrix satisfied: at least one scenario per AC, per role, per workflow state.
- [ ] Feature file includes positive, negative, and edge case scenarios.
