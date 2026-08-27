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
        super();
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

        return actions.isDisplayed(heading);
    }

    /** Fills the complete account form from an Account Builder object. */
    public SignupPage fill(Account account) {

        if ("Mrs".equalsIgnoreCase(account.getTitle())) {
            actions.click(mrs);
        } else {
            actions.click(mr);
        }

        actions.type(password, account.getPassword());

        actions.selectByVisibleText(days, account.getDay());
        actions.selectByVisibleText(months, account.getMonth());
        actions.selectByVisibleText(years, account.getYear());

        if (account.isNewsletter() && !actions.isSelected(newsletter)) {
            actions.click(newsletter);
        }

        if (account.isOffers() && !actions.isSelected(offers)) {
            actions.click(offers);
        }

        actions.type(firstName, account.getFirstName());
        actions.type(lastName, account.getLastName());
        actions.type(company, account.getCompany());
        actions.type(address1, account.getAddress1());
        actions.type(address2, account.getAddress2());
        actions.selectByVisibleText(country, account.getCountry());
        actions.type(state, account.getState());
        actions.type(city, account.getCity());
        actions.type(zipCode, account.getZipCode());
        actions.type(mobile, account.getMobileNumber());

        return this;
    }

    /** Submits registration. */
    public AccountCreatedPage createAccount() {

        actions.click(createAccount);
        return new AccountCreatedPage();
    }
}
