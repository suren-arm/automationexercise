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
import java.util.function.Function;

/**
 * The framework's only synchronisation point - no implicit waits, no
 * {@code Thread.sleep} - so the timeout is configured in one place.
 *
 * <p>Stale references are ignored while polling: the site rebuilds product
 * grids and cart rows while a condition is still being evaluated.</p>
 */
public class WaitUtils {

    private static final Logger LOG = LogManager.getLogger(WaitUtils.class);

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
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

    /** For state no built-in {@code ExpectedCondition} covers, such as scroll position. */
    public <T> T until(Function<WebDriver, T> condition) {
        return wait.until(condition);
    }
}
