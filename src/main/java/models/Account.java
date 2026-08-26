package models;

import java.util.UUID;

/**
 * Immutable user-account test data model.
 *
 * <p>Every test can create a fresh Account through the Builder. Valid defaults
 * keep tests short, while individual fields can be overridden when required.</p>
 */
public final class Account {

    private final String title;
    private final String name;
    private final String email;
    private final String password;
    private final String day;
    private final String month;
    private final String year;
    private final boolean newsletter;
    private final boolean offers;
    private final String firstName;
    private final String lastName;
    private final String company;
    private final String address1;
    private final String address2;
    private final String country;
    private final String state;
    private final String city;
    private final String zipCode;
    private final String mobileNumber;

    /** Copies all Builder values into the immutable account. */
    private Account(Builder builder) {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /** Starts a Builder populated with valid Automation Exercise data. */
    public static Builder builder() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    public String getTitle() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getName() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getEmail() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getPassword() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getDay() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getMonth() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getYear() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public boolean isNewsletter() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public boolean isOffers() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getFirstName() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getLastName() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getCompany() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getAddress1() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getAddress2() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getCountry() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getState() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getCity() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getZipCode() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }
    public String getMobileNumber() {
        // TODO: implement.
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Builder with valid defaults.
     *
     * <p>The email is unique for every Builder instance so registration tests
     * can be repeated without collisions.</p>
     */
    public static final class Builder {

        private String title = "Mr";
        private String name = "Automation User";
        private String email = "qa." + UUID.randomUUID() + "@example.com";
        private String password = "Password123!";
        private String day = "10";
        private String month = "May";
        private String year = "1990";
        private boolean newsletter = true;
        private boolean offers = true;
        private String firstName = "Automation";
        private String lastName = "User";
        private String company = "QA Company";
        private String address1 = "1 Test Street";
        private String address2 = "Suite 10";
        private String country = "Canada";
        private String state = "Ontario";
        private String city = "Toronto";
        private String zipCode = "10001";
        private String mobileNumber = "1234567890";

        public Builder title(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder name(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder email(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder password(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder day(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder month(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder year(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder newsletter(boolean value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder offers(boolean value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder firstName(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder lastName(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder company(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder address1(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder address2(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder country(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder state(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder city(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder zipCode(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
        public Builder mobileNumber(String value) {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }

        /** Creates the immutable Account. */
        public Account build() {
            // TODO: implement.
            throw new UnsupportedOperationException("TODO");
        }
    }
}
