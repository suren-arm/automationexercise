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
     * Opens the assignment base_url before every test:
     * https://automationexercise.com/test_cases
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Always closes the browser after each test. */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
