package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Checkout page: address details, order review and the order comment.
 */
public class CheckoutPage extends BasePage {

    @FindBy(xpath = "//h2[normalize-space()='Address Details']")
    private WebElement addressDetailsHeading;

    @FindBy(xpath = "//h2[normalize-space()='Review Your Order']")
    private WebElement reviewOrderHeading;

    @FindBy(id = "address_delivery")
    private WebElement deliveryAddress;

    @FindBy(name = "message")
    private WebElement comment;

    /**
     * Located by its destination rather than its label: the href is part of the
     * checkout flow itself, whereas the button text is presentational.
     */
    @FindBy(css = "a[href='/payment']")
    private WebElement placeOrder;

    /**
     * Matched on the row id, not the table's: the wrapper is {@code #cart_info}
     * here but {@code #cart_info_table} on the cart page, and the tbody also
     * holds a totals row.
     */
    private static final By REVIEW_PRODUCT_ROWS = By.cssSelector("tr[id^='product-']");

    /** Whether the Address Details section is displayed. */
    public boolean isAddressDetailsVisible() {
        return actions.isDisplayed(addressDetailsHeading);
    }

    /** Whether the Review Your Order section is displayed. */
    public boolean isReviewOrderVisible() {
        return actions.isDisplayed(reviewOrderHeading);
    }

    /** Delivery address block text, used to confirm the address is populated. */
    public String getDeliveryAddressText() {
        return actions.getText(deliveryAddress);
    }

    /** Number of products listed in the order review. */
    public int getReviewedItemCount() {
        return actions.getTexts(REVIEW_PRODUCT_ROWS).size();
    }

    /** Enters the order comment. */
    public CheckoutPage enterComment(String text) {
        actions.type(comment, text);
        return this;
    }

    /** Submits the order and opens the payment form. */
    public PaymentPage placeOrder() {
        actions.click(placeOrder);
        return new PaymentPage();
    }
}
