package utils;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates the random parts of test data.
 *
 * <p>{@link ThreadLocalRandom} rather than a shared {@code Random}, so parallel
 * workers do not contend on one generator.</p>
 */
public final class TestDataGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    /** Separates addresses created within the same millisecond. */
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private TestDataGenerator() {
    }

    /** Lower-case alphabetic string of the given length. */
    public static String randomAlphabetic(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder value = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            value.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        return value.toString();
    }

    /** Capitalised alphabetic word, for name fields. */
    public static String randomName(int length) {
        String value = randomAlphabetic(length);
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    /** Digit string of the given length, for postcodes and phone numbers. */
    public static String randomNumeric(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder value = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            value.append(random.nextInt(10));
        }

        return value.toString();
    }

    /** Whole number between the two bounds, both inclusive. */
    public static int randomInt(int minInclusive, int maxInclusive) {
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }

    /**
     * A random constant of the given enum.
     *
     * <p>One implementation for every enum, so the option types stay pure data
     * and know nothing about how test values are generated.</p>
     */
    public static <E extends Enum<E>> E randomEnum(Class<E> type) {
        E[] values = type.getEnumConstants();
        return values[randomInt(0, values.length - 1)];
    }

    /**
     * Address that is unique per run, so registration can be repeated without
     * hitting "email already exists".
     *
     * <p>The timestamp makes it unique across runs and the sequence across
     * threads within one millisecond; base-36 keeps it short enough to read in a
     * log.</p>
     */
    public static String uniqueEmail() {
        String unique = Long.toString(Instant.now().toEpochMilli(), Character.MAX_RADIX)
                + SEQUENCE.incrementAndGet();

        return "qa." + randomAlphabetic(4) + unique + "@example.com";
    }
}
