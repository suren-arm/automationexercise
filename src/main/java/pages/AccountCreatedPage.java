package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * "ACCOUNT CREATED!" confirmation page.
 */
public class AccountCreatedPage extends BasePage {

    @FindBy(css = "[data-qa='account-created']")
    private WebElement confirmation;

    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    /**
     * The markup says "Account Created!" and CSS uppercases it. Normalising
     * here lets tests assert the official wording without depending on whether
     * a driver reports rendered or raw text.
     */
    public String getConfirmation() {
        return actions.getText(confirmation).toUpperCase();
    }

    /** Continues to the home page as an authenticated user. */
    public HomePage continueToHome() {
        actions.click(continueButton);
        return new HomePage();
    }
}
