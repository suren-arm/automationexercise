package tests;

import config.TestData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import pages.ProductsPage;
import tests.base.BaseTest;

import java.util.List;
import java.util.Locale;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Epic("Automation Exercise")
@Feature("Test Case 9")
public class TestCase9SearchProductTest extends BaseTest {

    /**
     * Searches the product catalogue and confirms the results section appears
     * and every product returned matches the search term.
     *
     * <p>The browser is launched and the site opened by {@code BaseTest}.</p>
     */
    @Test(description = "Test Case 9: Search Product")
    @Story("Search Product")
    @Severity(SeverityLevel.NORMAL)
    public void searchProduct() {
        String searchTerm = TestData.searchProduct();

        HomePage homePage = homePage();

        assertTrue(homePage.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        // Open the catalogue
        ProductsPage productsPage = homePage.goToProducts();

        assertTrue(productsPage.isVisible(),
                "'ALL PRODUCTS' should be visible after clicking Products.");

        // Search
        productsPage.search(searchTerm);

        assertTrue(productsPage.isSearchResultVisible(),
                "'SEARCHED PRODUCTS' should be visible after running a search.");

        // Every result must relate to the term - asserting only on the heading
        // would pass even if the site returned the unfiltered catalogue
        List<String> resultNames = productsPage.getResultNames();

        assertFalse(resultNames.isEmpty(),
                "Search for '" + searchTerm + "' should return at least one product.");

        // Soft, so a single unrelated product does not hide the rest: the useful
        // diagnostic is how many results are wrong, not just the first one.
        String expected = searchTerm.toLowerCase(Locale.ROOT);
        SoftAssert softAssert = new SoftAssert();

        for (String name : resultNames) {
            softAssert.assertTrue(name.toLowerCase(Locale.ROOT).contains(expected),
                    "Search returned '" + name + "', which is unrelated to '"
                            + searchTerm + "'.");
        }

        softAssert.assertAll();
    }
}
