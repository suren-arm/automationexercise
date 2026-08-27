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
import org.testng.annotations.Test;
import pages.HomePage;
import tests.base.BaseTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("Automation Exercise")
@Feature("Test Case 16")
public class TestCase16PlaceOrderLoginBeforeCheckoutTest extends BaseTest {

    /**
     * Logs an existing user in, buys a product through checkout and payment, and
     * confirms the order is placed.
     *
     * <p>The scenario starts at the login step, so it needs a registered user.
     * The test creates one itself rather than depending on Test Case 1 or a
     * shared account, which keeps it runnable alone, in any order, and
     * repeatedly. The browser is launched and the site opened by
     * {@code BaseTest}.</p>
     */
    @Test(description = "Test Case 16: Place Order - Login before Checkout")
    @Story("Place Order: Login before Checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void placeOrderAfterLogin() {
        Account account = Account.builder()
                .name("TC16 User")
                .firstName("TC16")
                .lastName("User")
                .build();

        Payment payment = Payment.builder()
                .nameOnCard(account.getFirstName() + " " + account.getLastName())
                .build();

        HomePage home = homePage();

        assertTrue(home.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        // Prerequisite: the scenario assumes a user that already exists
        home = registerAccountToLogInWith(account);

        // Log in
        var login = home.logout();

        assertTrue(login.isLoginVisible(),
                "'Login to your account' should be visible after logging out.");

        home = login.enterLogin(account).clickLogin();

        assertTrue(home.isLoggedIn(),
                "'Logged in as username' should be visible after logging in.");
        assertEquals(home.getLoggedInUsername(), account.getName(),
                "Header should show the username of the account that logged in.");

        // Add a product and open the cart
        home.addFirstProductToCart();

        var cart = home.goToCart();

        assertTrue(cart.isVisible(),
                "Cart page should be displayed after clicking Cart.");
        assertTrue(cart.getItemCount() > 0,
                "Cart should contain the product that was just added.");

        // Check out and confirm the order details
        var checkout = cart.proceedToCheckout();

        assertTrue(checkout.isAddressDetailsVisible(),
                "'Address Details' should be visible on the checkout page.");
        assertTrue(checkout.isReviewOrderVisible(),
                "'Review Your Order' should be visible on the checkout page.");
        assertTrue(checkout.getDeliveryAddressText().contains(account.getAddress1()),
                "Delivery address should show the address registered for this account.");
        assertTrue(checkout.getReviewedItemCount() > 0,
                "Order review should list the product being purchased.");

        // Pay
        var orderPlaced = checkout
                .enterComment(TestData.checkoutComment())
                .placeOrder()
                .fill(payment)
                .payAndConfirm();

        assertTrue(orderPlaced.isOrderPlacedHeadingVisible(),
                "'ORDER PLACED!' confirmation heading should be visible.");
        assertTrue(orderPlaced.isOrderPlacedSuccessfully(),
                "Order confirmation message should be visible after paying.");

        // Clean up
        var deleted = orderPlaced.deleteAccount();

        assertEquals(deleted.getConfirmation(), "ACCOUNT DELETED!",
                "'ACCOUNT DELETED!' should be displayed after deleting the account.");
        assertTrue(deleted.continueToHome().isVisible(),
                "Home page should be visible after continuing from account deletion.");
    }

    /** @return the home page, signed in as the newly registered account */
    @Step("Register the account that will log in before checkout")
    private HomePage registerAccountToLogInWith(Account account) {
        var created = homePage()
                .goToSignupLogin()
                .enterSignup(account)
                .clickSignup()
                .fill(account)
                .createAccount();

        assertEquals(created.getConfirmation(), "ACCOUNT CREATED!",
                "Prerequisite account for this scenario should be created.");

        HomePage signedIn = created.continueToHome();

        assertTrue(signedIn.isLoggedIn(),
                "Prerequisite account should be logged in after registration.");

        return signedIn;
    }
}
