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
        PASSED.set(0);
        FAILED.set(0);
        SKIPPED.set(0);

        suiteName = suite.getName();

        System.out.println();
        System.out.println("============================================================");
        System.out.println("[TESTNG TREE]");
        System.out.println(suiteName);
        System.out.println("└── " + PROJECT_NAME);
        System.out.println("============================================================");
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        String className = testClass.getRealClass().getSimpleName();

        System.out.println();
        System.out.println("    └── " + className);
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        String className = testClass.getRealClass().getSimpleName();

        System.out.println("        [CLASS FINISHED] " + className);
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(
                "        └── "
                        + result.getMethod().getMethodName()
                        + "  [RUNNING] [thread=" + threadName() + "]"
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        PASSED.incrementAndGet();

        System.out.println(
                "            ✓ PASSED  "
                        + displayName(result)
                        + " [thread=" + threadName() + "]"
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        FAILED.incrementAndGet();

        System.out.println(
                "            ✗ FAILED  "
                        + displayName(result)
                        + " [thread=" + threadName() + "]"
        );

        if (result.getThrowable() != null) {
            System.out.println(
                    "              Reason: "
                            + result.getThrowable().getClass().getSimpleName()
                            + " - "
                            + safeMessage(result.getThrowable().getMessage())
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        SKIPPED.incrementAndGet();

        System.out.println(
                "            ! SKIPPED "
                        + displayName(result)
                        + " [thread=" + threadName() + "]"
        );
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed = PASSED.get();
        int failed = FAILED.get();
        int skipped = SKIPPED.get();
        int total = passed + failed + skipped;

        System.out.println();
        System.out.println("============================================================");
        System.out.println("[TESTNG SUMMARY]");
        System.out.println("Suite   : " + suiteName);
        System.out.println("Project : " + PROJECT_NAME);
        System.out.println("Total   : " + total);
        System.out.println("Passed  : " + passed);
        System.out.println("Failed  : " + failed);
        System.out.println("Skipped : " + skipped);
        System.out.println("Status  : " + (failed == 0 ? "SUCCESS" : "FAILED"));
        System.out.println("============================================================");
        System.out.println();
    }

    private String displayName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "."
                + result.getMethod().getMethodName();
    }


    /**
     * Returns the current TestNG worker thread name.
     */
    private String threadName() {
        return Thread.currentThread()
                     .getName();
    }

    private String safeMessage(String message) {
        if (message == null || message.isBlank()) {
            return "No failure message";
        }

        return message.replace('\n', ' ').replace('\r', ' ');
    }
}
