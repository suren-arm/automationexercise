package utils;

import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * Failure screenshot capture.
 */
public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    /**
     * Empty rather than throwing when there is nothing to capture: a test can
     * fail before the driver exists, and the screenshot attempt must not replace
     * the real failure with a misleading one.
     */
    public static Optional<byte[]> capture() {
        if (!DriverFactory.hasDriver()) {
            LOG.warn("No WebDriver for this thread - skipping failure screenshot.");
            return Optional.empty();
        }

        try {
            WebDriver driver = DriverFactory.getDriver();
            return Optional.of(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (RuntimeException e) {
            LOG.warn("Could not capture failure screenshot: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
