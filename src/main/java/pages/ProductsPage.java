package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Products page used only by TC9.
 */
public class ProductsPage extends BasePage {
    /**
     * Creates the ProductsPage Page Object.
     *
     * <p>Calling super() invokes the BasePage constructor, which:
     * gets the thread-safe driver, creates waits, checks the Google vignette,
     * creates reusable action wrappers, and initializes this page's @FindBy
     * elements through PageFactory.</p>
     */
    public ProductsPage() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }


    /** All Products heading. */
    @FindBy(xpath = "//h2[normalize-space()='All Products']")
    private WebElement allProducts;

    /** Search input. */
    @FindBy(id = "search_product")
    private WebElement searchInput;

    /** Search button. */
    @FindBy(id = "submit_search")
    private WebElement searchButton;

    /** Search result heading. */
    @FindBy(xpath = "//h2[normalize-space()='Searched Products']")
    private WebElement searchedProducts;

    /** Product result names. */
    private final By resultNames =
            By.cssSelector(".features_items .productinfo p");

    /** Returns whether All Products page is visible. */
    public boolean isVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Searches for a product. */
    public ProductsPage search(String productName) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Returns whether Searched Products heading is visible. */
    public boolean isSearchResultVisible() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Returns all visible result product names. */
    public List<String> getResultNames() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
}
