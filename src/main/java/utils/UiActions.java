package utils;

import features.GoogleVignetteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Central wrapper for all Selenium element interactions.
 *
 * <p>The Google vignette is checked immediately before each operation. This
 * solves the timing problem where the ad appears after a Page Object's
 * initial check but before the next Selenium click.</p>
 */
public class UiActions {

    private static final Logger LOG =
            LogManager.getLogger(UiActions.class);

    private final WebDriver driver;
    private final WaitUtils wait;
    private final Actions seleniumActions;
    private final GoogleVignetteHandler vignette;

    /** Creates the reusable UI action wrapper. */
    public UiActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
        this.seleniumActions = new Actions(driver);
        this.vignette = new GoogleVignetteHandler(driver);
    }

    /**
     * Waits and clicks.
     *
     * <p>If an ad appears after the initial check, the intercepted-click
     * exception triggers forced recovery and one retry.</p>
     */
    public void click(WebElement element) {
        LOG.info("Clicking element");

        dismissInterruptions();

        try {
            wait.clickable(element)
                    .click();

        } catch (ElementClickInterceptedException exception) {
            LOG.warn(
                    "Click intercepted. Recovering from Google vignette."
            );

            vignette.forceCloseBlockingAd();

            wait.clickable(element)
                    .click();
        }
    }

    /** Safe click using a locator. */
    public void click(By locator) {
        LOG.info("Clicking {}", locator);

        dismissInterruptions();

        try {
            wait.clickable(locator)
                    .click();

        } catch (ElementClickInterceptedException exception) {
            LOG.warn(
                    "Locator click intercepted. Recovering from Google vignette."
            );

            vignette.forceCloseBlockingAd();

            wait.clickable(locator)
                    .click();
        }
    }

    /** Waits and clears input. */
    public void clear(WebElement element) {
        dismissInterruptions();
        wait.visible(element)
                .clear();
    }

    /** Waits, clears and types. */
    public void type(
            WebElement element,
            String value) {

        LOG.info("Typing into input");

        dismissInterruptions();

        WebElement target =
                wait.visible(element);

        target.clear();
        target.sendKeys(value);
    }

    /** Appends text without clearing. */
    public void append(
            WebElement element,
            String value) {

        dismissInterruptions();

        wait.visible(element)
                .sendKeys(value);
    }

    /** Returns visible element text. */
    public String getText(WebElement element) {
        dismissInterruptions();

        return wait.visible(element)
                .getText();
    }

    /** Returns whether an element becomes visible. */
    public boolean isDisplayed(WebElement element) {
        dismissInterruptions();

        try {
            return wait.visible(element)
                    .isDisplayed();

        } catch (TimeoutException
                 | NoSuchElementException exception) {

            return false;
        }
    }

    /** Returns selection state. */
    public boolean isSelected(WebElement element) {
        dismissInterruptions();

        return wait.visible(element)
                .isSelected();
    }

    /** Selects dropdown option by visible text. */
    public void selectByVisibleText(
            WebElement element,
            String visibleText) {

        dismissInterruptions();

        new Select(
                wait.visible(element))
                .selectByVisibleText(visibleText);
    }

    /** Hovers over an element. */
    public void hover(WebElement element) {
        dismissInterruptions();

        seleniumActions
                .moveToElement(
                        wait.visible(element))
                .perform();
    }

    /** Scrolls an element into viewport. */
    public void scrollIntoView(WebElement element) {
        dismissInterruptions();

        WebElement target =
                wait.visible(element);

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block:'center'});",
                        target
                );
    }

    /** Scrolls to bottom. */
    public void scrollToBottom() {
        dismissInterruptions();

        ((JavascriptExecutor) driver)
                .executeScript(
                        "window.scrollTo("
                                + "0,"
                                + "document.body.scrollHeight"
                                + ");"
                );
    }

    /** Returns current number of matching elements. */
    public int count(By locator) {
        dismissInterruptions();

        return driver.findElements(locator)
                .size();
    }

    /** Returns text from all matching elements. */
    public List<String> getTexts(By locator) {
        dismissInterruptions();

        return driver.findElements(locator)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    /** Central transient-interruption hook. */
    private void dismissInterruptions() {
        vignette.closeIfPresent();
    }
}
