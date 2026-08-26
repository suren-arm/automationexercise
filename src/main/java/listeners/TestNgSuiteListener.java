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
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
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
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
