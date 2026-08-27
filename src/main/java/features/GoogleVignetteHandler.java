package features;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Handles intermittent Google vignette/interstitial advertisements.
 *
 * <p>Automation Exercise may display a full-screen Google advertisement in
 * an iframe such as {@code aswift_3}. That iframe can physically cover the
 * application and cause {@link ElementClickInterceptedException}.</p>
 *
 * <p>This class is a framework feature, not a business Page Object. It owns
 * the low-level Selenium needed to recover from third-party advertisements.</p>
 */
public final class GoogleVignetteHandler {

    private static final Logger LOG =
            LogManager.getLogger(GoogleVignetteHandler.class);

    private final WebDriver driver;

    /**
     * Close controls used by Google vignette variants.
     * The exact "Close" locator matches the overlay visible in the screenshot.
     */
    private static final List<By> CLOSE_SELECTORS = List.of(
            By.id("dismiss-button"),
            By.cssSelector("[aria-label='Close ad']"),
            By.cssSelector("[aria-label='Close']"),
            By.cssSelector("[aria-label*='Close']"),
            By.cssSelector("button[aria-label*='close' i]"),
            By.cssSelector("div[role='button'][aria-label*='close' i]"),
            By.xpath("//*[normalize-space()='Close' and "
                    + "(self::button or self::div or self::span or self::a)]")
    );

    /** Google advertising frames that may become full-screen overlays. */
    private static final By GOOGLE_AD_IFRAMES =
            By.cssSelector(
                    "iframe[title='Advertisement'], iframe[id^='aswift_']"
            );

    /** Creates the ad handler for the current browser. */
    public GoogleVignetteHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Fast optional check performed before normal UI actions.
     */
    public void closeIfPresent() {
        try {
            switchToDefaultContent();

            if (!isVignetteLikelyPresent()) {
                return;
            }

            LOG.info("Google vignette detected.");

            if (clickCloseInCurrentDocument()
                    || clickCloseInsideFrames(0, 4)) {

                switchToDefaultContent();
                waitUntilBlockingFrameDisappears();
                cleanupUrlFragment();
                return;
            }

            /*
             * Some variants respond to Escape even when no normal close
             * element is exposed to WebDriver.
             */
            new org.openqa.selenium.interactions.Actions(driver)
                    .sendKeys(Keys.ESCAPE)
                    .perform();

            waitUntilBlockingFrameDisappears();
            cleanupUrlFragment();

        } catch (Exception exception) {
            LOG.debug(
                    "Optional vignette check could not complete.",
                    exception
            );
            switchToDefaultContent();
        }
    }

    /**
     * Strong recovery invoked specifically after an intercepted click.
     */
    public void forceCloseBlockingAd() {
        try {
            LOG.warn(
                    "Click intercepted by overlay. "
                            + "Starting forced Google ad recovery."
            );

            switchToDefaultContent();

            if (clickCloseInCurrentDocument()
                    || clickCloseInsideFrames(0, 4)) {

                switchToDefaultContent();
                waitUntilBlockingFrameDisappears();
                cleanupUrlFragment();
                return;
            }

            /*
             * Last-resort recovery:
             * remove only Google advertisement containers that cover most of
             * the viewport. Small inline ads remain untouched.
             */
            removeViewportBlockingGoogleAd();

            switchToDefaultContent();
            cleanupUrlFragment();

        } catch (Exception exception) {
            LOG.warn(
                    "Forced Google ad recovery was not completely successful.",
                    exception
            );
        } finally {
            switchToDefaultContent();
        }
    }

    /** Detects vignette by URL, close element, or full-screen ad iframe. */
    private boolean isVignetteLikelyPresent() {
        String currentUrl = safeCurrentUrl();

        if (currentUrl.contains("google_vignette")) {
            return true;
        }

        for (By selector : CLOSE_SELECTORS) {
            if (!driver.findElements(selector).isEmpty()) {
                return true;
            }
        }

        for (WebElement frame : driver.findElements(GOOGLE_AD_IFRAMES)) {
            if (isViewportBlocking(frame)) {
                return true;
            }
        }

        return false;
    }

    /** Clicks a known close control in the currently selected document. */
    private boolean clickCloseInCurrentDocument() {
        for (By selector : CLOSE_SELECTORS) {
            List<WebElement> candidates =
                    driver.findElements(selector);

            for (WebElement candidate : candidates) {
                try {
                    if (!candidate.isDisplayed()) {
                        continue;
                    }

                    LOG.info(
                            "Closing vignette using selector: {}",
                            selector
                    );

                    try {
                        candidate.click();

                    } catch (ElementNotInteractableException exception) {

                        ((JavascriptExecutor) driver)
                                .executeScript(
                                        "arguments[0].click();",
                                        candidate
                                );
                    }

                    return true;

                } catch (StaleElementReferenceException ignored) {
                    // The ad DOM can rebuild while being dismissed.
                }
            }
        }

        return false;
    }

