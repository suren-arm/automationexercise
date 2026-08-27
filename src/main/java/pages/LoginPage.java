package pages;

import models.Account;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Combined Signup / Login page.
 */
public class LoginPage extends BasePage {

    @FindBy(xpath = "//h2[normalize-space()='New User Signup!']")
    private WebElement signupHeading;

    @FindBy(css = "input[data-qa='signup-name']")
    private WebElement signupName;

    @FindBy(css = "input[data-qa='signup-email']")
    private WebElement signupEmail;

    @FindBy(css = "button[data-qa='signup-button']")
    private WebElement signupButton;

    @FindBy(xpath = "//h2[normalize-space()='Login to your account']")
    private WebElement loginHeading;

    @FindBy(css = "input[data-qa='login-email']")
    private WebElement loginEmail;

    @FindBy(css = "input[data-qa='login-password']")
    private WebElement loginPassword;

    @FindBy(css = "button[data-qa='login-button']")
    private WebElement loginButton;

    /** Whether the "New User Signup!" section is displayed. */
    public boolean isSignupVisible() {
        return actions.isDisplayed(signupHeading);
    }

    /** Whether the "Login to your account" section is displayed. */
    public boolean isLoginVisible() {
        return actions.isDisplayed(loginHeading);
    }

    /** Enters the name and email that start registration. */
    public LoginPage enterSignup(Account account) {
        actions.type(signupName, account.getName());
        actions.type(signupEmail, account.getEmail());
        return this;
    }

    /** Continues to the full account form. */
    public SignupPage clickSignup() {
        actions.click(signupButton);
        return new SignupPage();
    }

    /** Enters existing credentials. */
    public LoginPage enterLogin(Account account) {
        actions.type(loginEmail, account.getEmail());
        actions.type(loginPassword, account.getPassword());
        return this;
    }

    /** Submits the login form. */
    public HomePage clickLogin() {
        actions.click(loginButton);
        return new HomePage();
    }
}
