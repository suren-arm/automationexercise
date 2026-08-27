package tests;

import config.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

/**
 * Official Automation Exercise Test Case 9 - Search Product.
 */
@Epic("Automation Exercise")
@Feature("Test Case 9")
public class TestCase9SearchProductTest extends BaseTest {

    /**
     * Executes TC9 exactly:
     * Home -> Products -> Search -> verify Search Products and matching results.
     */
    @Test(description = "Test Case 9: Search Product")
    @Story("Search Product")
    @Severity(SeverityLevel.NORMAL)
    public void testCase9SearchProduct() {
        var home = pages.home();

        var products = home.goToProducts();

        Assert.assertTrue(
                products.isVisible(),
                "ALL PRODUCTS should be visible.");

        String productName = ConfigReader.get("search.product");

        products.search(productName);

        Assert.assertTrue(
                products.isSearchResultVisible(),
                "SEARCHED PRODUCTS should be visible.");

        var resultNames = products.getResultNames();

        Assert.assertFalse(
                resultNames.isEmpty(),
                "Search should return at least one product.");

        resultNames.forEach(name ->
                Assert.assertTrue(
                        name.toLowerCase().contains(productName.toLowerCase()),
                        "Product '" + name + "' is unrelated to search '" + productName + "'."));
    }
}
