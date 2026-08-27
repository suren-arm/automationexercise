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
        nameOnCard = builder.nameOnCard;
        cardNumber = builder.cardNumber;
        cvc = builder.cvc;
        expiryMonth = builder.expiryMonth;
        expiryYear = builder.expiryYear;
    }

    /** Starts a builder with configured practice payment defaults. */
    public static Builder builder() {
        return new Builder();
    }

    public String getNameOnCard() { return nameOnCard; }
    public String getCardNumber() { return cardNumber; }
    public String getCvc() { return cvc; }
    public String getExpiryMonth() { return expiryMonth; }
    public String getExpiryYear() { return expiryYear; }

    /** Builder for test-only payment data. */
    public static final class Builder {

        private String nameOnCard = "Automation User";
        private String cardNumber = ConfigReader.get("payment.card.number");
        private String cvc = ConfigReader.get("payment.cvc");
        private String expiryMonth = ConfigReader.get("payment.expiry.month");
        private String expiryYear = ConfigReader.get("payment.expiry.year");

        public Builder nameOnCard(String value) { nameOnCard = value; return this; }
        public Builder cardNumber(String value) { cardNumber = value; return this; }
        public Builder cvc(String value) { cvc = value; return this; }
        public Builder expiryMonth(String value) { expiryMonth = value; return this; }
        public Builder expiryYear(String value) { expiryYear = value; return this; }

        /** Creates immutable Payment data. */
        public Payment build() {
            return new Payment(this);
        }
    }
}
