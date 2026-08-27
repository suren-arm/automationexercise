# automationexercise

This project intentionally automates **only** the four test cases requested by
the assignment:

- Test Case 1 - Register User
- Test Case 9 - Search Product
- Test Case 16 - Place Order: Login before Checkout
- Test Case 25 - Verify Scroll Up using Arrow button

No other Automation Exercise test cases are included.

## Base URL

The project uses the assignment link as `base_url` exactly:

```properties
base_url=https://automationexercise.com
```

`BaseTest` opens that URL before every test. Each test then uses the real Home
navigation link to begin its official application flow.

## Architecture

```text
src
├── main
│   ├── java
│   │   ├── config
│   │   │   └── ConfigReader.java
│   │   ├── factory
│   │   │   └── DriverFactory.java
│   │   ├── listeners
│   │   │   └── TestListener.java
│   │   ├── models
│   │   │   ├── Account.java
│   │   │   └── Payment.java
│   │   ├── pages
│   │   │   ├── BasePage.java
│   │   │   ├── PageManager.java
│   │   │   ├── HomePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── SignupPage.java
│   │   │   ├── AccountCreatedPage.java
│   │   │   ├── AccountDeletedPage.java
│   │   │   ├── ProductsPage.java
│   │   │   ├── CartPage.java
│   │   │   ├── CheckoutPage.java
│   │   │   ├── PaymentPage.java
│   │   │   └── OrderPlacedPage.java
│   │   └── utils
│   │       ├── BrowserActions.java
│   │       ├── ScreenshotUtils.java
│   │       ├── UiActions.java
│   │       └── WaitUtils.java
│   └── resources
│       ├── config.properties
│       └── log4j2.xml
└── test
    └── java
        └── tests
            ├── base
            │   └── BaseTest.java
            ├── TestCase1RegisterUserTest.java
            ├── TestCase9SearchProductTest.java
            ├── TestCase16PlaceOrderLoginBeforeCheckoutTest.java
            └── TestCase25ScrollUpTest.java
```

## Account Builder

Accounts are never stored as global shared credentials.

Every account-required test creates its own data:

```java
Account account = Account.builder()
        .name("TC16 User")
        .firstName("TC16")
        .lastName("User")
        .build();
```

The Builder generates a unique email automatically.

For TC16, the test creates the account as a precondition, logs out, and then
executes the official "Login before Checkout" flow using the same Account
object.

## Custom Selenium Actions

Page Objects do **not** directly call normal Selenium interaction methods.

Instead of:

```java
element.click();
element.clear();
element.sendKeys("...");
element.getText();
element.isDisplayed();
driver.findElements(...);
new Select(...);
```

they use:

```java
actions.click(element);
actions.type(element, value);
actions.getText(element);
actions.isDisplayed(element);
actions.getTexts(locator);
actions.selectByVisibleText(element, value);
```

Each element action performs the required explicit wait internally.

Browser-level calls are wrapped by `BrowserActions`.

## Run locally

Default:

```bash
mvn clean test
```

Firefox:

```bash
mvn clean test -Dbrowser=firefox
```

Edge:

```bash
mvn clean test -Dbrowser=edge
```

Headless Chrome:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

## Allure

Run tests:

```bash
mvn clean test
```

Then:

```bash
allure serve allure-results
```

Failure screenshots are attached by `TestListener`.

## Jenkins

The included `Jenkinsfile`:

1. Checks out the repository.
2. Compiles dependencies/project.
3. Runs only TC1, TC9, TC16 and TC25.
4. Preserves Maven's failing exit code.
5. Publishes Surefire results.
6. Publishes Allure results.
7. Archives framework logs.

Configure Jenkins Global Tools with:

- JDK-17
- Maven-3.9

Install the Allure Jenkins plugin before using the `allure` post action.


## Shared header in BasePage

The common Automation Exercise header is defined once in `BasePage`: logo, Home, Products, Cart, Signup/Login, Test Cases, API Testing, Video Tutorials, Contact us, Logged in as, Logout and Delete Account.

## Google vignette handling

