package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Page Object for the assignment base URL /test_cases. */
public class TestCasesPage extends BasePage {
    /**
     * Creates the TestCasesPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public TestCasesPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    @FindBy(xpath="//h2[contains(normalize-space(),'Test Cases')]") private WebElement testCasesHeading;
    public TestCasesPage open(String url) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public boolean isVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
