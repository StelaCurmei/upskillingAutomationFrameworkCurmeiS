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
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LogInPage;
import pageobjects.SignUpPage;
import utils.NavigationHelper;

import java.time.Duration;


public class SignUpSteps {
    private static final Logger LOG = LogManager.getLogger(SignUpSteps.class);
    WebDriver driver = DriverManager.getDriver();
    ConfigReader configReader = new ConfigReader();
    SignUpPage signUpPage = new SignUpPage(driver);
    LogInPage logInPage = new LogInPage(driver);
    NavigationHelper navigationHelper = new NavigationHelper(driver);

    @Given("Log in page is accessed")
    public void accessSite() {
        try {
            String url = configReader.getProperty("logInUrl");
            LOG.info("Navigating to: {}", url);

            driver.get(url);
            LOG.info("Login page accessed successfully.");
        } catch (InvalidArgumentException e) {
            LOG.error("Invalid or missing URL. Make sure LogInUrl property has a correct value: {}", e.getMessage());
        }
    }


    @And("Sign Up button is clicked")
    public void clickSignUp() {
        logInPage.clickSignUp();
        navigationHelper.verifyRedirect("addUserUrl");

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL is:{}", currentUrl);
    }

    @When("Valid sign in data is entered")
    public void enterSignUpData() {
        // Generate and set user data
        String email = DataGeneratorManager.getRandomEmail();
        String password = DataGeneratorManager.getRandomPassword();

        signUpPage.setFirstName(DataGeneratorManager.getRandomFirstName());
        signUpPage.setLastName(DataGeneratorManager.getRandomLastName());
        signUpPage.setEmail(email);
        signUpPage.setPassword(password);
    }

    @And("Submit button is clicked")
    public void clickSubmitButton() {
        signUpPage.clickSubmit();
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