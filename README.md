# automationexercise

UI test automation for four scenarios from
[automationexercise.com/test_cases](https://automationexercise.com/test_cases),
built on the Page Object Model with Selenium WebDriver, TestNG and Maven.

The suite drives the real public site end to end — registering an account,
placing an order and deleting the account again — so every scenario is verified
against the live application rather than a mock.

## Test case coverage

| Test case | Scenario | Official steps | Automated |
|---|---|---|---|
| 1 | Register User | 18 | Yes |
| 9 | Search Product | 8 | Yes |
| 16 | Place Order: Login before Checkout | 17 | Yes |
| 25 | Verify Scroll Up using 'Arrow' button and Scroll Down functionality | 7 | Yes |

Every official step is asserted, not merely executed. No other test cases are in
scope.

| Test class | Test case |
|---|---|
| `TestCase1RegisterUserTest` | 1 |
| `TestCase9SearchProductTest` | 9 |
| `TestCase16PlaceOrderLoginBeforeCheckoutTest` | 16 |
| `TestCase25ScrollUpTest` | 25 |

## Technologies

| Technology | Version | Used for |
|---|---|---|
| Java | 17 | language level (`maven.compiler.source/target`) |
| Selenium WebDriver | 4.35.0 | browser automation, `PageFactory` |
| TestNG | 7.11.0 | test runner, parallel execution, listeners |
| Maven | 3.6.3+ | build and test execution (Surefire 3.5.2) |
| Log4j2 | 2.25.1 | logging, `ThreadContext` (MDC), per-test log files |
| Allure | 2.29.1 | reporting and failure screenshots |

Page Object Model with `PageFactory` `@FindBy` elements. No SLF4J, Cucumber,
Hamcrest or dependency-injection framework is used — TestNG assertions and
plain Java only.

## Prerequisites

- **JDK 17 or newer.** The project compiles to Java 17; an older JDK will not
  build it.
- **Maven 3.6.3+** (required by Surefire 3.5.2), or use your IDE's bundled Maven.
- **A local browser** matching `browser` in `config.properties` — Chrome by
  default. Selenium Manager downloads the matching driver automatically, so
  there is no chromedriver/geckodriver setup.
- **Git** only to clone the repository.
- **Allure CLI** only if you want to view the HTML report (see
  [Reporting](#reporting)). Tests run and produce results without it.

## Running the tests

```bash
mvn clean test
```

That runs all four scenarios via `testng.xml`, which Surefire picks up
automatically.

Override any setting for a single run:

```bash
mvn clean test -Dheadless=false -Dbrowser=firefox
```

```bash
mvn clean test -DLOG_LEVEL=DEBUG -Dthread.count=1
```

### Running an individual test

A single class:

```bash
mvn test -Dtest=TestCase9SearchProductTest -DfailIfNoSpecifiedTests=false
```

A single method:

```bash
mvn test -Dtest=TestCase9SearchProductTest#searchProduct -DfailIfNoSpecifiedTests=false
```

Several classes:

```bash
mvn test -Dtest=TestCase1RegisterUserTest,TestCase25ScrollUpTest -DfailIfNoSpecifiedTests=false
```

`-Dtest` bypasses `testng.xml`, so the selected tests run sequentially and the
suite listener does not register. The framework accounts for this: `BaseTest`
applies the log level itself, so logging behaves identically either way.

From an IDE, run `testng.xml` directly, or run a test class. No IDE run
configuration is committed — see [One place to configure](#one-place-to-configure).

## Configuration

All settings live in `src/main/resources/config.properties`:

```properties
browser=chrome
headless=true
thread.count=2
explicit.wait.seconds=30
base.url=https://automationexercise.com
LOG_LEVEL=INFO
```

| Property | Default | Values | Purpose |
|---|---|---|---|
| `browser` | `chrome` | `chrome`, `firefox`, `gecko`, `edge` | Browser to launch |
| `headless` | `true` | `true`, `false` | Run without a visible window |
| `thread.count` | `2` | positive integer (`1` = sequential) | Parallel worker threads |
| `explicit.wait.seconds` | `30` | positive integer | Timeout for every wait |
| `base.url` | `https://automationexercise.com` | any URL | Application under test |
| `LOG_LEVEL` | `INFO` | `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR` | Logging verbosity |

Business values live separately in `src/main/resources/testdata.properties`,
because they describe *what* the tests exercise rather than *how* the framework
runs:

| Key | Used by |
|---|---|
| `search.product` | Test Case 9 — the term typed into product search |
| `home.hero.text` | Test Case 25 — text expected after scrolling back up |
| `checkout.comment` | Test Case 16 — the order comment |
| `payment.card.number`, `payment.cvc`, `payment.expiry.month`, `payment.expiry.year` | Test Case 16 — dummy card details |

### Missing or invalid values

| Situation | Behaviour |
|---|---|
| `LOG_LEVEL` missing, blank or unrecognised | Warns and falls back to `INFO` |
| Any other property missing or blank | Fails at startup: `Missing required property '<key>'` |
| `thread.count` / `explicit.wait.seconds` not a positive number | Fails at startup with the offending value |
| `browser` not a supported name | Fails at startup listing the supported values |
| `headless` not `true`/`false` | Parsed leniently — anything other than `true` is treated as `false` |

Counts fail loudly rather than defaulting, because a typo would otherwise change
how the suite runs while appearing to work.

### One place to configure

`config.properties` is the single source of truth. Every key can be overridden
with `-D` for one run, and nothing else sets a value:

- **Maven** forwards each key to the test JVM as an *empty* property. A blank
  value is ignored, so the file supplies the default unless you pass `-D`.
- **Jenkins** parameters all default to empty for the same reason, so an
  unattended build behaves exactly like `mvn clean test` locally.
- **No IDE run configuration is committed.** `.run/` is git-ignored — a
  checked-in run config is a second home for settings and can only drift out of
  step with the build.

`headless` defaults to `true` so that one value is correct everywhere: a CI
agent has no display, and any other default would force CI to override the file
and stop matching local runs. Pass `-Dheadless=false` to watch it run.

## Project structure

```text
src/main/java
├── config
│   ├── PropertiesLoader.java      classpath properties reader (-D wins over file)
│   ├── ConfigReader.java          framework settings
│   ├── TestData.java              business test data
│   └── LoggingConfigurator.java   applies LOG_LEVEL at startup
├── factory
│   └── DriverFactory.java         ThreadLocal WebDriver lifecycle
├── features
│   └── InterruptionHandler.java   advertisement detection and recovery
├── listeners
│   ├── SuiteListener.java         run banner, thread count, result summary
│   └── TestListener.java          per-test logging, failure screenshots
├── models
│   ├── Account.java               registration data, unique email per instance
│   └── Payment.java               dummy card data
├── pages
│   ├── BasePage.java              shared header navigation, PageFactory init
│   ├── PageManager.java           the page a test starts on
│   ├── HomePage.java              LoginPage.java        SignupPage.java
│   ├── AccountCreatedPage.java    AccountDeletedPage.java
│   ├── ProductsPage.java          CartPage.java         CheckoutPage.java
│   └── PaymentPage.java           OrderPlacedPage.java
└── utils
    ├── BrowserActions.java        navigation and window scrolling
    ├── UiActions.java             element interaction
    ├── WaitUtils.java             all synchronisation
    └── ScreenshotUtils.java       failure capture

src/main/resources
├── config.properties              framework settings
├── testdata.properties            business test data
└── log4j2.xml                     console, combined file, per-test files

src/test/java/tests
├── base/BaseTest.java             per-test browser lifecycle
├── TestCase1RegisterUserTest.java
├── TestCase9SearchProductTest.java
├── TestCase16PlaceOrderLoginBeforeCheckoutTest.java
└── TestCase25ScrollUpTest.java

testng.xml                         suite definition and listeners
```

Page objects and listeners live under `src/main/java` so they compile as
reusable framework code; only the scenarios themselves are test sources.

## Architecture

```text
Test              scenario flow + assertions
  ↓
PageManager  →  Page Object      locators + page behaviour, returns state
  ↓
UiActions         wait → dismiss interruption → act → log
  ↓
WaitUtils         explicit waits, the only synchronisation
  ↓
WebDriver         one per thread, from DriverFactory
```

Responsibilities are separated:

- **Tests** hold the scenario and every assertion. They contain no locators, no
  `WebDriver` calls and no waits.
- **Page objects** own locators and page behaviour and expose *state*
  (`getLoggedInUsername()`, `isSearchResultVisible()`) instead of asserting, so
  every expectation is visible in the test.
- **UiActions** is the only place an element is touched, so every interaction
  gets the same waiting, logging and advert recovery. `BasePage` does not expose
  the `WebDriver` to page objects, so this cannot be bypassed.

### Page creation

`PageManager` supplies the page a test *starts* on; after that each navigation
method returns the next page object (`home.goToProducts()`), which re-runs
`PageFactory.initElements` against the page that just loaded. Pages are never
constructed inside a test, and no element proxy is reused across a navigation —
which is what keeps stale references out of a site that rebuilds its product and
cart tables.

## Parallel execution

Enabled in `testng.xml`:

```xml
<suite name="Automation Exercise Required Test Cases" parallel="classes">
```

The suite declares the parallel *mode*; the *amount* comes from
`thread.count` in `config.properties`, applied by `SuiteListener` before the
suite builds its executor. That keeps run behaviour in one file.

Thread safety rests on three things:

- **`ThreadLocal<WebDriver>`** in `DriverFactory`. Each test creates its own
  browser and quits it in `@AfterMethod`, with `ThreadLocal.remove()` in a
  `finally` so a reused worker thread never inherits a stale driver.
- **No shared mutable state.** Page objects are created per test; `PageManager`
  is instantiated per test in `BaseTest`.
- **Unique test data per instance**, so two tests registering at the same moment
  cannot collide.

Logging stays readable because every line carries its thread *and* test name —
see [Logging](#logging).

Verified behaviour: `thread.count=1` runs on a single worker thread,
`thread.count=4` runs on four.

## Wait strategy

Every wait goes through `WaitUtils`, so the timeout is configured once and
implicit/explicit mixing cannot occur. There are **no implicit waits and no
`Thread.sleep`** anywhere in the project.

- `visible(...)` — `ExpectedConditions.visibilityOf` before reading state.
- `clickable(...)` — visible *and* enabled before clicking or typing. Typing
  waits for clickable rather than merely visible: a painted field is not
  necessarily ready to accept input.
- `StaleElementReferenceException` is ignored while polling, because the site
  rebuilds parts of the DOM mid-condition.

`pageLoaded()` is the one advisory wait. `driver.get()` already blocks until the
load event, but the site's adverts then start navigations of their own that push
`readyState` back to `loading`. Failing a test for that would fail it for
something unrelated to the application, and every following interaction waits for
its own element anyway.

## Advertisement handling

Automation Exercise intermittently serves Google advertisements that intercept
clicks. Recovery lives in `UiActions`, so tests and page objects never deal with
it: reusable UI actions detect and dismiss an interruption before an
interaction, and escalate only when a click is genuinely intercepted.

Two shapes matter, and size alone does not identify them — a full-screen
vignette blocks everything, while a short banner injected *between form fields*
is far too small to look like an overlay yet still swallows the click on the
control beneath it. Recovery therefore targets whatever actually overlaps the
element being clicked.

### The scroll-up arrow (Test Case 25)

Worth knowing, as it is the least obvious part of the project. Two site defects
make a naive implementation flaky:

1. The arrow is revealed by a jQuery handler bound to the `scroll` event, and a
   programmatic scroll does not reliably deliver one — in a headless or
   background window the event can be throttled away, leaving the arrow
   permanently hidden. The framework dispatches the event explicitly.
2. The arrow scrolls with `jQuery.animate(…, 'easeOutQuad')`, but the site never
   loads the jQuery Easing plugin, so at a normal duration jQuery calls an
   easing function that does not exist and the animation dies. Disabling
   animations gives the tween a zero duration, the path where jQuery skips the
   easing lookup.

That flag lives on the document and an advert navigation can discard it, so the
click is retried once. Nothing in the test scrolls the page directly — the page
can only reach the top if the arrow's own handler moves it, so a genuinely
broken arrow still fails.

## Test data

Framework configuration and business data are separate files (see
[Configuration](#configuration) for the `testdata.properties` keys). Everything
user-specific is generated at runtime:

- **Email addresses are unique per run.** `Account.Builder` builds
  `qa.<random><base36 timestamp><sequence>@example.com`. The timestamp makes it
  unique across runs and the sequence across threads within the same
  millisecond, so parallel registrations cannot collide and the suite can be
  repeated indefinitely without "email already exists" failures.
- **Test Case 16 creates its own user through the UI.** The official scenario
  starts by logging in, so it assumes a registered user. Rather than depending
  on Test Case 1 having run, or on a hard-coded shared account that only works
  once, the test registers an account as a precondition, logs out, and then
  performs the official flow from step 4.
- **Both account tests delete their account** as the final official step, so
  they clean up after themselves.

The result is that all four tests run individually, together, in any order, and
repeatedly.

### Payment values

Test Case 16 needs card details to complete the checkout form. They live in
`testdata.properties`:

```properties
payment.card.number=4111111111111111
payment.cvc=123
payment.expiry.month=12
payment.expiry.year=2030
```

These are dummy values — a well-known test card number the practice site
accepts — and correspond to no real card or customer. They are never logged;
`UiActions.type()` records a character count, never the value.

Worth being explicit about the distinction. CVC/CVV is payment *authentication*
data and expiry is payment-card data, so with real credentials neither belongs
in source control. A production framework would inject them at runtime — CI/CD
secret variables, environment variables, or a secrets manager — rather than
reading them from a file in the repository. Passing them as command-line system
properties is technically possible but is not the secure answer: arguments show
up in shell history, process listings and CI logs.

Keeping non-real values in `testdata.properties` here is a deliberate scope
decision for a technical assignment. It keeps the project self-contained, so it
can be cloned and run without configuring external secrets first. It is not a
model for handling real payment credentials.

## Logging

Log4j2, configured from `config.properties`:

```properties
LOG_LEVEL=INFO
```

`LoggingConfigurator` reads it through `ConfigReader` and applies it to the root
logger at startup, so changing verbosity needs no code change and no
recompilation. Framework classes just call `log.debug(...)` / `log.info(...)` —
none of them read `LOG_LEVEL`. Override per run with `-DLOG_LEVEL=DEBUG`.

Every line carries the thread and the test that produced it:

```text
2026-08-27 13:28:02.778 [TestNG-test-1] [registerUser] INFO  BrowserActions - Opening URL: https://automationexercise.com
2026-08-27 13:28:02.807 [TestNG-test-2] [searchProduct] DEBUG UiActions - Typing 8 character(s) into input#search_product
```

The test name comes from Log4j2's `ThreadContext` (its MDC), set in
`BaseTest.setUp` and cleared in `tearDown`. Clearing matters: TestNG reuses
worker threads, so a name left behind would be stamped onto the next test.
Because the context is thread-local, shared components such as `UiActions` are
labelled with the correct test without knowing anything about it.

Output goes to three places:

| Destination | Contents |
|---|---|
| Console | Everything, as it happens |
| `logs/automation.log` | Everything, rolled daily and at 10 MB |
| `logs/tests/<testName>.log` | One file per test |

Per-test files exist because parallel runs interleave the combined log. Routing
on the **test name** rather than the thread is what makes them useful — TestNG
reuses worker threads, so per-thread files would splice unrelated tests
together. Each file is rewritten per run, so it always describes the most recent
execution of that test.

Element values are never logged: the same `type()` call fills in an address line
and a password.

## Reporting

Allure results are written to `allure-results/` on every run. Viewing them needs
the [Allure CLI](https://allurereport.org/docs/install/) installed separately —
the project does not bundle the `allure-maven` plugin:

```bash
allure serve allure-results
```

```bash
allure generate allure-results --clean -o allure-report
```

Tests run and report pass/fail through Maven and TestNG without the CLI; it is
only needed for the HTML report.

### Screenshots

`TestListener` captures a PNG when a test fails and attaches it to the Allure
result. This is the only place a screenshot is taken — neither `BaseTest` nor
the tests capture one — so a failure produces exactly one attachment. Capture is
skipped safely if the browser never started, so a driver-creation failure is
reported as itself rather than being masked.

Screenshots are attached to the Allure result rather than written to a separate
folder; view them in the report against the failing test.

## Design decisions

- **Page Object Model with PageFactory** — locators live with the page they
  belong to; navigation methods return the next page object so a test reads as a
  business flow.
- **Page objects return state, tests assert** — every expectation stays visible
  in the scenario instead of being buried in a page class.
- **Centralised driver management** — `DriverFactory` owns creation and cleanup;
  no test touches `WebDriver`.
- **A single interaction layer** — `UiActions` is the only place elements are
  touched, so waiting, logging and advert recovery are applied uniformly.
- **A single synchronisation point** — one timeout, no implicit waits, no
  `Thread.sleep`.
- **Dynamic test data** — unique accounts per run keep the suite repeatable and
  independent of execution order.
- **One place to configure** — `config.properties` governs Maven, Jenkins and
  IDE runs alike; `-D` overrides a single run.
- **Thread-safe by construction** — `ThreadLocal` driver, per-test page objects,
  per-thread logging context cleared after each test.
