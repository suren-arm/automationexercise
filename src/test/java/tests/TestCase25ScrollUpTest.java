package tests;

import config.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.base.BaseTest;

/**
 * Official Automation Exercise Test Case 25 -
 * Verify Scroll Up using Arrow button and Scroll Down functionality.
 */
@Epic("Automation Exercise")
@Feature("Test Case 25")
public class TestCase25ScrollUpTest extends BaseTest {

    /**
     * Executes TC25 exactly:
     * Home -> scroll bottom -> verify Subscription -> click arrow ->
     * verify hero text at top.
     */
    @Test(description = "Test Case 25: Verify Scroll Up using Arrow button and Scroll Down functionality")
    @Story("Scroll Up using Arrow")
    @Severity(SeverityLevel.NORMAL)
    public void testCase25ScrollUpUsingArrow() {
        var home = pages.testCases().goToHome();

        Assert.assertTrue(
                home.isVisible(),
                "Home page should be visible.");

        home.scrollToBottom();

        Assert.assertTrue(
                home.isSubscriptionVisible(),
                "SUBSCRIPTION should be visible.");

        home.clickScrollUpArrow();

        Assert.assertTrue(
                home.getHeroText().contains(ConfigReader.get("home.hero.text")),
                "Full-Fledged practice website for Automation Engineers should be visible.");
    }
}
