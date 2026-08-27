package pages;

/**
 * Lazy Page Object manager.
 *
 * <p>Only pages required by test cases 1, 9, 16 and 25 are represented.</p>
 */
public class PageManager {

    private HomePage homePage;

    /** Returns HomePage. */
    public HomePage home() {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }
}
