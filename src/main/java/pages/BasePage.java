package pages;

import factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.BrowserActions;
import utils.UiActions;
import utils.WaitUtils;

/**
 * Base of every page object. The Automation Exercise header appears on every
 * page, so its locators and navigation live here once.
 *
 * <p>Navigation methods return a new page object, which re-initialises
 * PageFactory against the page that just loaded.</p>
 */
public abstract class BasePage {

    protected final WaitUtils waitUtils;
    protected final UiActions uiActions;
    protected final BrowserActions browserActions;

    @FindBy(css = "a[href='/products']")
    private WebElement productsMenu;

    @FindBy(css = "a[href='/view_cart']")
    private WebElement cartMenu;

    @FindBy(css = "a[href='/login']")
    private WebElement signupLoginMenu;

    @FindBy(css = "a[href='/logout']")
    private WebElement logoutMenu;

    @FindBy(css = "a[href='/delete_account']")
    private WebElement deleteAccountMenu;

    @FindBy(xpath = "//a[contains(.,'Logged in as')]")
    private WebElement loggedInAsMenu;

    protected BasePage() {
        // Kept local rather than protected so pages have to go through
        // UiActions, which applies waiting, logging and ad recovery.
        WebDriver driver = DriverFactory.getDriver();

        this.waitUtils = new WaitUtils(driver);
        this.uiActions = new UiActions(driver);
        this.browserActions = new BrowserActions(driver);

        PageFactory.initElements(driver, this);
    }

    public ProductsPage goToProducts() {
        uiActions.click(productsMenu);
        return new ProductsPage();
    }

    public CartPage goToCart() {
        uiActions.click(cartMenu);
        return new CartPage();
    }

    public LoginPage goToSignupLogin() {
        uiActions.click(signupLoginMenu);
        return new LoginPage();
    }

    public LoginPage logout() {
        uiActions.click(logoutMenu);
        return new LoginPage();
    }

    public AccountDeletedPage deleteAccount() {
        uiActions.click(deleteAccountMenu);
        return new AccountDeletedPage();
    }

    public boolean isLoggedIn() {
        return uiActions.isDisplayed(loggedInAsMenu);
    }

    /**
     * Waits for the Signup / Login link instead of negating {@link #isLoggedIn()},
     * which would burn the full timeout waiting for something to disappear.
     */
    public boolean isSignedOut() {
        return uiActions.isDisplayed(signupLoginMenu);
    }

    /** Header username with the "Logged in as" prefix stripped. */
    public String getLoggedInUsername() {
        return uiActions.getText(loggedInAsMenu)
                .replace("Logged in as", "")
                .trim();
    }
}
