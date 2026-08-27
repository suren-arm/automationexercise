package pages;

/**
 * Supplies the page a test starts on. From there, navigation methods return the
 * next page object, so tests never construct pages themselves.
 */
public class PageManager {

    private HomePage homePage;

    public HomePage home() {
        if (homePage == null) {
            homePage = new HomePage();
        }

        return homePage;
    }
}
