package config;

/**
 * Business values for the four scenarios - what the tests exercise, as opposed
 * to {@link ConfigReader}, which covers how the framework runs.
 *
 * <p>Per-run unique data such as the registration email is generated at runtime
 * by the Account model, not stored here.</p>
 */
public final class TestData {

    private static final PropertiesLoader DATA = new PropertiesLoader("testdata.properties");

    private TestData() {
    }

    /** Test Case 9: term typed into the product search box. */
    public static String searchProduct() {
        return DATA.getRequired("search.product");
    }

    /** Test Case 25: hero text that must be visible again after scrolling up. */
    public static String homeHeroText() {
        return DATA.getRequired("home.hero.text");
    }

    /** Test Case 16: comment entered on the checkout page. */
    public static String checkoutComment() {
        return DATA.getRequired("checkout.comment");
    }

    /** Test Case 16: dummy card number accepted by the public practice site. */
    public static String cardNumber() {
        return DATA.getRequired("payment.card.number");
    }

    /** Test Case 16: dummy card CVC. */
    public static String cvc() {
        return DATA.getRequired("payment.cvc");
    }

    /** Test Case 16: dummy card expiry month. */
    public static String expiryMonth() {
        return DATA.getRequired("payment.expiry.month");
    }

    /** Test Case 16: dummy card expiry year. */
    public static String expiryYear() {
        return DATA.getRequired("payment.expiry.year");
    }
}
