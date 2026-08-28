package utils;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.BooleanSupplier;

/**
 * The framework's only synchronisation point - no implicit waits, no
 * {@code Thread.sleep} - so the timeout is configured in one place.
 *
 * <p>Stale references are ignored while polling: the site rebuilds product
 * grids and cart rows while a condition is still being evaluated.</p>
 */
public class WaitUtils {

    private static final Logger LOG = LogManager.getLogger(WaitUtils.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.explicitWaitSeconds()));

        this.wait.ignoring(StaleElementReferenceException.class);
    }

    public WebElement visible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Advisory wait for the document to finish parsing.
     *
     * <p>{@code driver.get()} already blocks until load, but the site's ads then
     * start navigations of their own that push {@code readyState} back to
     * {@code loading}. Failing on that would fail a test for something unrelated
     * to the application, and every interaction that follows waits for its own
     * element anyway.</p>
     */
    public boolean pageLoaded() {
        try {
            wait.until(d -> {
                Object state = ((JavascriptExecutor) d)
                        .executeScript("return document.readyState");

                return "complete".equals(state) || "interactive".equals(state);
            });

            return true;
        } catch (TimeoutException e) {
            LOG.warn("document.readyState did not settle within the timeout; continuing, "
                    + "because each interaction waits for its own element.");
            return false;
        }
    }

    /**
     * Waits for a condition that needs no driver reference, such as scroll
     * position.
     *
     * <p>Selenium's wait takes a {@code Function<WebDriver, ?>}. Absorbing that
     * shape here means callers write {@code () -> scrollY() > 0} rather than
     * declaring a driver parameter they never use.</p>
     */
    public void untilTrue(BooleanSupplier condition) {
        wait.until(ignored -> condition.getAsBoolean());
    }

    /**
     * Same, on a shorter budget, reporting the outcome instead of throwing.
     *
     * <p>For conditions that are expected to settle far faster than the global
     * timeout, where waiting it out would only delay a retry or a failure.</p>
     *
     * @return whether the condition became true before the timeout
     */
    public boolean untilTrue(BooleanSupplier condition, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ignored -> condition.getAsBoolean());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
