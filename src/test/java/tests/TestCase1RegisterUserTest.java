package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.Account;
import org.testng.annotations.Test;
import pages.HomePage;
import tests.base.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("Automation Exercise")
@Feature("Test Case 1")
public class TestCase1RegisterUserTest extends BaseTest {

    /**
     * Registers a new user, confirms the account is created and signed in, then
     * deletes it again so the scenario can be repeated.
     *
     * <p>The browser is launched and the site opened by {@code BaseTest}.</p>
     */
    @Test(description = "Test Case 1: Register User")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void registerUser() {
        Account account = Account.builder().build();
        log.debug("Registering {}", account.getEmail());

        HomePage home = homePage();

        assertTrue(home.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        // Start registration
        var login = home.goToSignupLogin();

        assertTrue(login.isSignupVisible(),
                "'New User Signup!' should be visible on the Signup / Login page.");

        var signup = login.enterSignup(account).clickSignup();

        assertTrue(signup.isVisible(),
                "'ENTER ACCOUNT INFORMATION' should be visible after clicking Signup.");

        // The site carries the name and email over from the previous step
        assertEquals(signup.getPrefilledName(), account.getName(),
                "Name should be carried over to the account information form.");
        assertEquals(signup.getPrefilledEmail(), account.getEmail(),
                "Email should be carried over to the account information form.");

        // Complete the account form
        signup.fill(account);

        assertTrue(signup.isNewsletterSelected(),
                "'Sign up for our newsletter!' checkbox should be selected.");
        assertTrue(signup.isOffersSelected(),
                "'Receive special offers from our partners!' checkbox should be selected.");

        var created = signup.createAccount();

        assertEquals(created.getConfirmation(), "ACCOUNT CREATED!",
                "'ACCOUNT CREATED!' should be displayed after submitting the form.");

        // Verify the new account is signed in
        home = created.continueToHome();

        assertTrue(home.isLoggedIn(),
                "'Logged in as username' should be visible after registration.");
        assertEquals(home.getLoggedInUsername(), account.getName(),
                "Header should show the username of the account just created.");

        // Delete the account
        var deleted = home.deleteAccount();

        assertEquals(deleted.getConfirmation(), "ACCOUNT DELETED!",
                "'ACCOUNT DELETED!' should be displayed after deleting the account.");

        HomePage afterDeletion = deleted.continueToHome();

        assertTrue(afterDeletion.isVisible(),
                "Home page should be visible after continuing from account deletion.");
        assertTrue(afterDeletion.isSignedOut(),
                "User should no longer be logged in once the account is deleted.");
    }
}
