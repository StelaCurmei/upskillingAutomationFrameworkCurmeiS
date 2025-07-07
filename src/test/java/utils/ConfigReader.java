package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPS = new Properties();

    // static init: load from classpath
    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (is == null) {
                throw new RuntimeException(
                        CONFIG_FILE + " not found on classpath"
                );
            }

            PROPS.load(is);

        } catch (IOException e) {
            // stop everything early if config can’t be read
            throw new ExceptionInInitializerError(
                    "Failed to load " + CONFIG_FILE + ": " + e.getMessage()
            );
        }
    }

    // no instantiation
    public ConfigReader() {
    }

    // Get a property value, or null if it doesn't exist.

    public static String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    // Get a property value, or return defaultValue if it doesn't exist.

    public static String getProperty(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }
}
