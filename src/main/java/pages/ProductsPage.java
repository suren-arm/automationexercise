package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * All Products page and product search.
 */
public class ProductsPage extends BasePage {

    @FindBy(xpath = "//h2[normalize-space()='All Products']")
    private WebElement allProductsHeading;

    @FindBy(id = "search_product")
    private WebElement searchInput;

    @FindBy(id = "submit_search")
    private WebElement searchButton;

    @FindBy(xpath = "//h2[normalize-space()='Searched Products']")
    private WebElement searchedProductsHeading;

    /**
     * Product names in the results grid. Resolved on demand because the grid is
     * replaced when a search runs.
     */
    private static final By RESULT_NAMES =
            By.cssSelector(".features_items .productinfo p");

    /** Whether the ALL PRODUCTS listing is displayed. */
    public boolean isVisible() {
        return actions.isDisplayed(allProductsHeading);
    }

    /** Types a search term and submits it. */
    public ProductsPage search(String productName) {
        actions.type(searchInput, productName);
        actions.click(searchButton);
        return this;
    }

    /** Whether the SEARCHED PRODUCTS heading is displayed. */
    public boolean isSearchResultVisible() {
        return actions.isDisplayed(searchedProductsHeading);
    }

    /** Names of every product returned by the search. */
    public List<String> getResultNames() {
        return actions.getTexts(RESULT_NAMES);
    }
}
