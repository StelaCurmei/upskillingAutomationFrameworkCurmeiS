package configreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties PROPERTIES;

    public ConfigReader() {
        PROPERTIES = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            PROPERTIES.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}