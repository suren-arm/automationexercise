package listeners;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

/**
 * TestNG listener for result logging and failure screenshots.
 */
public class TestListener implements ITestListener {

    /** Listener logger. */
    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    /** Logs passed tests. */
    @Override
    public void onTestSuccess(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Logs skipped tests. */
    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Logs failure and attaches screenshot to Allure. */
    @Override
    public void onTestFailure(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