`features/GoogleVignetteHandler.java` detects an intermittent `#google_vignette`, tries known close controls in the main document and ad iframes, restores default content, and removes the leftover URL fragment. `BasePage.preparePage()` calls it before page interactions.


## Google vignette click-interception fix

The ad can appear between two Selenium actions:

```text
type signup name
type signup email
Google vignette appears
click Signup
ElementClickInterceptedException
```

Therefore page-level checking alone is not reliable.

`UiActions` now checks `GoogleVignetteHandler` immediately before every
interaction. `click()` additionally catches `ElementClickInterceptedException`,
calls `forceCloseBlockingAd()`, and retries once.

The handler recognizes:

- visible `Close` text,
- `dismiss-button`,
- common Google close aria-labels,
- nested ad iframes,
- `iframe[title='Advertisement']`,
- `aswift_*` frames,
- `#google_vignette`,
- and viewport-sized Google overlays.

Only as a final fallback does it remove a Google advertisement container that
covers most of the viewport. Normal small inline ads are left untouched.


## Page construction and vignette handling

Every concrete Page Object now has an explicit constructor:

```java
public LoginPage() {
    super();
}
```

The common initialization remains centralized in `BasePage`:

```java
this.driver = DriverFactory.getDriver();
this.wait = new WaitUtils(driver);

this.vignette = new GoogleVignetteHandler(driver);
this.vignette.closeIfPresent();

this.actions = new UiActions(driver);
this.browser = new BrowserActions(driver);

PageFactory.initElements(driver, this);
```

There are therefore two vignette protections:

1. **Page constructor** — handles an ad already present when the new page is
   constructed.
2. **UiActions** — checks again immediately before interactions and retries a
   click when `ElementClickInterceptedException` occurs.

### Java multi-catch correction

This is illegal Java:

```java
catch (ElementClickInterceptedException | ElementNotInteractableException e)
```

because `ElementClickInterceptedException` is a subclass of
`ElementNotInteractableException`.

The framework now uses:

```java
catch (ElementNotInteractableException e)
```

in that location. Catching the parent also catches its
`ElementClickInterceptedException` subclass.

## Parallel Execution and Thread Count

The framework supports parallel TestNG execution. The number of worker threads
can be configured without changing Java code or `testng.xml`.

### Thread-count priority

The framework resolves the thread count in this order:

```text
1. Maven command-line argument: -Dthread.count=X
2. config.properties: thread.count=X
3. Framework fallback: 2
```

A value supplied from Maven has the highest priority.

### Default thread count

`config.properties` contains:

```properties
thread.count=2
```

Therefore, running:

```bash
mvn clean test
```

uses **2 parallel threads** by default.

If `thread.count` is removed from `config.properties` and no Maven argument is
provided, the framework still falls back to **2 threads**.

### Run with a custom number of threads

To override the configured value, pass `thread.count` as a Maven system
property.

Run with 1 thread:

```bash
mvn clean test -Dthread.count=1
```

Run with 3 threads:

```bash
mvn clean test -Dthread.count=3
```

Run with 5 threads:

```bash
mvn clean test -Dthread.count=5
```

Run with 10 threads:

```bash
mvn clean test -Dthread.count=10
```

For example:

```bash
mvn clean test -Dthread.count=5
```

results in:

```text
TestNG parallel thread count: 5
```

### Run with browser and thread count

Framework properties can be combined in the same Maven command.

Chrome with 4 threads:

```bash
mvn clean test -Dbrowser=chrome -Dthread.count=4
```

Firefox with 3 threads:

```bash
mvn clean test -Dbrowser=firefox -Dthread.count=3
```

### How parallel execution works

`testng.xml` defines the parallel execution mode but does not hardcode a thread
count. `TestNgSuiteListener` obtains the value from `ConfigReader` and applies
it to the active TestNG `XmlSuite`.

The effective flow is:

```text
mvn clean test -Dthread.count=4
              |
              v
       ConfigReader
              |
              v
   TestNgSuiteListener
              |
              v
 XmlSuite.setThreadCount(4)
              |
              v
       TestNG workers
              |
              v
 ThreadLocal<WebDriver>
```

