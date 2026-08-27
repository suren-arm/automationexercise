package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Checkout page used by TC16.
 *
 * <p>The official case verifies Address Details and Review Your Order, enters
 * a comment, then continues to payment.</p>
 */
public class CheckoutPage extends BasePage {
    /**
     * Creates the CheckoutPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public CheckoutPage() {
        super();
    }


    /** Address Details heading. */
    @FindBy(xpath = "//h2[normalize-space()='Address Details']")
    private WebElement addressDetails;

    /** Review Your Order heading. */
    @FindBy(xpath = "//h2[normalize-space()='Review Your Order']")
    private WebElement reviewOrder;

    /** Order comment text area. */
    @FindBy(name = "message")
    private WebElement comment;

    /** Place Order button. */
    @FindBy(xpath = "//a[contains(normalize-space(),'Place Order')]")
    private WebElement placeOrder;

    /** Returns whether Address Details is visible. */
    public boolean isAddressDetailsVisible() {

        return actions.isDisplayed(addressDetails);
    }

    /** Returns whether Review Your Order is visible. */
    public boolean isReviewOrderVisible() {

        return actions.isDisplayed(reviewOrder);
    }

    /** Enters order comment. */
    public CheckoutPage enterComment(String text) {

        actions.type(comment, text);
        return this;
    }

    /** Opens payment form. */
    public PaymentPage placeOrder() {

        actions.click(placeOrder);
        return new PaymentPage();
    }
}
