package factory;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Thread-safe WebDriver factory.
 *
 * <p>No test class creates WebDriver directly. The factory owns browser
 * initialization, browser selection, configuration, access and cleanup.</p>
 */
public final class DriverFactory {

    /** Framework logger. */
    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    /** One WebDriver instance per TestNG worker thread. */
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    /** Static utility class. */
    private DriverFactory() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Creates the configured browser.
     *
     * @return current thread's WebDriver
     */
    public static WebDriver createDriver() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Creates ChromeDriver with framework options. */
    private static WebDriver createChrome(boolean headless) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Creates FirefoxDriver/GeckoDriver with framework options. */
    private static WebDriver createFirefox(boolean headless) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Creates EdgeDriver with framework options. */
    private static WebDriver createEdge(boolean headless) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Returns the current thread's driver.
     *
     * @return current WebDriver
     */
    public static WebDriver getDriver() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Quits and removes the current thread's WebDriver.
     *
     * <p>ThreadLocal.remove() avoids stale references when TestNG worker
     * threads are reused.</p>
     */
    public static void quitDriver() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