`DriverFactory` uses `ThreadLocal<WebDriver>`, so each TestNG worker thread has
its own independent WebDriver instance. Drivers must not be shared between
parallel test threads.

### Invalid values

The thread count must be a positive integer.

Valid examples:

```text
1
2
4
10
```

Invalid examples:

```text
0
-1
abc
```

Invalid values fail with a configuration error instead of silently starting
the suite with an unexpected thread count.

### Recommendation

For local execution, the default of **2 threads** is a safe starting point.

For faster execution, increase the value according to the machine's available
CPU/memory and the number of browsers it can run reliably, for example:

```bash
mvn clean test -Dthread.count=4
```

A higher thread count does not automatically mean faster execution. Every
parallel thread creates an independent browser, so excessive values can consume
significant CPU and memory.

## Test Names in Maven Console

When a test is started directly from IntelliJ IDEA, IntelliJ's TestNG runner
shows a graphical test tree in the **Run** tool window.

For example:

```text
Default Suite
└── automationexercise
    └── TestCase9SearchProduct
        ├── setUp
        ├── testCase9SearchProduct
        └── tearDown
```

When tests are started with Maven:

```bash
mvn clean test
```

the execution uses Maven Surefire rather than IntelliJ's native graphical
TestNG runner. Maven cannot create the same IntelliJ Run-tool-window tree.

To provide the same useful execution information in the Maven console, the
framework includes `MavenConsoleTestListener`.

The Maven output contains the suite name, test class name, test method name,
and final status:

```text
[TESTNG] SUITE START : Automation Exercise Suite

[TESTNG] CLASS START : TestCase9SearchProduct
[TESTNG]   STARTED   : TestCase9SearchProduct.testCase9SearchProduct
[TESTNG]   PASSED    : TestCase9SearchProduct.testCase9SearchProduct
[TESTNG] CLASS END   : TestCase9SearchProduct

[TESTNG] SUITE END   : Automation Exercise Suite
```

With multiple test classes and parallel execution, each test still includes
its class name, making the output easier to follow:

```text
[TESTNG]   STARTED   : TestCase1RegisterUser.testCase1RegisterUser
[TESTNG]   STARTED   : TestCase9SearchProduct.testCase9SearchProduct
[TESTNG]   PASSED    : TestCase9SearchProduct.testCase9SearchProduct
[TESTNG]   PASSED    : TestCase1RegisterUser.testCase1RegisterUser
```

Maven Surefire is configured to keep this output in the console:

```xml
<useFile>false</useFile>
<redirectTestOutputToFile>false</redirectTestOutputToFile>
<printSummary>true</printSummary>
```

The normal Maven/Surefire result summary is also preserved:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

> **Note:** `setUp` and `tearDown` are TestNG configuration methods rather than
> `@Test` methods. IntelliJ displays them as nodes in its graphical runner.
> The Maven listener focuses on suite, class, and actual test method execution,
> while the framework's normal logs continue to show setup/cleanup activity.

## Framework Suite Start and Finish Logs

`TestNgSuiteListener` acts as the framework-level suite lifecycle listener.

It runs once **before all tests** and once **after all tests**. This keeps suite
startup/shutdown behavior out of individual test classes.

When the framework starts, Maven/Jenkins logs clearly show the execution
configuration.

Example:

```text
============================================================
AUTOMATIONEXERCISE FRAMEWORK STARTED
Suite          : Automation Exercise Suite
Environment    : PROD
Browser        : CHROME
Execution Mode : LOCAL
Thread Count   : 4
Base URL       : https://automationexercise.com
============================================================

[FRAMEWORK] STARTED | ENV=PROD | BROWSER=CHROME | MODE=LOCAL | THREADS=4
```

After the complete suite finishes:

```text
============================================================
AUTOMATIONEXERCISE FRAMEWORK FINISHED
Suite   : Automation Exercise Suite
Status  : SUCCESS
Total   : 4
Passed  : 4
Failed  : 0
Skipped : 0
============================================================

[FRAMEWORK] FINISHED | STATUS=SUCCESS | TOTAL=4 | PASSED=4 | FAILED=0 | SKIPPED=0
```

### Local execution

The default configuration is:

```properties
execution.mode=local
```

