package stepdefinitions;

import configreader.ConfigReader;
import io.cucumber.java.en.Given;
import managers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;

public class CommonSteps {
    private static final Logger LOG = LogManager.getLogger(CommonSteps.class);
    protected WebDriver driver;
    protected ConfigReader configReader;

    public CommonSteps() {
        driver = DriverManager.getDriver();
        configReader = new ConfigReader();
    }

    @Given("log in page is accessed")
    public void accessSite1() {
        try {
            String url = configReader.getProperty("logInUrl");
            LOG.info("Navigating to: {}", url);
            driver.get(url);
            LOG.info("Login page accessed successfully.");
        } catch (InvalidArgumentException e) {
            LOG.error("Invalid or missing URL. Make sure LogInUrl property has a correct value: {}", e.getMessage());
            throw e;
        }
    }
}
