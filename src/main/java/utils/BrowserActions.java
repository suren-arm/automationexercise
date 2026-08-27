package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Browser-level operations: navigation and window scrolling. Element
 * interaction lives in {@link UiActions}.
 */
public class BrowserActions {

    private static final Logger LOG = LogManager.getLogger(BrowserActions.class);

    /** A scroll animation can settle a pixel or two short of zero. */
    private static final long TOP_TOLERANCE_PX = 5;

    private static final Duration SCROLL_SETTLE_TIMEOUT = Duration.ofSeconds(5);

    private final WebDriver driver;
    private final WaitUtils wait;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    public void open(String url) {
        LOG.info("Opening URL: {}", url);
        driver.get(url);
        wait.pageLoaded();
    }

    /**
     * Scrolls to the bottom, then fires a {@code scroll} event.
     *
     * <p>The site reveals its scroll-up arrow from a jQuery handler bound to
     * that event, and a programmatic scroll does not reliably produce one - in a
     * headless or background window it can be throttled away, leaving the arrow
     * hidden. Dispatching it is a no-op when the browser already fired one.</p>
     */
    public void scrollToBottom() {
        LOG.info("Scrolling to page bottom");

        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight);"
                        + "window.dispatchEvent(new Event('scroll'));");

        wait.until(d -> scrollY() > 0);
    }

    /**
     * Makes jQuery animations apply their final value immediately.
     *
     * <p>The control's own handler still runs and still does the scrolling;
     * only the tweening is skipped, which removes a source of intermittent
     * failure when the browser is not painting. No-op without jQuery.</p>
     *
     * @return whether the flag was applied
     */
    public boolean disableAnimations() {
        Object applied = ((JavascriptExecutor) driver).executeScript(
                "if (window.jQuery && window.jQuery.fx) {"
                        + " window.jQuery.fx.off = true; return true; }"
                        + " return false;");

        boolean disabled = Boolean.TRUE.equals(applied);

        if (!disabled) {
            LOG.debug("jQuery was not present on this document; animations left as they are.");
        }

        return disabled;
    }

    /** Current vertical scroll offset in pixels. */
    public long scrollY() {
        Object value = ((JavascriptExecutor) driver)
                .executeScript("return Math.round(window.pageYOffset);");

        return value instanceof Number number ? number.longValue() : 0L;
    }

    /**
     * Polls until the page settles at the top. Uses a short budget rather than
     * the global timeout: with animations off the scroll is near-instant, so
     * anything slower has stalled and the caller should retry.
     */
    public boolean waitUntilScrolledToTop() {
        try {
            new WebDriverWait(driver, SCROLL_SETTLE_TIMEOUT)
                    .until(d -> scrollY() <= TOP_TOLERANCE_PX);
            return true;
        } catch (TimeoutException e) {
            LOG.warn("Page has not scrolled back to the top. Current offset: {}px", scrollY());
            return false;
        }
    }
}
