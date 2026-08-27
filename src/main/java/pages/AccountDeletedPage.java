package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * "ACCOUNT DELETED!" confirmation page used by the TC1 and TC16 cleanup steps.
 */
public class AccountDeletedPage extends BasePage {

    @FindBy(css = "[data-qa='account-deleted']")
    private WebElement confirmation;

    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    /**
     * Returns the confirmation text in upper case; see
     * {@link AccountCreatedPage#getConfirmation()} for why it is normalised.
     */
    public String getConfirmation() {
        return actions.getText(confirmation).toUpperCase();
    }

    /** Continues back to the home page. */
    public HomePage continueToHome() {
        actions.click(continueButton);
        return new HomePage();
    }
}
