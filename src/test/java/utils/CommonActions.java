package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

public class CommonActions {
    private static final Logger LOG = LogManager.getLogger(CommonActions.class);
    private WebDriver driver;
    private ConfigReader configReader;

    public CommonActions() {
        driver = DriverManager.getDriver();
        configReader = new ConfigReader();
    }

    public void accessSite() {
        try {
            String url = configReader.getProperty("logInUrl");
            LOG.info("Navigating to: {}", url);
            driver.get(url);
            LOG.info("Login page accessed successfully.");
        } catch (InvalidArgumentException e) {
            LOG.error("Invalid or missing URL. Ensure logInUrl property is set correctly: {}", e.getMessage());
            throw e;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public ConfigReader getConfigReader() {
        return configReader;
    }
}