    /** Recursively searches nested iframes for a close control. */
    private boolean clickCloseInsideFrames(
            int depth,
            int maxDepth) {

        if (depth >= maxDepth) {
            return false;
        }

        List<WebElement> frames =
                driver.findElements(By.tagName("iframe"));

        for (int index = 0; index < frames.size(); index++) {
            try {
                List<WebElement> refreshedFrames =
                        driver.findElements(By.tagName("iframe"));

                if (index >= refreshedFrames.size()) {
                    break;
                }

                driver.switchTo()
                        .frame(refreshedFrames.get(index));

                if (clickCloseInCurrentDocument()) {
                    return true;
                }

                if (clickCloseInsideFrames(
                        depth + 1,
                        maxDepth)) {
                    return true;
                }

            } catch (NoSuchFrameException
                     | StaleElementReferenceException ignored) {

            } finally {
                switchToDefaultContent();
            }
        }

        return false;
    }

    /** Returns true if an ad frame covers most of the browser viewport. */
    private boolean isViewportBlocking(WebElement frame) {
        try {
            if (!frame.isDisplayed()) {
                return false;
            }

            Rectangle frameRect =
                    frame.getRect();

            Dimension viewport =
                    driver.manage()
                            .window()
                            .getSize();

            return frameRect.getWidth()
                    >= viewport.getWidth() * 0.75
                    && frameRect.getHeight()
                    >= viewport.getHeight() * 0.65;

        } catch (StaleElementReferenceException exception) {
            return false;
        }
    }

    /**
     * Removes only viewport-sized Google advertising overlays.
     *
     * <p>This is a fallback for third-party advertisement markup when no
     * clickable close element is exposed.</p>
     */
    private void removeViewportBlockingGoogleAd() {
        LOG.warn(
                "No close control found. "
                        + "Removing viewport-sized Google ad overlay."
        );

        String script = """
                const frames = Array.from(
                    document.querySelectorAll(
                        "iframe[title='Advertisement'], iframe[id^='aswift_']"
                    )
                );

                let removed = 0;

                for (const frame of frames) {
                    const r = frame.getBoundingClientRect();

                    const blocksViewport =
                        r.width >= window.innerWidth * 0.75 &&
                        r.height >= window.innerHeight * 0.65;

                    if (!blocksViewport) {
                        continue;
                    }

                    let node = frame;

                    for (let i = 0; i < 4 && node.parentElement; i++) {
                        const parent = node.parentElement;
                        const pr = parent.getBoundingClientRect();

                        if (pr.width >= window.innerWidth * 0.75 &&
                            pr.height >= window.innerHeight * 0.65) {
                            node = parent;
                        } else {
                            break;
                        }
                    }

                    node.remove();
                    removed++;
                }

                return removed;
                """;

        Object removed =
                ((JavascriptExecutor) driver)
                        .executeScript(script);

        LOG.info(
                "Removed {} blocking advertisement overlay(s).",
                removed
        );
    }

    /** Waits briefly until no viewport-sized advertisement iframe remains. */
    private void waitUntilBlockingFrameDisappears() {
        try {
            new WebDriverWait(
                    driver,
                    Duration.ofSeconds(3))
                    .until(d -> {

                        for (WebElement frame :
                                d.findElements(GOOGLE_AD_IFRAMES)) {

                            if (isViewportBlocking(frame)) {
                                return false;
                            }
                        }

                        return true;
                    });

        } catch (TimeoutException ignored) {
            /*
             * UiActions.click() performs another recovery if interception
             * still occurs, so this optional wait must not fail a test.
             */
        }
    }

    /** Removes the #google_vignette URL fragment after dismissal. */
    private void cleanupUrlFragment() {
        String url = safeCurrentUrl();

        if (url.contains("#google_vignette")) {
            ((JavascriptExecutor) driver)
                    .executeScript(
                            "history.replaceState("
                                    + "null,"
                                    + "document.title,"
                                    + "window.location.pathname + "
                                    + "window.location.search"
                                    + ");"
                    );
        }
    }

    /** Reads current URL safely. */
    private String safeCurrentUrl() {
        try {
            String url = driver.getCurrentUrl();
            return url == null ? "" : url;

        } catch (WebDriverException exception) {
            return "";
        }
    }

    /** Always returns Selenium to the application top-level document. */
    private void switchToDefaultContent() {
        try {
            driver.switchTo()
                    .defaultContent();

        } catch (WebDriverException ignored) {
        }
    }
}
