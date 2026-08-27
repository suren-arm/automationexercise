package models;

import utils.TestDataGenerator;

/**
 * Immutable account test data. Every builder produces a fresh identity and a
 * unique email, which keeps the registration scenarios repeatable and safe to
 * run in parallel.
 */
public final class Account {

    private final Gender gender;
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
        gender = builder.gender;
        name = builder.name;
        email = builder.email;
        password = builder.password;
        day = builder.day;
        month = builder.month.label();
        year = builder.year;
        newsletter = builder.newsletter;
        offers = builder.offers;
        firstName = builder.firstName;
        lastName = builder.lastName;
        company = builder.company;
        address1 = builder.address1;
        address2 = builder.address2;
        country = builder.country.label();
        state = builder.state;
        city = builder.city;
        zipCode = builder.zipCode;
        mobileNumber = builder.mobileNumber;
    }

    /** Starts a Builder populated with valid Automation Exercise data. */
    public static Builder builder() {
        return new Builder();
    }

    public Gender getGender() { return gender; }
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
     * Builder with valid defaults for the Automation Exercise registration
     * form. Any field can be overridden; the email normally should not be.
     */
    public static final class Builder {

        private Gender gender = TestDataGenerator.randomEnum(Gender.class);

        /** Left unset so {@link #build()} can keep it in step with the names. */
        private String name;

        private String email = TestDataGenerator.uniqueEmail();
        private String password = TestDataGenerator.randomName(6)
                + TestDataGenerator.randomNumeric(4) + "!";

        // Day is capped at 28 so the date is valid in every month.
        private String day = String.valueOf(TestDataGenerator.randomInt(1, 28));
        private Month month = TestDataGenerator.randomEnum(Month.class);
        private String year = String.valueOf(TestDataGenerator.randomInt(1950, 2005));

        // The scenario requires both subscription boxes ticked.
        private boolean newsletter = true;
        private boolean offers = true;

        private String firstName = TestDataGenerator.randomName(6);
        private String lastName = TestDataGenerator.randomName(8);
        private String company = TestDataGenerator.randomName(8) + " Ltd";
        private String address1 = TestDataGenerator.randomInt(1, 999)
                + " " + TestDataGenerator.randomName(7) + " Street";
        private String address2 = "Suite " + TestDataGenerator.randomInt(1, 99);
        private Country country = TestDataGenerator.randomEnum(Country.class);
        private String state = TestDataGenerator.randomName(7);
        private String city = TestDataGenerator.randomName(7);
        private String zipCode = TestDataGenerator.randomNumeric(5);
        private String mobileNumber = TestDataGenerator.randomNumeric(10);

        public Builder gender(Gender value) { gender = value; return this; }
        public Builder name(String value) { name = value; return this; }
        public Builder email(String value) { email = value; return this; }
        public Builder password(String value) { password = value; return this; }
        public Builder day(String value) { day = value; return this; }
        public Builder month(Month value) { month = value; return this; }
        public Builder year(String value) { year = value; return this; }
        public Builder newsletter(boolean value) { newsletter = value; return this; }
        public Builder offers(boolean value) { offers = value; return this; }
        public Builder firstName(String value) { firstName = value; return this; }
        public Builder lastName(String value) { lastName = value; return this; }
        public Builder company(String value) { company = value; return this; }
        public Builder address1(String value) { address1 = value; return this; }
        public Builder address2(String value) { address2 = value; return this; }
        public Builder country(Country value) { country = value; return this; }
        public Builder state(String value) { state = value; return this; }
        public Builder city(String value) { city = value; return this; }
        public Builder zipCode(String value) { zipCode = value; return this; }
        public Builder mobileNumber(String value) { mobileNumber = value; return this; }

        /**
         * Derives the display name from the two name fields unless one was set
         * explicitly, so {@code name} can never drift from
         * {@code firstName + " " + lastName}.
         */
        public Account build() {
            if (name == null) {
                name = firstName + " " + lastName;
            }

            return new Account(this);
        }
    }
}
