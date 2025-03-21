package utils;

import configreader.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavigationHelper {
    private static final Logger LOG = LogManager.getLogger(NavigationHelper.class);
    private WebDriver driver;
    private WebDriverWait wait;
    private ConfigReader configReader;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.configReader = new ConfigReader();
    }


//      Verifies if the current URL matches the expected URL after a redirection.
//      @param expectedUrl The expected redirected URL.

    public void verifyRedirect(String urlPropertyKey) {
        try {
            // Fetch expected URL from config.properties
            String expectedUrl = configReader.getProperty(urlPropertyKey);

            // Wait until the URL matches the expected one
            wait.until(ExpectedConditions.urlToBe(expectedUrl));

            LOG.info("Successfully redirected to: {}", driver.getCurrentUrl());
        } catch (TimeoutException e) {
            LOG.error("Redirect timeout. Expected: {}, but got: {}", configReader.getProperty(urlPropertyKey), driver.getCurrentUrl());
        } catch (WebDriverException e) {
            LOG.error("WebDriver issue: {}", e.getMessage());
        }
    }
}
