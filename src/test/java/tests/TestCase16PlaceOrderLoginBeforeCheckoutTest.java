package tests;

import config.TestData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import models.Account;
import models.Payment;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AccountCreatedPage;
import pages.AccountDeletedPage;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import pages.OrderPlacedPage;
import tests.base.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("Automation Exercise")
@Feature("Test Case 16")
public class TestCase16PlaceOrderLoginBeforeCheckoutTest extends BaseTest {

    private Account account;
    private Payment payment;
    private LoginPage loginPage;

    /**
     * Registers the user the scenario assumes already exists, then signs out so
     * the test can begin at the official first step.
     *
     * <p>This lives outside the test on purpose: a failure to register is a
     * precondition that could not be met, so it is reported as a configuration
     * failure and the test is skipped rather than recorded as "placing an order
     * is broken". Creating its own account is also what keeps the scenario
     * independent of Test Case 1 and repeatable.</p>
     */
    @BeforeMethod
    public void registerUserAndSignOut() {
        account = Account.builder().build();
        payment = Payment.builder().nameOnCard(account.getName()).build();

        log.debug("Placing an order as {}", account.getEmail());

        HomePage homePage = homePage();

        assertTrue(homePage.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        loginPage = registerAccountToLogInWith(account).logout();
    }

    /**
     * Logs the registered user in, buys a product through checkout and payment,
     * and confirms the order is placed.
     */
    @Test(description = "Test Case 16: Place Order - Login before Checkout")
    @Story("Place Order: Login before Checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void placeOrderAfterLogin() {
        assertTrue(loginPage.isLoginVisible(),
                "'Login to your account' should be visible after logging out.");

        HomePage homePage = loginPage.enterLogin(account).clickLogin();

        assertTrue(homePage.isLoggedIn(),
                "'Logged in as username' should be visible after logging in.");
        assertEquals(homePage.getLoggedInUsername(), account.getName(),
                "Header should show the username of the account that logged in.");

        // Add a product and open the cart
        homePage.addFirstProductToCart();

        CartPage cartPage = homePage.goToCart();

        assertTrue(cartPage.isVisible(),
                "Cart page should be displayed after clicking Cart.");
        assertTrue(cartPage.getItemCount() > 0,
                "Cart should contain the product that was just added.");

        // Check out and confirm the order details
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        // Hard: the rest of this block reads the checkout page, so it has to
        // have loaded before any of those checks mean anything.
        assertTrue(checkoutPage.isAddressDetailsVisible(),
                "'Address Details' should be visible on the checkout page.");

        // Independent details of the same page - a wrong address does not stop
        // the order contents being worth checking, and vice versa.
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(checkoutPage.isReviewOrderVisible(),
                "'Review Your Order' should be visible on the checkout page.");
        softAssert.assertTrue(
                checkoutPage.getDeliveryAddressText().contains(account.getAddress1()),
                "Delivery address should show the address registered for this account.");
        softAssert.assertTrue(checkoutPage.getReviewedItemCount() > 0,
                "Order review should list the product being purchased.");

        // Before paying: an order whose details are wrong should not be placed.
        softAssert.assertAll();

        // Pay
        OrderPlacedPage orderPlacedPage = checkoutPage
                .enterComment(TestData.checkoutComment())
                .placeOrder()
                .fill(payment)
                .payAndConfirm();

        assertTrue(orderPlacedPage.isOrderPlacedHeadingVisible(),
                "'ORDER PLACED!' confirmation heading should be visible.");
        assertTrue(orderPlacedPage.isOrderPlacedSuccessfully(),
                "Order confirmation message should be visible after paying.");

        // Clean up
        AccountDeletedPage accountDeletedPage = orderPlacedPage.deleteAccount();

        assertEquals(accountDeletedPage.getConfirmation(), "ACCOUNT DELETED!",
                "'ACCOUNT DELETED!' should be displayed after deleting the account.");
        assertTrue(accountDeletedPage.continueToHome().isVisible(),
                "Home page should be visible after continuing from account deletion.");
    }

    /** @return the home page, signed in as the newly registered account */
    @Step("Register the account that will log in before checkout")
    private HomePage registerAccountToLogInWith(Account account) {
        AccountCreatedPage accountCreatedPage = homePage()
                .goToSignupLogin()
                .enterSignup(account)
                .clickSignup()
                .fill(account)
                .createAccount();

        assertEquals(accountCreatedPage.getConfirmation(), "ACCOUNT CREATED!",
                "Prerequisite account for this scenario should be created.");

        HomePage signedInHomePage = accountCreatedPage.continueToHome();

        assertTrue(signedInHomePage.isLoggedIn(),
                "Prerequisite account should be logged in after registration.");

        return signedInHomePage;
    }
}
