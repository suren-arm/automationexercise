package models;

import config.ConfigReader;

/**
 * Immutable payment test data for the public Automation Exercise practice form.
 *
 * <p>This is not a real card. Values are test-only and come from configuration.</p>
 */
public final class Payment {

    private final String nameOnCard;
    private final String cardNumber;
    private final String cvc;
    private final String expiryMonth;
    private final String expiryYear;

    /** Creates an immutable Payment from Builder values. */
    private Payment(Builder builder) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Starts a builder with configured practice payment defaults. */
    public static Builder builder() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    public String getNameOnCard() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getCardNumber() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getCvc() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getExpiryMonth() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getExpiryYear() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Builder for test-only payment data. */
    public static final class Builder {

        private String nameOnCard = "Automation User";
        private String cardNumber = ConfigReader.get("payment.card.number");
        private String cvc = ConfigReader.get("payment.cvc");
        private String expiryMonth = ConfigReader.get("payment.expiry.month");
        private String expiryYear = ConfigReader.get("payment.expiry.year");

        public Builder nameOnCard(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder cardNumber(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder cvc(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder expiryMonth(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder expiryYear(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }

        /** Creates immutable Payment data. */
        public Payment build() {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
    }
}
