package listeners;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

import java.io.ByteArrayInputStream;

/**
 * Per-test result logging and failure screenshots.
 *
 * <p>This is the only place a screenshot is captured. Neither BaseTest nor the
 * tests themselves take one, so a failure produces exactly one attachment.</p>
 */
public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("START  : {}", name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASSED : {}", name(result));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIPPED: {}", name(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("FAILED : {}", name(result), result.getThrowable());

        ScreenshotUtils.capture().ifPresent(png ->
                Allure.addAttachment(
                        "Failure screenshot - " + name(result),
                        "image/png",
                        new ByteArrayInputStream(png),
                        ".png"));
    }

    private String name(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
    }
}
