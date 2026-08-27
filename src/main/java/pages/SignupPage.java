package pages;

import models.Account;
import models.Gender;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * "ENTER ACCOUNT INFORMATION" registration form.
 */
public class SignupPage extends BasePage {

    @FindBy(xpath = "//b[normalize-space()='Enter Account Information']")
    private WebElement heading;

    @FindBy(id = "id_gender1")
    private WebElement titleMr;

    @FindBy(id = "id_gender2")
    private WebElement titleMrs;

    @FindBy(id = "name")
    private WebElement name;

    @FindBy(id = "email")
    private WebElement email;

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

    /** Whether the account information form is displayed. */
    public boolean isVisible() {
        return actions.isDisplayed(heading);
    }

    /** Name carried over from the signup step, pre-filled by the site. */
    public String getPrefilledName() {
        return actions.getValue(name);
    }

    /** Email carried over from the signup step, pre-filled by the site. */
    public String getPrefilledEmail() {
        return actions.getValue(email);
    }

    /** Whether the newsletter checkbox is ticked. */
    public boolean isNewsletterSelected() {
        return actions.isSelected(newsletter);
    }

    /** Whether the special-offers checkbox is ticked. */
    public boolean isOffersSelected() {
        return actions.isSelected(offers);
    }

    /**
     * Fills the whole registration form.
     *
     * <p>Checkboxes are toggled only when their current state differs from the
     * requested one, so the method is safe to call regardless of the defaults
     * the site ships with.</p>
     */
    public SignupPage fill(Account account) {
        actions.click(account.getGender() == Gender.MRS ? titleMrs : titleMr);

        actions.type(password, account.getPassword());

        actions.selectByVisibleText(days, account.getDay());
        actions.selectByVisibleText(months, account.getMonth());
        actions.selectByVisibleText(years, account.getYear());

        if (account.isNewsletter() != actions.isSelected(newsletter)) {
            actions.click(newsletter);
        }

        if (account.isOffers() != actions.isSelected(offers)) {
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

    /** Submits the registration form. */
    public AccountCreatedPage createAccount() {
        actions.click(createAccount);
        return new AccountCreatedPage();
    }
}
