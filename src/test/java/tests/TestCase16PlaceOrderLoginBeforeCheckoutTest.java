package tests;

import config.ConfigReader;
import io.qameta.allure.*;
import models.Account;
import models.Payment;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

/**
 * Official Automation Exercise Test Case 16 - Place Order: Login before Checkout.
 *
 * <p>A fresh account is created first as a test precondition. The official
 * TC16 flow then starts by logging into that account, so no shared account or
 * hard-coded credentials are required.</p>
 */
@Epic("Automation Exercise")
@Feature("Test Case 16")
public class TestCase16PlaceOrderLoginBeforeCheckoutTest extends BaseTest {

    /**
     * Executes complete TC16 with an isolated account:
     * create account precondition -> logout -> login -> cart -> checkout ->
     * payment -> order confirmation -> delete account.
     */
    @Test(description = "Test Case 16: Place Order - Login before Checkout")
    @Story("Place Order: Login before Checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void testCase16PlaceOrderLoginBeforeCheckout() {
        Account account = Account.builder()
                .name("TC16 User")
                .firstName("TC16")
                .lastName("User")
                .build();

        Payment payment = Payment.builder()
                .nameOnCard(account.getFirstName() + " " + account.getLastName())
                .build();

        /*
         * PRECONDITION:
         * Create a unique account because official TC16 assumes an existing user.
         */
        var home = pages.home();

        var created = home
                .goToSignupLogin()
                .enterSignup(account)
                .clickSignup()
                .fill(account)
                .createAccount();

        Assert.assertEquals(
                created.getConfirmation(),
                "ACCOUNT CREATED!",
                "TC16 precondition account should be created.");

        home = created.continueToHome();

        Assert.assertTrue(
                home.isLoggedIn(),
                "Precondition account should be logged in.");

        /*
         * Logout so the official TC16 starts with "Login before Checkout".
         */
        var login = home.logout();

        Assert.assertTrue(
                login.isLoginVisible(),
                "Login to your account should be visible.");

        /*
         * OFFICIAL TC16 FLOW:
         * Fill email/password and click Login.
         */
        home = login
                .enterLogin(account)
                .clickLogin();

        Assert.assertTrue(
                home.isLoggedIn(),
                "Logged in as username should be visible.");

        /*
         * Add product, then explicitly open Cart.
         */
        home.addFirstProductToCart();

        var cart = home.goToCart();

        Assert.assertTrue(
                cart.isVisible(),
                "Cart page should be visible.");

        /*
         * Proceed to Checkout and verify required sections.
         */
        var checkout = cart.proceedToCheckout();

        Assert.assertTrue(
                checkout.isAddressDetailsVisible(),
                "Address Details should be visible.");

        Assert.assertTrue(
                checkout.isReviewOrderVisible(),
                "Review Your Order should be visible.");

        /*
         * Enter comment and proceed to payment.
         */
        var paymentPage = checkout
                .enterComment(ConfigReader.get("checkout.comment"))
                .placeOrder();

        /*
         * Fill payment and confirm the order.
         */
        var orderPlaced = paymentPage
                .fill(payment)
                .payAndConfirm();

        Assert.assertTrue(
                orderPlaced.isOrderPlacedSuccessfully(),
                "Order success message should be visible.");

        /*
         * TC16 cleanup: delete account and verify deletion.
         */
        var deleted = orderPlaced.deleteAccount();

        Assert.assertEquals(
                deleted.getConfirmation(),
                "ACCOUNT DELETED!",
                "ACCOUNT DELETED! should be visible.");

        Assert.assertTrue(
                deleted.continueToHome().isVisible(),
                "Home should be visible after account deletion.");
    }
}
