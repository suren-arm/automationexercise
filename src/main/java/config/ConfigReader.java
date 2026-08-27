package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads framework configuration.
 *
 * <p>Java system properties have priority over config.properties. Therefore
 * Jenkins can override browser, headless mode, or environment without changing
 * source code.</p>
 */
public final class ConfigReader {

    /** Loaded configuration values. */
    private static final Properties PROPERTIES = new Properties();

    /** Loads config.properties once when the class is initialized. */
    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException("config.properties was not found.");
            }

            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config.properties.", e);
        }
    }

    /** Utility class; object creation is not required. */
    private ConfigReader() {
    }

    /**
     * Returns configuration by key.
     *
     * @param key property name
     * @return JVM system value when supplied, otherwise file value
     */
    public static String get(String key) {
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        return PROPERTIES.getProperty(key);
    }

    /** Returns selected browser. */
    public static String browser() {
        return get("browser");
    }

    /** Returns configured explicit-wait timeout in seconds. */
    public static int timeout() {
        return Integer.parseInt(get("timeout"));
    }

    /** Returns whether browser should run headlessly. */
    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    /** Returns selected environment name. */
    public static String environment() {
        return get("environment");
    }

    /**
     * Returns assignment base URL.
     *
     * <p>The user explicitly requested
     * https://automationexercise.com as the root base_url.</p>
     */
    public static String baseUrl() {
        String environmentUrl = get(environment() + ".base_url");

        if (environmentUrl != null && !environmentUrl.isBlank()) {
            return environmentUrl;
        }

        return get("base_url");
    }

    /** Returns TestNG thread count: Maven/System property > config.properties > default 2. */
    public static int getThreadCount() {
        String systemValue = System.getProperty("thread.count");
        if (systemValue != null && !systemValue.isBlank()) {
            return parsePositiveThreadCount(systemValue, "Maven/System property");
        }
        String configValue = PROPERTIES.getProperty("thread.count");
        if (configValue != null && !configValue.isBlank()) {
            return parsePositiveThreadCount(configValue, "config.properties");
        }
        return 2;
    }

    /** Parses and validates a positive thread count. */
    private static int parsePositiveThreadCount(String value, String source) {
        try {
            int threadCount = Integer.parseInt(value.trim());
            if (threadCount <= 0) {
                throw new IllegalArgumentException("thread.count from " + source + " must be greater than 0. Actual value: " + value);
            }
            return threadCount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid thread.count from " + source + ": " + value, e);
        }
    }



    /**
     * Returns the configured execution mode.
     *
     * <p>Priority is the same as other framework properties:</p>
     * <ol>
     *     <li>System/Maven property: {@code -Dexecution.mode=remote}</li>
     *     <li>{@code config.properties}: {@code execution.mode=local}</li>
     *     <li>Fallback: {@code local}</li>
     * </ol>
     *
     * @return normalized execution mode: local or remote
     */
    public static String getExecutionMode() {

        String value =
                System.getProperty(
                        "execution.mode"
                );

        if (value == null
                || value.isBlank()) {

            value =
                    PROPERTIES.getProperty(
                            "execution.mode",
                            "local"
                    );
        }

        value =
                value.trim()
                     .toLowerCase();

        if (!value.equals("local")
                && !value.equals("remote")) {

            throw new IllegalArgumentException(
                    "Unsupported execution.mode: "
                            + value
                            + ". Supported values: local, remote"
            );
        }

        return value;
    }

}