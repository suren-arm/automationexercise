package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Account-created confirmation page.
 */
public class AccountCreatedPage extends BasePage {
    /**
     * Creates the AccountCreatedPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public AccountCreatedPage() {
        super();
    }


    /** Account Created confirmation. */
    @FindBy(xpath = "//h2[@data-qa='account-created']//b")
    private WebElement confirmation;

    /** Continue button. */
    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    /** Returns confirmation text. */
    public String getConfirmation() {

        return actions.getText(confirmation);
    }

    /** Continues to authenticated Home. */
    public HomePage continueToHome() {

        actions.click(continueButton);
        return new HomePage();
    }
}
