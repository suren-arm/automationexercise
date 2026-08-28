package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Home page content. Shared header navigation is inherited from BasePage.
 */
public class HomePage extends BasePage {

    @FindBy(xpath = "//h2[contains(normalize-space(),"
            + "'Full-Fledged practice website for Automation Engineers')]")
    private WebElement heroText;

    @FindBy(xpath = "//h2[normalize-space()='Subscription']")
    private WebElement subscription;

    /** Shown at the bottom-right only once the page has been scrolled down. */
    @FindBy(id = "scrollUp")
    private WebElement scrollUpArrow;

    @FindBy(css = "button[data-dismiss='modal']")
    private WebElement continueShopping;

    private static final By FIRST_PRODUCT_ADD_TO_CART =
            By.cssSelector(".features_items .product-image-wrapper a.add-to-cart");

    public HomePage open(String baseUrl) {
        browserActions.open(baseUrl);
        return this;
    }

    public boolean isVisible() {
        return uiActions.isDisplayed(heroText);
    }

    public String getHeroText() {
        return uiActions.getText(heroText);
    }

    /** Adds the first product and closes the modal the site opens afterwards. */
    public HomePage addFirstProductToCart() {
        WebElement addToCart = waitUtils.visible(FIRST_PRODUCT_ADD_TO_CART);

        uiActions.scrollIntoView(addToCart);
        uiActions.click(addToCart);
        uiActions.click(continueShopping);

        return this;
    }

    public HomePage scrollToBottom() {
        browserActions.scrollToBottom();
        return this;
    }

    public boolean isSubscriptionVisible() {
        return uiActions.isDisplayed(subscription);
    }

    /**
     * Clicks the scroll-up arrow and waits for the page to settle at the top.
     *
     * <p>Animations are switched off first. The site scrolls with
     * {@code jQuery.animate(..., 'easeOutQuad')} but never loads the easing
     * plugin, so at a normal duration jQuery calls an easing function that does
     * not exist and the scroll dies. A zero duration is the path where jQuery
     * skips the easing lookup.</p>
     *
     * <p>Nothing here scrolls the page directly, so only the arrow's own
     * handler can move it.</p>
     */
    public boolean clickScrollUpArrowAndWaitForTop() {
        browserActions.disableAnimations();
        uiActions.click(scrollUpArrow);

        return browserActions.waitUntilScrolledToTop();
    }

    public long getScrollPosition() {
        return browserActions.scrollY();
    }
}
