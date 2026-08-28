package pages;

import models.Payment;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Payment form.
 *
 * <p>The card values are dummy test data for this public practice site and are
 * never logged.</p>
 */
public class PaymentPage extends BasePage {

    @FindBy(name = "name_on_card")
    private WebElement nameOnCard;

    @FindBy(name = "card_number")
    private WebElement cardNumber;

    @FindBy(name = "cvc")
    private WebElement cvc;

    @FindBy(name = "expiry_month")
    private WebElement expiryMonth;

    @FindBy(name = "expiry_year")
    private WebElement expiryYear;

    @FindBy(css = "button[data-qa='pay-button']")
    private WebElement payButton;

    /** Fills every payment field. */
    public PaymentPage fill(Payment payment) {
        uiActions.type(nameOnCard, payment.getNameOnCard());
        uiActions.type(cardNumber, payment.getCardNumber());
        uiActions.type(cvc, payment.getCvc());
        uiActions.type(expiryMonth, payment.getExpiryMonth());
        uiActions.type(expiryYear, payment.getExpiryYear());
        return this;
    }

    /** Submits payment and lands on the order confirmation page. */
    public OrderPlacedPage payAndConfirm() {
        uiActions.click(payButton);
        return new OrderPlacedPage();
    }
}
