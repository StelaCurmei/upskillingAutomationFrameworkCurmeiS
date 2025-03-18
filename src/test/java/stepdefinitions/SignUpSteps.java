package stepdefinitions;

import configreader.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.DataGeneratorManager;
import managers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.AddUserPage;
import pageobjects.LogInPage;

import java.time.Duration;


public class SignUpSteps {
    private static final Logger LOG = LogManager.getLogger(SignUpSteps.class);
    WebDriver driver = DriverManager.getDriver();
    ConfigReader configReader = new ConfigReader();
    AddUserPage addUserPage = new AddUserPage(driver);
    LogInPage logInPage = new LogInPage(driver);

    @Given("Log in page is accessed")
    public void accessSite() {
        String url = configReader.getProperty("logInUrl");
        LOG.info("Loaded URL:{}", url);
        LOG.info("Navigating to:{}", url);
        driver.get(url);
        String currentUrl = driver.getCurrentUrl();

        assert currentUrl != null;
        if (currentUrl.equals(configReader.getProperty("logInUrl"))) {
            LOG.info("Log in page is accessed: {}", currentUrl);
        } else {
            LOG.info("Test failed: Log in page is not loaded or a wrong URL was opened. Current URL is: {}", currentUrl);
        }
        Assert.assertEquals( "Test failed: Log in page is not loaded or a wrong URL was opened. Current URL is: {}", url, currentUrl);
    }

    @And("Sign Up button is clicked")
    public void clickSignUp() {
        logInPage.clickSignUp();
        String url = driver.getCurrentUrl();

        // Wait for the URL to change after clicking Sign Up
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("addUser")); // Adjust this based on actual URL

        String expectedUrl = configReader.getProperty("addUserUrl"); // Update if necessary
        String currentUrl = driver.getCurrentUrl();

        LOG.info("Current URL: {}", currentUrl);

        // Assertion to check if the URL is correct
        Assert.assertEquals("Test Failed: Incorrect Sign-Up URL loaded.", expectedUrl, currentUrl);
    }
    @When("Valid sign in data is entered")
    public void enterSignUpData() {
        addUserPage.setFirstName(DataGeneratorManager.getRandomFirstName());
        addUserPage.setLastName(DataGeneratorManager.getRandomLastName());
        addUserPage.setEmail(DataGeneratorManager.getRandomEmail());
        addUserPage.setPassword(DataGeneratorManager.getRandomPassword());
    }

    @And("Submit button is clicked")
    public void clickSubmitButton() {
        addUserPage.clickSubmit();
    }

    @Then("User is redirected to Contact list page")
    public void userIsRedirectedToContactListPage() {
        String expectedUrl = configReader.getProperty("contactListUrl");

        if (expectedUrl == null || expectedUrl.isEmpty()) {
            LOG.error("Expected contact list URL is missing in the configuration.");
            throw new IllegalArgumentException("Contact list URL is missing in the configuration file.");
        }

        // Wait until the URL changes to the expected URL
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        // Get the current URL after waiting
        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL: {}", currentUrl);

        // Assertion to ensure the redirection happened correctly
        Assert.assertEquals("Test Failed: Redirect didn't happen or a wrong URL was opened.", expectedUrl, currentUrl);

        LOG.info("Test Passed: Redirect successful to: {}", currentUrl);
    }
}
