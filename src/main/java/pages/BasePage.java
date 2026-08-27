package pages;

import factory.DriverFactory;
import features.GoogleVignetteHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.BrowserActions;
import utils.UiActions;
import utils.WaitUtils;

/**
 * Base Page Object containing the site-wide Automation Exercise header.
 * Shared navigation must live here rather than being duplicated in pages.
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final WaitUtils wait;
    protected final UiActions actions;
    protected final BrowserActions browser;

    /** Handles intermittent Google vignette ads at page construction time. */
    protected final GoogleVignetteHandler vignette;

    @FindBy(css="a[href='/'] img") private WebElement logo;
    @FindBy(css="a[href='/']") private WebElement homeMenu;
    @FindBy(css="a[href='/products']") private WebElement productsMenu;
    @FindBy(css="a[href='/view_cart']") private WebElement cartMenu;
    @FindBy(css="a[href='/login']") private WebElement signupLoginMenu;
    @FindBy(css="a[href='/api_list']") private WebElement apiTestingMenu;
    @FindBy(css="a[href='https://www.youtube.com/c/AutomationExercise']") private WebElement videoTutorialsMenu;
    @FindBy(css="a[href='/contact_us']") private WebElement contactUsMenu;
    @FindBy(css="a[href='/logout']") private WebElement logoutMenu;
    @FindBy(css="a[href='/delete_account']") private WebElement deleteAccountMenu;
    @FindBy(xpath="//a[contains(.,'Logged in as')]") private WebElement loggedInAsMenu;

    protected BasePage() {
        /*
         * 1. Get the WebDriver that belongs to the current TestNG thread.
         */
        this.driver = DriverFactory.getDriver();

        /*
         * 2. Create the explicit-wait helper.
         */
        this.wait = new WaitUtils(driver);

        /*
         * 3. Check Google vignette immediately when a new page object is built.
         *    This handles an ad that is already visible after navigation.
         */
        this.vignette = new GoogleVignetteHandler(driver);
        this.vignette.closeIfPresent();

        /*
         * 4. Create reusable action wrappers.
         *    UiActions also checks the vignette immediately before each action,
         *    because an ad may appear later between two UI operations.
         */
        this.actions = new UiActions(driver);
        this.browser = new BrowserActions(driver);

        /*
         * 5. Initialize @FindBy elements declared by the concrete child page.
         *    Even though this is in BasePage, "this" is the real child object.
         */
        PageFactory.initElements(driver, this);
    }

    /** Dismisses intermittent Google vignette before important interactions. */
    

    public HomePage goToHome() { actions.click(homeMenu); return new HomePage(); }
    public ProductsPage goToProducts() { actions.click(productsMenu); return new ProductsPage(); }
    public CartPage goToCart() { actions.click(cartMenu); return new CartPage(); }
    public LoginPage goToSignupLogin() { actions.click(signupLoginMenu); return new LoginPage(); }
    public boolean isLoggedIn() { return actions.isDisplayed(loggedInAsMenu); }
    public String getLoggedInText() { return actions.getText(loggedInAsMenu); }
    public LoginPage logout() { actions.click(logoutMenu); return new LoginPage(); }
    public AccountDeletedPage deleteAccount() { actions.click(deleteAccountMenu); return new AccountDeletedPage(); }
    public boolean isLogoDisplayed() { return actions.isDisplayed(logo); }
}
