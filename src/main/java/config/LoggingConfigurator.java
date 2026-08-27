package config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Locale;

/**
 * Applies {@code LOG_LEVEL} to the root logger once at startup. The root level
 * in {@code log4j2.xml} is only a bootstrap value; this replaces it.
 */
public final class LoggingConfigurator {

    private static final Logger LOG = LogManager.getLogger(LoggingConfigurator.class);

    private static final Level DEFAULT_LEVEL = Level.INFO;

    private static boolean applied;

    private LoggingConfigurator() {
    }

    /**
     * Idempotent: called from both the suite listener and the test base class,
     * because {@code -Dtest=...} bypasses the suite file and its listeners.
     */
    public static synchronized Level apply() {
        String configured = ConfigReader.logLevel();
        Level level = Level.getLevel(configured.toUpperCase(Locale.ROOT));

        if (level == null) {
            LOG.warn("LOG_LEVEL '{}' is not a recognised level - falling back to {}. "
                            + "Valid values: TRACE, DEBUG, INFO, WARN, ERROR.",
                    configured, DEFAULT_LEVEL);

            level = DEFAULT_LEVEL;
        }

        if (!applied) {
            Configurator.setRootLevel(level);
            applied = true;

            LOG.info("Log level set to {} from LOG_LEVEL.", level);
        }

        return level;
    }
}
