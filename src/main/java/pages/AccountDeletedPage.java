package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Account-deleted confirmation page used by TC1 and TC16 cleanup.
 */
public class AccountDeletedPage extends BasePage {
    /**
     * Creates the AccountDeletedPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public AccountDeletedPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** Account Deleted confirmation. */
    @FindBy(xpath = "//h2[@data-qa='account-deleted']//b")
    private WebElement confirmation;

    /** Continue button. */
    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    /** Returns confirmation text. */
    public String getConfirmation() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Continues back to Home. */
    public HomePage continueToHome() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
