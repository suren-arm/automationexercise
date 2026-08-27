package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Home-specific content. Shared header navigation is inherited from BasePage. */
public class HomePage extends BasePage {
    /**
     * Creates the HomePage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public HomePage() {
        super();
    }

    @FindBy(xpath="//h2[contains(normalize-space(),'Full-Fledged practice website for Automation Engineers')]") private WebElement heroText;
    @FindBy(xpath="//h2[normalize-space()='Subscription']") private WebElement subscription;
    @FindBy(id="scrollUp") private WebElement scrollUpArrow;
    private final By firstProductAddToCart=By.cssSelector(".features_items .product-image-wrapper a.add-to-cart");
    @FindBy(css="button[data-dismiss='modal']") private WebElement continueShopping;

    public HomePage open(String baseUrl) { browser.open(baseUrl); return this; }
    public boolean isVisible() { return actions.isDisplayed(heroText); }
    public HomePage addFirstProductToCart() { WebElement e=wait.visible(firstProductAddToCart); actions.scrollIntoView(e); actions.click(e); actions.click(continueShopping); return this; }
    public HomePage scrollToBottom() { actions.scrollToBottom(); return this; }
    public boolean isSubscriptionVisible() { return actions.isDisplayed(subscription); }
    public HomePage clickScrollUpArrow() { actions.click(scrollUpArrow); return this; }
    public String getHeroText() { return actions.getText(heroText); }
}
