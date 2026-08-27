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
        browser.open(baseUrl);
        return this;
    }

    public boolean isVisible() {
        return actions.isDisplayed(heroText);
    }

    public String getHeroText() {
        return actions.getText(heroText);
    }

    /** Adds the first product and closes the modal the site opens afterwards. */
    public HomePage addFirstProductToCart() {
        WebElement addToCart = wait.visible(FIRST_PRODUCT_ADD_TO_CART);

        actions.scrollIntoView(addToCart);
        actions.click(addToCart);
        actions.click(continueShopping);

        return this;
    }

    public HomePage scrollToBottom() {
        browser.scrollToBottom();
        return this;
    }

    public boolean isSubscriptionVisible() {
        return actions.isDisplayed(subscription);
    }

    public boolean isScrollUpArrowVisible() {
        return actions.isDisplayed(scrollUpArrow);
    }

    private static final int SCROLL_UP_ATTEMPTS = 2;

    /**
     * Clicks the scroll-up arrow and waits for the page to settle at the top.
     *
     * <p>The site scrolls with {@code jQuery.animate(..., 'easeOutQuad')} but
     * never loads the easing plugin, so at a normal duration jQuery calls an
     * easing function that does not exist and the scroll dies. Disabling
     * animations gives the tween a zero duration, where jQuery skips the easing
     * lookup. That flag lives on the document and an ad navigation can discard
     * it, hence the retry.</p>
     *
     * <p>Nothing here scrolls the page directly, so only the arrow's own
     * handler can move it and a broken arrow still fails.</p>
     */
    public boolean clickScrollUpArrowAndWaitForTop() {
        for (int attempt = 1; attempt <= SCROLL_UP_ATTEMPTS; attempt++) {
            browser.disableAnimations();
            actions.click(scrollUpArrow);

            if (browser.waitUntilScrolledToTop()) {
                return true;
            }

            if (attempt < SCROLL_UP_ATTEMPTS) {
                // Back to the bottom so the arrow is offered again.
                browser.scrollToBottom();
            }
        }

        return false;
    }

    public long getScrollPosition() {
        return browser.scrollY();
    }
}
