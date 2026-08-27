package features;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * Recovers from third-party ad overlays that block the application.
 *
 * <p>The site serves Google vignettes in an {@code aswift_*} iframe that can
 * cover the viewport and cause {@code ElementClickInterceptedException}. They
 * appear on a timer rather than in response to the test, so recovery is applied
 * centrally by {@code UiActions} rather than spread through page objects.</p>
 *
 * <p>Detection runs before every action, so it is a single JavaScript call; the
 * expensive dismissal work only runs when that returns true.</p>
 */
public final class InterruptionHandler {

    private static final Logger LOG = LogManager.getLogger(InterruptionHandler.class);

    /** Kept in sync with {@link #REMOVE_BLOCKING_ADS}. */
    private static final String DETECT_BLOCKING_AD = """
            if (window.location.href.indexOf('google_vignette') !== -1) { return true; }
            var frames = document.querySelectorAll(
                "iframe[title='Advertisement'], iframe[id^='aswift_']");
            for (var i = 0; i < frames.length; i++) {
                var r = frames[i].getBoundingClientRect();
                if (r.width >= window.innerWidth * 0.75
                        && r.height >= window.innerHeight * 0.65) { return true; }
            }
            return false;
            """;

    /** Last resort: remove viewport-sized ad containers, leaving small ads alone. */
    private static final String REMOVE_BLOCKING_ADS = """
            var frames = document.querySelectorAll(
                "iframe[title='Advertisement'], iframe[id^='aswift_']");
            var removed = 0;
            for (var i = 0; i < frames.length; i++) {
                var frame = frames[i];
                var r = frame.getBoundingClientRect();
                if (!(r.width >= window.innerWidth * 0.75
                        && r.height >= window.innerHeight * 0.65)) { continue; }
                var node = frame;
                for (var d = 0; d < 4 && node.parentElement; d++) {
                    var pr = node.parentElement.getBoundingClientRect();
                    if (pr.width >= window.innerWidth * 0.75
                            && pr.height >= window.innerHeight * 0.65) {
                        node = node.parentElement;
                    } else { break; }
                }
                node.remove();
                removed++;
            }
            return removed;
            """;

    /**
     * Hides ads overlapping the element about to be clicked.
     *
     * <p>Not every ad is a full-screen vignette: the site also injects banner
     * iframes inline between form fields, far too short to look like an overlay
     * yet still sitting on top of a checkbox. Targeting the intersection leaves
     * unrelated content untouched.</p>
     */
    private static final String HIDE_ADS_OVERLAPPING_TARGET = """
            var target = arguments[0];
            var r = target.getBoundingClientRect();
            var ads = document.querySelectorAll(
                "iframe[id^='aswift_'], iframe[title='Advertisement'],"
                + " iframe[src*='googleads'], iframe[src*='doubleclick'],"
                + " ins.adsbygoogle, div[id^='google_ads']");
            var hidden = 0;
            for (var i = 0; i < ads.length; i++) {
                var ad = ads[i];
                var a = ad.getBoundingClientRect();
                var separate = a.right  < r.left || a.left > r.right
                            || a.bottom < r.top  || a.top  > r.bottom;
                if (separate) { continue; }
                ad.style.setProperty('display', 'none', 'important');
                hidden++;
            }
            return hidden;
            """;

    /** Close controls used by the different vignette variants. */
    private static final List<By> CLOSE_SELECTORS = List.of(
            By.id("dismiss-button"),
            By.cssSelector("[aria-label='Close ad']"),
            By.cssSelector("button[aria-label*='close' i]"),
            By.cssSelector("div[role='button'][aria-label*='close' i]"));

    private final WebDriver driver;

    public InterruptionHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Pre-action check. Never throws: if an overlay really is still blocking,
     * the click that follows reports it more clearly than this could.
     */
    public void dismissIfPresent() {
        try {
            if (!isBlockingAdPresent()) {
                return;
            }

            LOG.info("Advertisement overlay detected - dismissing.");
            dismiss();
        } catch (WebDriverException e) {
            LOG.debug("Advertisement pre-check did not complete: {}", e.getMessage());
            returnToPage();
        }
    }

    /**
     * Recovery after a click was intercepted. Handles both shapes: a
     * full-screen vignette is dismissed, and any banner on top of the target is
     * hidden.
     */
    public void recoverFrom(WebElement target) {
        try {
            if (isBlockingAdPresent() && !dismiss()) {
                Object removed = script(REMOVE_BLOCKING_ADS);
                LOG.warn("No close control found - removed {} blocking overlay(s).", removed);
            }

            Object hidden = script(HIDE_ADS_OVERLAPPING_TARGET, target);

            if (hidden instanceof Number number && number.intValue() > 0) {
                LOG.warn("Hid {} advertisement(s) overlapping the target element.", number);
            }
        } catch (WebDriverException e) {
            LOG.warn("Advertisement recovery failed: {}", e.getMessage());
        } finally {
            returnToPage();
        }
    }

    private boolean isBlockingAdPresent() {
        return Boolean.TRUE.equals(script(DETECT_BLOCKING_AD));
    }

    /** Tries the close controls, then Escape. Reports whether the overlay is gone. */
    private boolean dismiss() {
        boolean clicked = clickCloseInCurrentDocument() || clickCloseInsideAdFrames();

        if (!clicked) {
            // Some variants expose no close element but still honour Escape.
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        }

        returnToPage();
        clearVignetteFragment();

        return !isBlockingAdPresent();
    }

    private boolean clickCloseInCurrentDocument() {
        for (By selector : CLOSE_SELECTORS) {
            for (WebElement candidate : driver.findElements(selector)) {
                try {
                    if (!candidate.isDisplayed()) {
                        continue;
                    }

                    LOG.info("Closing advertisement using {}", selector);

                    try {
                        candidate.click();
                    } catch (ElementNotInteractableException e) {
                        script("arguments[0].click();", candidate);
                    }

                    return true;
                } catch (StaleElementReferenceException e) {
                    // The ad rebuilds its own DOM while being dismissed.
                }
            }
        }

        return false;
    }

    /**
     * Scoped to ad frames rather than recursing through every iframe, which
     * keeps recovery quick and leaves unrelated embedded content alone.
     */
    private boolean clickCloseInsideAdFrames() {
        List<WebElement> adFrames = driver.findElements(
                By.cssSelector("iframe[title='Advertisement'], iframe[id^='aswift_']"));

        for (WebElement frame : adFrames) {
            try {
                driver.switchTo().frame(frame);

                if (clickCloseInCurrentDocument()) {
                    return true;
                }
            } catch (WebDriverException e) {
                // Cross-origin or detached ad frame - try the next one.
            } finally {
                returnToPage();
            }
        }

        return false;
    }

    /** The ad leaves a {@code #google_vignette} fragment behind. */
    private void clearVignetteFragment() {
        script("if (window.location.hash.indexOf('google_vignette') !== -1) {"
                + " history.replaceState(null, document.title,"
                + " window.location.pathname + window.location.search); }");
    }

    private void returnToPage() {
        try {
            driver.switchTo().defaultContent();
        } catch (WebDriverException e) {
            LOG.debug("Could not switch back to the main document: {}", e.getMessage());
        }
    }

    private Object script(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
}
