package utils;

import config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Central explicit-wait implementation.
 *
 * <p>All UI interactions should wait through this class. Tests and Page
 * Objects do not use Thread.sleep().</p>
 */
public class WaitUtils {

    /** Selenium explicit wait. */
    private final WebDriverWait wait;

    /** Creates wait using configured timeout. */
    public WaitUtils(WebDriver driver) {
        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(ConfigReader.timeout()));

        wait.ignoring(StaleElementReferenceException.class);
    }

    /** Waits for WebElement visibility. */
    public WebElement visible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Waits for locator visibility. */
    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits until WebElement is clickable. */
    public WebElement clickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Waits until locator is clickable. */
    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Waits until a locator exists in DOM. */
    public WebElement present(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Waits until URL contains expected text. */
    public boolean urlContains(String value) {
        return wait.until(ExpectedConditions.urlContains(value));
    }

    /** Waits until an element contains expected text. */
    public boolean textPresent(WebElement element, String expected) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, expected));
    }

    /** Waits until document.readyState is complete. */
    public boolean pageLoaded(WebDriver driver) {
        return wait.until(d ->
                "complete".equals(
                        ((JavascriptExecutor) d)
                                .executeScript("return document.readyState")));
    }
}
