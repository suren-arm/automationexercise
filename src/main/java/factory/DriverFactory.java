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
 * Owns the WebDriver for each test thread. Held in a {@link ThreadLocal} so
 * TestNG can run classes in parallel without sharing a browser.
 */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    /** Creates the configured browser for the current thread. */
    public static WebDriver createDriver() {
        String browser = ConfigReader.browser().trim().toLowerCase();
        boolean headless = ConfigReader.headless();

        WebDriver driver = switch (browser) {
            case "chrome" -> createChrome(headless);
            case "firefox", "gecko" -> createFirefox(headless);
            case "edge" -> createEdge(headless);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
                            + ". Supported values: chrome, firefox/gecko, edge.");
        };

        DRIVER.set(driver);

        // A clean session per test: no cookie may carry a login between tests.
        driver.manage().deleteAllCookies();

        if (!headless) {
            driver.manage().window().maximize();
        }

        LOG.info("Created {} driver (headless={}) on thread {}",
                browser, headless, Thread.currentThread().getName());

        return driver;
    }

    /**
     * Sends the ad networks the site embeds to a dead address, so their scripts
     * never load and cannot cover the page.
     *
     * <p>Cheaper and more reliable than dismissing an overlay after it appears,
     * but it only covers Chromium: Firefox has no equivalent switch, and a new
     * ad host would not be on this list. {@code InterruptionHandler} therefore
     * stays as the safety net.</p>
     */
    private static final String BLOCK_AD_HOSTS = "--host-resolver-rules="
            + "MAP *.doubleclick.net 127.0.0.1,"
            + "MAP *.googlesyndication.com 127.0.0.1,"
            + "MAP *.googleadservices.com 127.0.0.1,"
            + "MAP *.googletagservices.com 127.0.0.1,"
            + "MAP *.adtrafficquality.google 127.0.0.1,"
            + "MAP adservice.google.com 127.0.0.1,"
            + "MAP fundingchoicesmessages.google.com 127.0.0.1";

    private static WebDriver createChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            // A fixed window size keeps headless layout comparable to headed,
            // which matters for scroll-dependent behaviour.
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }

        options.addArguments("--disable-notifications", BLOCK_AD_HOSTS);
        return new ChromeDriver(options);
    }

    /** Firefox has no host-blocking switch, so ads are handled at runtime here. */
    private static WebDriver createFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }

        options.addArguments("--disable-notifications", BLOCK_AD_HOSTS);
        return new EdgeDriver(options);
    }

    /** Whether this thread currently has a driver. */
    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    /** Returns this thread's driver. */
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialised for thread "
                            + Thread.currentThread().getName() + ".");
        }

        return driver;
    }

    /**
     * {@code remove()} runs in a {@code finally} so the slot is cleared even if
     * {@code quit()} fails - TestNG reuses worker threads, and a stale reference
     * would be handed to the next test.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            return;
        }

        try {
            driver.quit();
            LOG.info("Driver closed on thread {}", Thread.currentThread().getName());
        } finally {
            DRIVER.remove();
        }
    }
}
