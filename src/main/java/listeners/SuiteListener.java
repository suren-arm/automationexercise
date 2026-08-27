package listeners;

import config.ConfigReader;
import config.LoggingConfigurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.xml.XmlSuite;

/**
 * Prints the execution context at the start, so a CI log shows which browser
 * and URL a run used, and an aggregated summary at the end.
 */
public class SuiteListener implements ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(SuiteListener.class);
    private static final String SEPARATOR = "=".repeat(60);

    @Override
    public void onStart(ISuite suite) {
        Level logLevel = LoggingConfigurator.apply();
        int threadCount = applyThreadCount(suite);

        LOG.info(SEPARATOR);
        LOG.info("AUTOMATIONEXERCISE SUITE STARTED");
        LOG.info("Suite     : {}", suite.getName());
        LOG.info("Browser   : {}", ConfigReader.browser());
        LOG.info("Headless  : {}", ConfigReader.headless());
        LOG.info("Base URL  : {}", ConfigReader.baseUrl());
        LOG.info("Timeout   : {}s", ConfigReader.explicitWaitSeconds());
        LOG.info("Log level : {}", logLevel);
        LOG.info("Threads   : {} ({})", threadCount, suite.getXmlSuite().getParallel());
        LOG.info(SEPARATOR);
    }

    /**
     * TestNG would normally take this from {@code testng.xml}, making the suite
     * file a second home for run settings. {@code onStart} runs before the suite
     * builds its executor, so setting it here is picked up.
     */
    private int applyThreadCount(ISuite suite) {
        int threadCount = ConfigReader.threadCount();

        XmlSuite xmlSuite = suite.getXmlSuite();
        xmlSuite.setThreadCount(threadCount);

        // Any <test> that declares its own count would otherwise win over the
        // suite-level value.
        xmlSuite.getTests().forEach(test -> test.setThreadCount(threadCount));

        return threadCount;
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (ISuiteResult result : suite.getResults().values()) {
            passed += result.getTestContext().getPassedTests().size();
            failed += result.getTestContext().getFailedTests().size();
            skipped += result.getTestContext().getSkippedTests().size();
        }

        LOG.info(SEPARATOR);
        LOG.info("AUTOMATIONEXERCISE SUITE FINISHED");
        LOG.info("Status   : {}", failed == 0 ? "SUCCESS" : "FAILED");
        LOG.info("Total    : {}", passed + failed + skipped);
        LOG.info("Passed   : {}", passed);
        LOG.info("Failed   : {}", failed);
        LOG.info("Skipped  : {}", skipped);
        LOG.info(SEPARATOR);
    }
}
