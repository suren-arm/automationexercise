package pages;

import org.openqa.selenium.By;

/**
 * Order confirmation page used by TC16.
 * Shared Delete Account navigation is inherited from BasePage.
 */
public class OrderPlacedPage extends BasePage {
    /**
     * Creates the OrderPlacedPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public OrderPlacedPage() {
        super();
    }


    /**
     * Automation Exercise has used slightly different confirmation markup/text
     * over time, so this locator targets the public success area rather than a
     * fragile exact tag structure.
     */
    private final By successMessage = By.xpath(
            "//*[contains(normalize-space(),'order has been placed successfully') "
                    + "or contains(normalize-space(),'Congratulations! Your order has been confirmed!')]");

    /** Returns whether an order success message is visible. */
    public boolean isOrderPlacedSuccessfully() {

        return actions.isDisplayed(wait.visible(successMessage));
    }
}
