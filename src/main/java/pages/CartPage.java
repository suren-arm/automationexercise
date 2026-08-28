package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Shopping cart page.
 */
public class CartPage extends BasePage {

    /**
     * Matches the breadcrumb by class token rather than an exact {@code @class}
     * comparison, so an extra class on the element cannot break the locator.
     */
    @FindBy(xpath = "//li[contains(concat(' ', normalize-space(@class), ' '), ' active ')"
            + " and normalize-space()='Shopping Cart']")
    private WebElement heading;

    @FindBy(css = "a.check_out")
    private WebElement proceedToCheckout;

    /**
     * Product rows of the cart table. Matched on the per-product row id so the
     * same locator holds on the cart and checkout pages, which wrap the table
     * in differently named containers.
     */
    private static final By CART_PRODUCT_ROWS = By.cssSelector("tr[id^='product-']");

    /** Whether the cart page is displayed. */
    public boolean isVisible() {
        return uiActions.isDisplayed(heading);
    }

    /** Number of product rows currently in the cart. */
    public int getItemCount() {
        return uiActions.getTexts(CART_PRODUCT_ROWS).size();
    }

    /** Proceeds from the cart to checkout. */
    public CheckoutPage proceedToCheckout() {
        uiActions.click(proceedToCheckout);
        return new CheckoutPage();
    }
}
