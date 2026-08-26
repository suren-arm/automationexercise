package pages;

import models.Account;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Full account-registration form used by TC1 and as TC16 account precondition.
 */
public class SignupPage extends BasePage {
    /**
     * Creates the SignupPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public SignupPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** Account Information heading. */
    @FindBy(xpath = "//b[normalize-space()='Enter Account Information']")
    private WebElement heading;

    @FindBy(id = "id_gender1")
    private WebElement mr;

    @FindBy(id = "id_gender2")
    private WebElement mrs;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "days")
    private WebElement days;

    @FindBy(id = "months")
    private WebElement months;

    @FindBy(id = "years")
    private WebElement years;

    @FindBy(id = "newsletter")
    private WebElement newsletter;

    @FindBy(id = "optin")
    private WebElement offers;

    @FindBy(id = "first_name")
    private WebElement firstName;

    @FindBy(id = "last_name")
    private WebElement lastName;

    @FindBy(id = "company")
    private WebElement company;

    @FindBy(id = "address1")
    private WebElement address1;

    @FindBy(id = "address2")
    private WebElement address2;

    @FindBy(id = "country")
    private WebElement country;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "zipcode")
    private WebElement zipCode;

    @FindBy(id = "mobile_number")
    private WebElement mobile;

    @FindBy(css = "button[data-qa='create-account']")
    private WebElement createAccount;

    /** Returns whether account-information form is visible. */
    public boolean isVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Fills the complete account form from an Account Builder object. */
    public SignupPage fill(Account account) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Submits registration. */
    public AccountCreatedPage createAccount() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
