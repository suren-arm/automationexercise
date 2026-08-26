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
    @FindBy(css="a[href='/test_cases']") private WebElement testCasesMenu;
    @FindBy(css="a[href='/api_list']") private WebElement apiTestingMenu;
    @FindBy(css="a[href='https://www.youtube.com/c/AutomationExercise']") private WebElement videoTutorialsMenu;
    @FindBy(css="a[href='/contact_us']") private WebElement contactUsMenu;
    @FindBy(css="a[href='/logout']") private WebElement logoutMenu;
    @FindBy(css="a[href='/delete_account']") private WebElement deleteAccountMenu;
    @FindBy(xpath="//a[contains(.,'Logged in as')]") private WebElement loggedInAsMenu;

    protected BasePage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Dismisses intermittent Google vignette before important interactions. */
    

    public HomePage goToHome() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public ProductsPage goToProducts() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public CartPage goToCart() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public LoginPage goToSignupLogin() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public TestCasesPage goToTestCases() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public boolean isLoggedIn() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getLoggedInText() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public LoginPage logout() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public AccountDeletedPage deleteAccount() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public boolean isLogoDisplayed() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
