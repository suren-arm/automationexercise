package listeners;

import org.testng.IClassListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prints an IntelliJ-like TestNG hierarchy into the Maven console.
 *
 * <p>Maven cannot render IntelliJ's graphical Run tool-window, but this
 * listener prints the same hierarchy and result information as plain text.</p>
 */
public class MavenConsoleTestListener
        implements ISuiteListener,
                   IClassListener,
                   ITestListener {

    private static final String PROJECT_NAME = "automationexercise";

    private static final AtomicInteger PASSED = new AtomicInteger();
    private static final AtomicInteger FAILED = new AtomicInteger();
    private static final AtomicInteger SKIPPED = new AtomicInteger();

    private static String suiteName = "Default Suite";

    @Override
    public void onStart(ISuite suite) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onTestStart(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void onFinish(ISuite suite) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    private String displayName(ITestResult result) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /**
     * Returns the current TestNG worker thread name.
     */
    private String threadName() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    private String safeMessage(String message) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
