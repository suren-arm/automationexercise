package utils;

import features.InterruptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * The single element-interaction wrapper used by every page object. Each method
 * adds an explicit wait, a log line, or recovery from the ad overlay the site
 * intermittently shows.
 *
 * <p>Element values are never logged - the same {@code type()} call fills in an
 * address field and a password.</p>
 */
public class UiActions {

    private static final Logger LOG = LogManager.getLogger(UiActions.class);

    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private final InterruptionHandler interruptions;

    public UiActions(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.interruptions = new InterruptionHandler(driver);
    }

    /**
     * Waits for the element to be clickable and performs a native click.
     *
     * <p>An intercepted click is retried only when the interception is
     * confirmed to be one of the site's advertisements covering this element.
     * Any other cause - a modal still open, a disabled control, the wrong page,
     * a bad locator - propagates, because those are defects the suite should
     * report rather than click through.</p>
     */
    public void click(WebElement element) {
        interruptions.dismissIfPresent();

        WebElement target = waitUtils.clickable(element);
        LOG.debug("Clicking {}", describe(target));

        try {
            target.click();
        } catch (ElementClickInterceptedException e) {
            if (!interruptions.recoverFrom(element)) {
                throw e;
            }

            LOG.warn("An advertisement was covering the element - clicking again.");
            waitUtils.clickable(element).click();
        }
    }

    /**
     * Clears the field and types the value.
     *
     * <p>Waits for clickable rather than visible: a painted field is not
     * necessarily ready for input, and {@code clear()} throws if it is not.
     * That wait is the fix - a field that is still not interactable afterwards
     * is a genuine problem and is allowed to fail.</p>
     */
    public void type(WebElement element, String value) {
        interruptions.dismissIfPresent();

        WebElement target = waitUtils.clickable(element);

        LOG.debug("Typing {} character(s) into {}", value.length(), describe(target));

        target.clear();
        target.sendKeys(value);
    }

    public String getText(WebElement element) {
        interruptions.dismissIfPresent();
        return waitUtils.visible(element).getText();
    }

    /**
     * Current value of an input. Reads the DOM property, not the attribute,
     * which only reports what the markup shipped with.
     */
    public String getValue(WebElement element) {
        interruptions.dismissIfPresent();

        String value = waitUtils.visible(element).getDomProperty("value");
        return value == null ? "" : value;
    }

    /**
     * Returns {@code false} on timeout rather than throwing, so assertions fail
     * with their own message instead of a raw {@code TimeoutException}.
     */
    public boolean isDisplayed(WebElement element) {
        interruptions.dismissIfPresent();

        try {
            return waitUtils.visible(element).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDisplayed(By locator) {
        interruptions.dismissIfPresent();

        try {
            return waitUtils.visible(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSelected(WebElement element) {
        interruptions.dismissIfPresent();
        return waitUtils.visible(element).isSelected();
    }

    public void selectByVisibleText(WebElement element, String visibleText) {
        interruptions.dismissIfPresent();
        new Select(waitUtils.visible(element)).selectByVisibleText(visibleText);
    }

    public void scrollIntoView(WebElement element) {
        interruptions.dismissIfPresent();

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                waitUtils.visible(element));
    }

    /**
     * Text of every element matching a locator. Waits for the first match so a
     * slow render is not mistaken for an empty result; a genuinely empty result
     * returns an empty list for the caller to assert on.
     */
    public List<String> getTexts(By locator) {
        interruptions.dismissIfPresent();

        try {
            waitUtils.visible(locator);
        } catch (TimeoutException e) {
            LOG.info("No elements matched {} within the timeout.", locator);
            return List.of();
        }

        return driver.findElements(locator)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    /**
     * Short element description for debug logs. Guarded on DEBUG because
     * {@code log.debug} arguments are evaluated eagerly and this costs a round
     * trip.
     */
    private String describe(WebElement element) {
        if (!LOG.isDebugEnabled()) {
            return "element";
        }

        try {
            String tag = element.getTagName();

            String id = element.getDomAttribute("id");
            if (id != null && !id.isBlank()) {
                return tag + "#" + id;
            }

            String name = element.getDomAttribute("name");
            if (name != null && !name.isBlank()) {
                return tag + "[name=" + name + "]";
            }

            // Links and buttons rarely carry either; the label is enough to
            // follow a flow in the log.
            String text = element.getText();
            if (text != null && !text.isBlank()) {
                String label = text.strip().replaceAll("\\s+", " ");
                return tag + " '" + (label.length() > 40 ? label.substring(0, 40) + "…" : label) + "'";
            }

            return tag;
        } catch (WebDriverException e) {
            return "element";
        }
    }
}
