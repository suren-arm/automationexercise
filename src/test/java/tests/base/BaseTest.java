package tests.base;

import config.ConfigReader;
import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import pages.PageManager;

/**
 * Common setup/cleanup for exactly the four requested UI test cases.
 */
public abstract class BaseTest {

    /** Per-test-class logger. */
    protected final Logger log = LogManager.getLogger(getClass());

    /** Page Object manager. */
    protected PageManager pages;

    /**
     * Opens the Home page before every test from the configured root base_url.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {

        /*
         * ThreadContext is thread-local.
         * Each parallel TestNG worker therefore gets its own current test name,
         * even though every thread writes safely to the same log file.
         */
        ThreadContext.put(
                "testName",
                method.getName()
        );
        DriverFactory.createDriver();
        pages = new PageManager();

        log.info("Opening Home page from base_url: {}", ConfigReader.baseUrl());

        pages.home().open(ConfigReader.baseUrl());

        Assert.assertTrue(
                pages.home().isVisible(),
                "Home page should be visible.");
    }

    /** Always closes the browser after each test. */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        try {
            DriverFactory.quitDriver();
        } finally {

            /*
             * Always clear thread-local logging context because TestNG can
             * reuse worker threads for later test methods.
             */
            ThreadContext.clearAll();
        }
    }
}
