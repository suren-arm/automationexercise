package listeners;

import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.xml.XmlSuite;

/**
 * Configures TestNG suite-level settings and prints framework lifecycle
 * information before and after the entire automated test suite.
 *
 * <p>This listener is the framework-level equivalent of @BeforeSuite and
 * @AfterSuite, while keeping suite configuration separate from test classes.</p>
 */
public class TestNgSuiteListener
        implements ISuiteListener {

    /** Framework lifecycle logger. */
    private static final Logger LOG =
            LogManager.getLogger(
                    TestNgSuiteListener.class
            );

    /**
     * Runs once before the TestNG suite starts.
     *
     * <p>It resolves the dynamic thread count, applies it to TestNG, and prints
     * the complete execution context so Maven/Jenkins logs immediately show
     * how the framework was started.</p>
     *
     * @param suite active TestNG suite
     */
    @Override
    public void onStart(ISuite suite) {

        int threadCount =
                ConfigReader.getThreadCount();

        XmlSuite xmlSuite =
                suite.getXmlSuite();

        xmlSuite.setThreadCount(
                threadCount
        );

        String environment =
                ConfigReader.environment()
                            .toUpperCase();

        String browser =
                ConfigReader.browser()
                            .toUpperCase();

        String executionMode =
                ConfigReader.getExecutionMode()
                            .toUpperCase();

        String baseUrl =
                ConfigReader.baseUrl();

        LOG.info(
                "============================================================"
        );

        LOG.info(
                "AUTOMATIONEXERCISE FRAMEWORK STARTED"
        );

        LOG.info(
                "Suite          : {}",
                suite.getName()
        );

        LOG.info(
                "Environment    : {}",
                environment
        );

        LOG.info(
                "Browser        : {}",
                browser
        );

        LOG.info(
                "Execution Mode : {}",
                executionMode
        );

        LOG.info(
                "Thread Count   : {}",
                threadCount
        );

        LOG.info(
                "Base URL       : {}",
                baseUrl
        );

        LOG.info(
                "============================================================"
        );

        /*
         * Also print a compact human-readable line directly to stdout.
         * This makes the most important startup information easy to spot
         * inside Maven and Jenkins consoles.
         */
        System.out.printf(
                "%n[FRAMEWORK] STARTED | ENV=%s | BROWSER=%s | MODE=%s | THREADS=%d%n%n",
                environment,
                browser,
                executionMode,
                threadCount
        );
    }

    /**
     * Runs once after every test in the suite has completed.
     *
     * <p>Aggregates TestNG results across all suite contexts and prints a final
     * framework summary.</p>
     *
     * @param suite completed TestNG suite
     */
    @Override
    public void onFinish(ISuite suite) {

        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (ISuiteResult suiteResult
                : suite.getResults()
                       .values()) {

            passed +=
                    suiteResult.getTestContext()
                               .getPassedTests()
                               .size();

            failed +=
                    suiteResult.getTestContext()
                               .getFailedTests()
                               .size();

            skipped +=
                    suiteResult.getTestContext()
                               .getSkippedTests()
                               .size();
        }

        int total =
                passed
                        + failed
                        + skipped;

        String finalStatus =
                failed == 0
                        ? "SUCCESS"
                        : "FAILED";

        LOG.info(
                "============================================================"
        );

        LOG.info(
                "AUTOMATIONEXERCISE FRAMEWORK FINISHED"
        );

        LOG.info(
                "Suite   : {}",
                suite.getName()
        );

        LOG.info(
                "Status  : {}",
                finalStatus
        );

        LOG.info(
                "Total   : {}",
                total
        );

        LOG.info(
                "Passed  : {}",
                passed
        );

        LOG.info(
                "Failed  : {}",
                failed
        );

        LOG.info(
                "Skipped : {}",
                skipped
        );

        LOG.info(
                "============================================================"
        );

        System.out.printf(
                "%n[FRAMEWORK] FINISHED | STATUS=%s | TOTAL=%d | PASSED=%d | FAILED=%d | SKIPPED=%d%n%n",
                finalStatus,
                total,
                passed,
                failed,
                skipped
        );
    }
}
