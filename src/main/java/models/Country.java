package models;

/**
 * Countries the registration form offers. The field is a {@code <select>}, so
 * only these values are accepted.
 */
public enum Country {

    INDIA("India"),
    UNITED_STATES("United States"),
    CANADA("Canada"),
    AUSTRALIA("Australia"),
    ISRAEL("Israel"),
    NEW_ZEALAND("New Zealand"),
    SINGAPORE("Singapore");

    private final String label;

    Country(String label) {
        this.label = label;
    }

    /** Exactly as the option reads on the page, for selection by visible text. */
    public String label() {
        return label;
    }
}
