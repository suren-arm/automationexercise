package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/** Central wrapper for browser-level Selenium operations. */
public class BrowserActions {
    private static final Logger LOG = LogManager.getLogger(BrowserActions.class);
    private final WebDriver driver;
    private final WaitUtils wait;

    public BrowserActions(WebDriver driver) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public void open(String url) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String currentUrl() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public void refresh() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public void removeUrlFragment() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
