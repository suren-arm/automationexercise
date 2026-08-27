package config;

/**
 * How the tests run. Business values belong in {@link TestData}.
 *
 * <p>Any key can be overridden with a {@code -D} system property for a single
 * run.</p>
 */
public final class ConfigReader {

    private static final PropertiesLoader CONFIG = new PropertiesLoader("config.properties");

    private ConfigReader() {
    }

    /** chrome, firefox/gecko or edge. */
    public static String browser() {
        return CONFIG.getRequired("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(CONFIG.getRequired("headless"));
    }

    public static int explicitWaitSeconds() {
        return positiveInt("explicit.wait.seconds");
    }

    /** TestNG worker threads, applied by {@code SuiteListener}. 1 = sequential. */
    public static int threadCount() {
        return positiveInt("thread.count");
    }

    /**
     * Fails loudly rather than defaulting: a typo in a count would otherwise
     * change how the suite runs while looking like it worked.
     */
    private static int positiveInt(String key) {
        String value = CONFIG.getRequired(key);

        try {
            int parsed = Integer.parseInt(value.trim());

            if (parsed <= 0) {
                throw new IllegalStateException(
                        key + " must be greater than 0 but was: " + value);
            }

            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid " + key + ": " + value, e);
        }
    }

    public static String baseUrl() {
        return CONFIG.getRequired("base.url");
    }

    /**
     * Configured log level as written, or {@code INFO} if absent.
     * {@link LoggingConfigurator} decides whether it names a real level.
     */
    public static String logLevel() {
        String value = CONFIG.get("LOG_LEVEL");

        return value == null || value.isBlank() ? "INFO" : value.trim();
    }
}
