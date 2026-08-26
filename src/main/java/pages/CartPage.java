package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Shopping cart page used only by TC16.
 */
public class CartPage extends BasePage {
    /**
     * Creates the CartPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public CartPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** Shopping Cart breadcrumb/heading. */
    @FindBy(xpath = "//li[@class='active' and normalize-space()='Shopping Cart']")
    private WebElement heading;

    /** Proceed To Checkout button. */
    @FindBy(xpath = "//a[contains(normalize-space(),'Proceed To Checkout')]")
    private WebElement proceedToCheckout;

    /** Returns whether cart page is displayed. */
    public boolean isVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Proceeds from Cart to Checkout. */
    public CheckoutPage proceedToCheckout() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
