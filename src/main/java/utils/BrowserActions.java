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

    public BrowserActions(WebDriver driver) { this.driver=driver; this.wait=new WaitUtils(driver); }
    public void open(String url) { LOG.info("Opening URL: {}", url); driver.get(url); wait.pageLoaded(driver); }
    public String currentUrl() { return driver.getCurrentUrl(); }
    public void refresh() { driver.navigate().refresh(); wait.pageLoaded(driver); }
    public void removeUrlFragment() { ((JavascriptExecutor)driver).executeScript("history.replaceState(null, document.title, window.location.pathname + window.location.search);"); }
}