Example local execution with QA, Chrome, and the default thread count:

```bash
mvn clean test
```

Example local PROD execution with 4 threads:

```bash
mvn clean test \
  -Denvironment=prod \
  -Dbrowser=chrome \
  -Dthread.count=4 \
  -Dexecution.mode=local
```

The startup line will include:

```text
[FRAMEWORK] STARTED | ENV=PROD | BROWSER=CHROME | MODE=LOCAL | THREADS=4
```

### Remote execution label

When tests are executed through Selenium Grid/cloud infrastructure, the
execution mode can be supplied as:

```bash
mvn clean test \
  -Denvironment=prod \
  -Dbrowser=chrome \
  -Dthread.count=4 \
  -Dexecution.mode=remote
```

The startup log becomes:

```text
[FRAMEWORK] STARTED | ENV=PROD | BROWSER=CHROME | MODE=REMOTE | THREADS=4
```

`execution.mode` currently describes the framework execution context in logs.
Remote WebDriver/Grid configuration should only be enabled when the project's
`DriverFactory` is configured with the actual remote hub/cloud endpoint.

### Why a suite listener instead of @BeforeSuite / @AfterSuite?

Suite-level configuration is framework infrastructure rather than test
business logic. Keeping it in `TestNgSuiteListener` provides a clean separation:

```text
TestNgSuiteListener
├── dynamic thread count
├── framework start information
├── environment/browser/mode
└── final suite totals

BaseTest
├── per-test browser setup
└── per-test browser cleanup

Test Classes
└── business test scenarios
```

This also prevents every test class from inheriting unrelated suite-reporting
logic.

## Maven Surefire and Test Reporting

The project uses the **Maven Surefire Plugin** to execute the existing
`testng.xml` suite when running:

```bash
mvn clean test
```

The existing TestNG configuration and listeners remain active.

Surefire is configured to keep test output directly in the Maven console:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.2</version>

    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>

        <useFile>false</useFile>
        <redirectTestOutputToFile>false</redirectTestOutputToFile>
        <printSummary>true</printSummary>
        <trimStackTrace>false</trimStackTrace>
    </configuration>
</plugin>
```

### Maven test output

`MavenConsoleTestListener` prints the actual test class and test method before
and after execution.

Example:

```text
================================================================
[TESTNG] SUITE STARTED : Automation Exercise Suite
================================================================

[TEST CLASS] tests.TestCase9SearchProduct

[STARTED   ] TestCase9SearchProduct.testCase9SearchProduct
[PASSED    ] TestCase9SearchProduct.testCase9SearchProduct

[CLASS END ] TestCase9SearchProduct

[TEST CLASS] tests.TestCase16PlaceOrderLoginBeforeCheckout

[STARTED   ] TestCase16PlaceOrderLoginBeforeCheckout.testCase16PlaceOrderLoginBeforeCheckout
[FAILED    ] TestCase16PlaceOrderLoginBeforeCheckout.testCase16PlaceOrderLoginBeforeCheckout
[ERROR     ] AssertionError: Order success message should be visible.

================================================================
[TESTNG] SUITE FINISHED : Automation Exercise Suite
----------------------------------------------------------------
TOTAL   : 4
PASSED  : 3
FAILED  : 1
SKIPPED : 0
================================================================
```

This makes it possible to identify the failing class and exact test method
without opening the generated report.

Surefire still prints its standard Maven summary afterwards, for example:

```text
Tests run: 4, Failures: 1, Errors: 0, Skipped: 0

BUILD FAILURE
```

When all tests pass:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

### TestNG configuration

`testng.xml` remains the source of truth for:

- suite name,
- parallel execution mode,
- selected test classes,
- TestNG listeners.

The Maven Surefire plugin only launches the existing TestNG suite and controls
how Maven reports its output. It does not replace or remove the TestNG
configuration.

## Maven Surefire Plugin for TestNG

The project uses the **Maven Surefire Plugin** to execute the existing
`testng.xml` suite when running:

```bash
mvn clean test
```

The plugin is configured in `pom.xml` as:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.2</version>

    <configuration>

        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>

        <useFile>false</useFile>
        <redirectTestOutputToFile>false</redirectTestOutputToFile>
        <printSummary>true</printSummary>
        <trimStackTrace>false</trimStackTrace>

        <systemPropertyVariables>
            <browser>${browser}</browser>
            <environment>${environment}</environment>
            <headless>${headless}</headless>
            <thread.count>${thread.count}</thread.count>
            <execution.mode>${execution.mode}</execution.mode>
        </systemPropertyVariables>

    </configuration>
</plugin>
```

