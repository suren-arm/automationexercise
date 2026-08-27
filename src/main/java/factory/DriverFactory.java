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
    }

    /**
     * Creates the configured browser.
     *
     * @return current thread's WebDriver
     */
    public static WebDriver createDriver() {
        String browser = ConfigReader.browser().trim().toLowerCase();
        boolean headless = ConfigReader.headless();

        WebDriver webDriver = switch (browser) {
            case "chrome" -> createChrome(headless);
            case "firefox", "gecko" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
                            + ". Supported values: chrome, firefox/gecko, edge.");
        };

        DRIVER.set(webDriver);
        webDriver.manage().deleteAllCookies();

        if (!headless) {
            webDriver.manage().window().maximize();
        }

        LOG.info("Created {} driver. Thread={}", browser, Thread.currentThread().getName());
        return webDriver;
    }

    /** Creates ChromeDriver with framework options. */
    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-notifications");
        return new ChromeDriver(options);
    }

    /** Creates FirefoxDriver/GeckoDriver with framework options. */
    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    /** Creates EdgeDriver with framework options. */
    private static WebDriver createEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-notifications");
        return new EdgeDriver(options);
    }

    /**
     * Returns the current thread's driver.
     *
     * @return current WebDriver
     */
    public static WebDriver getDriver() {
        WebDriver webDriver = DRIVER.get();

        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized for the current thread.");
        }

        return webDriver;
    }

    /**
     * Quits and removes the current thread's WebDriver.
     *
     * <p>ThreadLocal.remove() avoids stale references when TestNG worker
     * threads are reused.</p>
     */
    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();

        try {
            if (webDriver != null) {
                webDriver.quit();
                LOG.info("Driver closed. Thread={}", Thread.currentThread().getName());
            }
        } finally {
            DRIVER.remove();
        }
    }
}
