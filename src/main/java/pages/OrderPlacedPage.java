package pages;

import org.openqa.selenium.By;

/**
 * Order confirmation page. Delete Account navigation is inherited from BasePage.
 */
public class OrderPlacedPage extends BasePage {

    private static final By ORDER_PLACED_HEADING = By.cssSelector("[data-qa='order-placed']");

    /**
     * The official test case quotes "Your order has been placed successfully!",
     * but the live site renders "Congratulations! Your order has been
     * confirmed!". Both are accepted so the test tracks the site rather than the
     * stale documentation.
     */
    private static final By SUCCESS_MESSAGE = By.xpath(
            "//p[contains(normalize-space(),'order has been placed successfully')"
                    + " or contains(normalize-space(),'Your order has been confirmed')]");

    public boolean isOrderPlacedHeadingVisible() {
        return uiActions.isDisplayed(ORDER_PLACED_HEADING);
    }

    public boolean isOrderPlacedSuccessfully() {
        return uiActions.isDisplayed(SUCCESS_MESSAGE);
    }
}
