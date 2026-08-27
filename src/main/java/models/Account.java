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
        title = builder.title;
        name = builder.name;
        email = builder.email;
        password = builder.password;
        day = builder.day;
        month = builder.month;
        year = builder.year;
        newsletter = builder.newsletter;
        offers = builder.offers;
        firstName = builder.firstName;
        lastName = builder.lastName;
        company = builder.company;
        address1 = builder.address1;
        address2 = builder.address2;
        country = builder.country;
        state = builder.state;
        city = builder.city;
        zipCode = builder.zipCode;
        mobileNumber = builder.mobileNumber;
    }

    /** Starts a Builder populated with valid Automation Exercise data. */
    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() { return title; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDay() { return day; }
    public String getMonth() { return month; }
    public String getYear() { return year; }
    public boolean isNewsletter() { return newsletter; }
    public boolean isOffers() { return offers; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompany() { return company; }
    public String getAddress1() { return address1; }
    public String getAddress2() { return address2; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
    public String getMobileNumber() { return mobileNumber; }

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

        public Builder title(String value) { title = value; return this; }
        public Builder name(String value) { name = value; return this; }
        public Builder email(String value) { email = value; return this; }
        public Builder password(String value) { password = value; return this; }
        public Builder day(String value) { day = value; return this; }
        public Builder month(String value) { month = value; return this; }
        public Builder year(String value) { year = value; return this; }
        public Builder newsletter(boolean value) { newsletter = value; return this; }
        public Builder offers(boolean value) { offers = value; return this; }
        public Builder firstName(String value) { firstName = value; return this; }
        public Builder lastName(String value) { lastName = value; return this; }
        public Builder company(String value) { company = value; return this; }
        public Builder address1(String value) { address1 = value; return this; }
        public Builder address2(String value) { address2 = value; return this; }
        public Builder country(String value) { country = value; return this; }
        public Builder state(String value) { state = value; return this; }
        public Builder city(String value) { city = value; return this; }
        public Builder zipCode(String value) { zipCode = value; return this; }
        public Builder mobileNumber(String value) { mobileNumber = value; return this; }

        /** Creates the immutable Account. */
        public Account build() {
            return new Account(this);
        }
    }
}
