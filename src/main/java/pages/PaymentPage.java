package pages;

import models.Payment;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Payment page used by TC16.
 *
 * <p>Payment values come from a Payment model/configuration and are used only
 * with this public practice website.</p>
 */
public class PaymentPage extends BasePage {
    /**
     * Creates the PaymentPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public PaymentPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** Name on card. */
    @FindBy(name = "name_on_card")
    private WebElement nameOnCard;

    /** Card number. */
    @FindBy(name = "card_number")
    private WebElement cardNumber;

    /** CVC. */
    @FindBy(name = "cvc")
    private WebElement cvc;

    /** Expiry month. */
    @FindBy(name = "expiry_month")
    private WebElement expiryMonth;

    /** Expiry year. */
    @FindBy(name = "expiry_year")
    private WebElement expiryYear;

    /** Pay and Confirm Order button. */
    @FindBy(id = "submit")
    private WebElement payButton;

    /**
     * Fills all payment fields through custom UiActions.
     */
    public PaymentPage fill(Payment payment) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Submits payment and returns order-confirmation page. */
    public OrderPlacedPage payAndConfirm() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
