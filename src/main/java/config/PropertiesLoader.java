package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads a properties file from the classpath. A system property always wins
 * over the file value, so any key can be overridden with {@code -D}.
 */
final class PropertiesLoader {

    private final String resourceName;
    private final Properties properties = new Properties();

    /**
     * Fails fast on a missing file, so a misconfigured build does not surface
     * later as a confusing {@code null} mid-test.
     */
    PropertiesLoader(String resourceName) {
        this.resourceName = resourceName;

        try (InputStream input = PropertiesLoader.class.getClassLoader()
                .getResourceAsStream(resourceName)) {

            if (input == null) {
                throw new IllegalStateException(resourceName + " was not found on the classpath.");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load " + resourceName + ".", e);
        }
    }

    String get(String key) {
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        return properties.getProperty(key);
    }

    String getRequired(String key) {
        String value = get(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required property '" + key + "' in " + resourceName + ".");
        }

        return value;
    }
}
