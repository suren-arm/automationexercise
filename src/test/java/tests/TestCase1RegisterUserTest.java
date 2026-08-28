package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.Account;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AccountCreatedPage;
import pages.AccountDeletedPage;
import pages.HomePage;
import pages.LoginPage;
import pages.SignupPage;
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

        HomePage homePage = homePage();

        assertTrue(homePage.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        // Start registration
        LoginPage loginPage = homePage.goToSignupLogin();

        assertTrue(loginPage.isSignupVisible(),
                "'New User Signup!' should be visible on the Signup / Login page.");

        SignupPage signupPage = loginPage.enterSignup(account).clickSignup();

        assertTrue(signupPage.isVisible(),
                "'ENTER ACCOUNT INFORMATION' should be visible after clicking Signup.");

        // Four independent facts about the form: the name and email carried over
        // from the previous step, and both subscription boxes ticked. Soft, so
        // one wrong field does not hide the other three.
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(signupPage.getPrefilledName(), account.getName(),
                "Name should be carried over to the account information form.");
        softAssert.assertEquals(signupPage.getPrefilledEmail(), account.getEmail(),
                "Email should be carried over to the account information form.");

        // Complete the account form
        signupPage.fill(account);

        softAssert.assertTrue(signupPage.isNewsletterSelected(),
                "'Sign up for our newsletter!' checkbox should be selected.");
        softAssert.assertTrue(signupPage.isOffersSelected(),
                "'Receive special offers from our partners!' checkbox should be selected.");

        // Before submitting: the form should be right before an account is made.
        softAssert.assertAll();

        AccountCreatedPage accountCreatedPage = signupPage.createAccount();

        assertEquals(accountCreatedPage.getConfirmation(), "ACCOUNT CREATED!",
                "'ACCOUNT CREATED!' should be displayed after submitting the form.");

        // Verify the new account is signed in
        homePage = accountCreatedPage.continueToHome();

        assertTrue(homePage.isLoggedIn(),
                "'Logged in as username' should be visible after registration.");
        assertEquals(homePage.getLoggedInUsername(), account.getName(),
                "Header should show the username of the account just created.");

        // Delete the account
        AccountDeletedPage accountDeletedPage = homePage.deleteAccount();

        assertEquals(accountDeletedPage.getConfirmation(), "ACCOUNT DELETED!",
                "'ACCOUNT DELETED!' should be displayed after deleting the account.");

        HomePage homePageAfterDeletion = accountDeletedPage.continueToHome();

        assertTrue(homePageAfterDeletion.isVisible(),
                "Home page should be visible after continuing from account deletion.");
        assertTrue(homePageAfterDeletion.isSignedOut(),
                "User should no longer be logged in once the account is deleted.");
    }
}
