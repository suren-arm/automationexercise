package tests.base;

import config.ConfigReader;
import config.LoggingConfigurator;
import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;
import pages.PageManager;

import java.lang.reflect.Method;

/**
 * Shared lifecycle for the four scenarios. Each test gets its own browser, so
 * tests are independent of each other and of execution order.
 *
 * <p>Setup covers steps 1-2 of every official case (launch, navigate) but not
 * step 3 - verifying the home page is an official assertion, so it stays in the
 * tests where it is traceable.</p>
 */
public abstract class BaseTest {

    /*
     * Also applied by SuiteListener; needed here because -Dtest=... bypasses
     * testng.xml and its listeners. Idempotent.
     */
    static {
        LoggingConfigurator.apply();
    }

    protected final Logger log = LogManager.getLogger(getClass());

    protected PageManager pages;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        // Stamps the test name onto every line this thread logs, including
        // lines from shared components, and routes them to a per-test file.
        ThreadContext.put("testName", method.getName());

        log.debug("Starting {} on thread {}",
                method.getName(), Thread.currentThread().getName());

        DriverFactory.createDriver();
        pages = new PageManager();

        log.info("Opening {}", ConfigReader.baseUrl());
        pages.home().open(ConfigReader.baseUrl());
    }

    /**
     * Quits the browser. Failures are logged rather than thrown, so a teardown
     * problem cannot mask the real assertion failure in the report.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            DriverFactory.quitDriver();
        } catch (RuntimeException e) {
            log.warn("Browser cleanup failed.", e);
        } finally {
            // TestNG reuses worker threads, so a name left here would be
            // stamped onto the next test.
            ThreadContext.clearAll();
        }
    }

    protected HomePage homePage() {
        return pages.home();
    }
}
