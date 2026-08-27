package tests;

import io.qameta.allure.*;
import models.Account;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

/**
 * Official Automation Exercise Test Case 1 - Register User.
 */
@Epic("Automation Exercise")
@Feature("Test Case 1")
public class TestCase1RegisterUserTest extends BaseTest {

    /**
     * Executes the complete TC1 flow:
     * home -> signup -> create account -> verify login -> delete account.
     */
    @Test(description = "Test Case 1: Register User")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void testCase1RegisterUser() {
        Account account = Account.builder()
                .name("TC1 User")
                .firstName("TC1")
                .lastName("User")
                .build();

        var home = pages.home();

        var login = home.goToSignupLogin();

        Assert.assertTrue(
                login.isSignupVisible(),
                "New User Signup! should be visible.");

        var signup = login
                .enterSignup(account)
                .clickSignup();

        Assert.assertTrue(
                signup.isVisible(),
                "ENTER ACCOUNT INFORMATION should be visible.");

        var created = signup
                .fill(account)
                .createAccount();

        Assert.assertEquals(
                created.getConfirmation(),
                "ACCOUNT CREATED!",
                "ACCOUNT CREATED! should be visible.");

        home = created.continueToHome();

        Assert.assertTrue(
                home.isLoggedIn(),
                "Logged in as username should be visible.");

        Assert.assertTrue(
                home.getLoggedInText().contains(account.getName()),
                "Logged-in username should match created account.");

        var deleted = home.deleteAccount();

        Assert.assertEquals(
                deleted.getConfirmation(),
                "ACCOUNT DELETED!",
                "ACCOUNT DELETED! should be visible.");

        Assert.assertTrue(
                deleted.continueToHome().isVisible(),
                "Home should be visible after deletion.");
    }
}