### What Surefire does in this framework

Surefire:

1. Starts TestNG from Maven.
2. Uses the existing `testng.xml`.
3. Keeps all configured TestNG listeners active.
4. Passes browser/environment/thread/runtime properties into the TestNG JVM.
5. Keeps test output visible in the Maven console.
6. Prints the normal Maven test summary.
7. Returns a non-zero exit code when tests fail, so CI/Jenkins correctly marks
   the build as failed.

### Run all TestNG tests

```bash
mvn clean test
```

### Run with a different browser

```bash
mvn clean test -Dbrowser=firefox
```

### Run with a custom thread count

```bash
mvn clean test -Dthread.count=4
```

### Run PROD with Chrome and 4 threads

```bash
mvn clean test \
  -Denvironment=prod \
  -Dbrowser=chrome \
  -Dthread.count=4
```

### Console result

The custom TestNG listeners print class/method-level execution such as:

```text
[TEST CLASS] tests.TestCase9SearchProduct
[STARTED   ] TestCase9SearchProduct.testCase9SearchProduct
[PASSED    ] TestCase9SearchProduct.testCase9SearchProduct
```

When a test fails:

```text
[FAILED    ] TestCase16PlaceOrderLoginBeforeCheckout.testCase16PlaceOrderLoginBeforeCheckout
```

At the end the framework prints totals, and Surefire also prints its standard
summary:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

or, when a test fails:

```text
Tests run: 4, Failures: 1, Errors: 0, Skipped: 0
BUILD FAILURE
```

`testng.xml` remains the source of truth for the suite, selected test classes,
parallel mode, and TestNG listeners. Surefire is the Maven execution layer that
launches that TestNG configuration.


## Thread-Safe Logging

The framework uses Log4j2 for console and persistent file logging.

Local logs are stored in the project-level folder:

```text
automationexercise/
└── logs/
    ├── automation.log
    └── archive/
        └── automation-YYYY-MM-DD-N.log.gz
```

The active log file is:

```text
logs/automation.log
```

Unlike `target/`, the `logs/` folder is not deleted by:

```bash
mvn clean
```

### Parallel/thread-safe logging

TestNG can run multiple classes in parallel. All worker threads write to the
same Log4j2 `RollingFile` appender, which is designed for concurrent logging.

Every log line includes both:

- the Java/TestNG worker thread,
- the current test method name.

Example:

```text
2026-08-25 20:45:03.120 INFO  [TestNG-test-UI Tests-1] [test=testCase1RegisterUser] BaseTest - Starting test
2026-08-25 20:45:03.121 INFO  [TestNG-test-UI Tests-2] [test=testCase9SearchProduct] BaseTest - Starting test
2026-08-25 20:45:05.418 INFO  [TestNG-test-UI Tests-1] [test=testCase1RegisterUser] UiActions - Clicking element
```

`BaseTest` stores the current test name in Log4j2 `ThreadContext`:

```java
ThreadContext.put(
        "testName",
        method.getName()
);
```

`ThreadContext` itself is thread-local. Therefore one parallel test cannot
overwrite another test's logging context.

After each test:

```java
ThreadContext.clearAll();
```

is executed in `finally` so TestNG worker-thread reuse cannot leak the previous
test name into the next test.

### Log rotation

`logs/automation.log` is automatically rolled when:

- it reaches **10 MB**, or
- a new day begins.

Rolled files are compressed under:

```text
logs/archive/
```

The rolling strategy keeps up to 10 archived files.

### Jenkins

The Jenkins pipeline archives:

```text
logs/**/*
```

so framework logs remain available as build artifacts after CI execution.

