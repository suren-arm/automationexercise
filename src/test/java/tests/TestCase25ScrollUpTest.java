package tests;

import config.TestData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.HomePage;
import tests.base.BaseTest;

import static org.testng.Assert.assertTrue;

@Epic("Automation Exercise")
@Feature("Test Case 25")
public class TestCase25ScrollUpTest extends BaseTest {

    /**
     * Scrolls to the subscription section at the foot of the home page, then
     * returns to the top using the scroll-up arrow.
     *
     * <p>Each scroll is checked against the window offset as well as element
     * visibility: Selenium reports visibility from CSS, not from what is inside
     * the viewport, so a visibility check alone would pass even if nothing had
     * scrolled. The browser is launched and the site opened by
     * {@code BaseTest}.</p>
     */
    @Test(description = "Test Case 25: Verify Scroll Up using 'Arrow' button "
            + "and Scroll Down functionality")
    @Story("Scroll Up using Arrow")
    @Severity(SeverityLevel.NORMAL)
    public void scrollDownAndBackToTop() {
        HomePage homePage = homePage();

        assertTrue(homePage.isVisible(),
                "Home page should be visible after navigating to the base URL.");

        // Scroll to the bottom
        long initialPosition = homePage.getScrollPosition();

        homePage.scrollToBottom();

        long bottomPosition = homePage.getScrollPosition();

        assertTrue(bottomPosition > initialPosition,
                "Page should have scrolled down. Offset went from "
                        + initialPosition + "px to " + bottomPosition + "px.");
        assertTrue(homePage.isSubscriptionVisible(),
                "'SUBSCRIPTION' should be visible at the bottom of the home page.");

        // Return to the top using the arrow
        boolean returnedToTop = homePage.clickScrollUpArrowAndWaitForTop();

        assertTrue(returnedToTop,
                "Page should scroll back to the top after clicking the arrow. "
                        + "Final offset: " + homePage.getScrollPosition() + "px.");
        assertTrue(homePage.getHeroText().contains(TestData.homeHeroText()),
                "Hero text '" + TestData.homeHeroText()
                        + "' should be visible after scrolling back up, but was: '"
                        + homePage.getHeroText() + "'.");
    }
}
