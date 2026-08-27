package utils;

import factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

/**
 * Failure screenshot utility.
 */
public final class ScreenshotUtils {

    /** Utility class. */
    private ScreenshotUtils() {
    }

    /** Captures current browser as PNG bytes. */
    public static byte[] capture() {
        return ((TakesScreenshot) DriverFactory.getDriver())
                .getScreenshotAs(OutputType.BYTES);
    }

    /** Converts screenshot bytes into an Allure-compatible stream. */
    public static ByteArrayInputStream stream() {
        return new ByteArrayInputStream(capture());
    }
}