## IntelliJ IDEA: Maven/Surefire vs Native TestNG Runner

### Root cause

The project is correctly configured so that:

```bash
mvn clean test
```

runs TestNG through Maven Surefire using the existing `testng.xml` file.

However, IntelliJ IDEA has **two different execution paths**:

```text
Maven tool window -> Maven -> Surefire -> TestNG -> Maven console

IntelliJ TestNG configuration -> IntelliJ TestNG runner -> interactive Test Runner UI
```

The clickable IntelliJ tree with the currently running test highlighted is
created by IntelliJ's native TestNG runner. Maven/Surefire cannot force the
Maven tool-window execution to become that native runner.

Therefore the project intentionally supports both paths while using the **same
`testng.xml` and same test classes**.

### Maven / CI execution

Use this for CI/CD and for validating the exact Maven build:

```bash
mvn clean test
```

Surefire is configured in `pom.xml` with:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.2</version>

    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>

        <useFile>false</useFile>
        <redirectTestOutputToFile>false</redirectTestOutputToFile>
        <printSummary>true</printSummary>
        <trimStackTrace>false</trimStackTrace>

        <systemPropertyVariables>
            <browser>${browser}</browser>
            <environment>${environment}</environment>
            <headless>${headless}</headless>
            <thread.count>${thread.count}</thread.count>
            <execution.mode>${execution.mode}</execution.mode>
        </systemPropertyVariables>
    </configuration>
</plugin>
```

This keeps `mvn clean test` suitable for Jenkins/CI and produces Surefire
reports under:

```text
target/surefire-reports/
```

The framework's TestNG listeners also print suite/class/method/pass/fail
information to the Maven console.

### IntelliJ interactive TestNG execution

Use the shared run configuration included in the project:

```text
.run/automationexercise - TestNG Suite.run.xml
```

After importing/reloading the Maven project, select:

```text
automationexercise - TestNG Suite
```

from IntelliJ's run-configuration selector and click **Run**.

This launches the same `testng.xml` through IntelliJ's native TestNG runner and
gives you the interactive UI:

```text
Automation Exercise Required Test Cases
└── Required UI Tests
    ├── TestCase1RegisterUserTest
    │   └── testCase1RegisterUser
    ├── TestCase9SearchProductTest
    │   └── testCase9SearchProduct
    ├── TestCase16PlaceOrderLoginBeforeCheckoutTest
    │   └── testCase16PlaceOrderLoginBeforeCheckout
    └── TestCase25ScrollUpTest
        └── testCase25ScrollUpUsingArrow
```

In that Test Runner you can:

- see the suite and test classes,
- see the currently executing method,
- see running/passed/failed state,
- click a method to inspect its output,
- rerun failed tests,
- debug a class or method.

### If IntelliJ does not import the shared run configuration

Create it manually:

1. Open **Run -> Edit Configurations**.
2. Click **+** and choose **TestNG**.
3. Name it `automationexercise - TestNG Suite`.
4. Set **Test kind** to **Suite**.
5. Set **Suite** to `$PROJECT_DIR$/testng.xml` (browse to `testng.xml`).
6. Set **Use classpath of module** to `automationexercise`.
7. Set **Working directory** to `$PROJECT_DIR$`.
8. Set VM options to:

```text
-Dbrowser=chrome -Denvironment=qa -Dthread.count=2 -Dexecution.mode=local -Dheadless=false
```

9. Click **Apply**, then **Run**.

JetBrains calls this a TestNG **Suite** run configuration. It is the execution
mode that provides IntelliJ's native interactive Test Runner.

### Important: do not delegate this TestNG run to Maven

If your goal is the native TestNG tree, do not rely on **Delegate IDE
build/run actions to Maven** for this configuration. Delegating execution to
Maven changes the runner back to Maven/Surefire and you return to Maven-style
output.

### One TestNG dependency

The project intentionally has one TestNG dependency only:

```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>${testng.version}</version>
    <scope>test</scope>
</dependency>
```

A previous version contained a second `org.testng:testng` dependency with a
different version and `compile` scope. It was removed to avoid Maven dependency
warnings and inconsistent TestNG resolution between Maven and IntelliJ.
