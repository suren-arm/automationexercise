package models;

/**
 * Months of the date-of-birth selector, spelled as the form lists them.
 *
 * <p>Separate from {@link java.time.Month} because what matters here is the
 * option text the page renders, not calendar arithmetic.</p>
 */
public enum Month {

    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    private final String label;

    Month(String label) {
        this.label = label;
    }

    /** Exactly as the option reads on the page, for selection by visible text. */
    public String label() {
        return label;
    }
}
