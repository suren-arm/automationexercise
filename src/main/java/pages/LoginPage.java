package pages;

import models.Account;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login and initial signup Page Object.
 *
 * <p>All account data is supplied as an Account object rather than separate
 * strings.</p>
 */
public class LoginPage extends BasePage {
    /**
     * Creates the LoginPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public LoginPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** New User Signup heading. */
    @FindBy(xpath = "//h2[normalize-space()='New User Signup!']")
    private WebElement signupHeading;

    /** Signup name. */
    @FindBy(css = "input[data-qa='signup-name']")
    private WebElement signupName;

    /** Signup email. */
    @FindBy(css = "input[data-qa='signup-email']")
    private WebElement signupEmail;

    /** Signup button. */
    @FindBy(css = "button[data-qa='signup-button']")
    private WebElement signupButton;

    /** Login heading. */
    @FindBy(xpath = "//h2[normalize-space()='Login to your account']")
    private WebElement loginHeading;

    /** Login email. */
    @FindBy(css = "input[data-qa='login-email']")
    private WebElement loginEmail;

    /** Login password. */
    @FindBy(css = "input[data-qa='login-password']")
    private WebElement loginPassword;

    /** Login button. */
    @FindBy(css = "button[data-qa='login-button']")
    private WebElement loginButton;

    /** Returns whether signup section is visible. */
    public boolean isSignupVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Returns whether login section is visible. */
    public boolean isLoginVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Enters name/email for signup from Account. */
    public LoginPage enterSignup(Account account) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Continues signup to the full account form. */
    public SignupPage clickSignup() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Enters existing account login credentials. */
    public LoginPage enterLogin(Account account) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Submits login. */
    public HomePage clickLogin() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
