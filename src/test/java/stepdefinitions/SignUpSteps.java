package stepdefinitions;

import configreader.ConfigReader;
import io.cucumber.java.en.And;
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
import pageobjects.ContactListPage;
import pageobjects.LogInPage;
import pageobjects.SignUpPage;
import utils.NavigationHelper;

import java.time.Duration;

public class SignUpSteps {

    private static final Logger LOG = LogManager.getLogger(SignUpSteps.class);

    // Initialize WebDriver and other utilities.
    private utils.CommonActions commonActions;
    private WebDriver driver = DriverManager.getDriver();
    private LogInPage loginPage;
    private ConfigReader configReader = new ConfigReader();
    private final LogInPage logInPage = new LogInPage(driver);
    private SignUpPage signUpPage = new SignUpPage(driver);
    private ContactListPage contactListPage = new ContactListPage(driver);
    private final NavigationHelper navigationHelper = new NavigationHelper(driver);

    public SignUpSteps() {
        // Instantiate our helper that sets up the driver and configuration.
        commonActions = new utils.CommonActions();
        driver = commonActions.getDriver();
        configReader = commonActions.getConfigReader();

        // Initialize page objects using the driver from CommonActions.
        loginPage = new LogInPage(driver);
        signUpPage = new SignUpPage(driver);
        contactListPage = new ContactListPage(driver);
    }

    @When("the user clicks the Sign Up button")
    public void clickSignUpButton() {
        // Click the SignUp button on the login page.
        logInPage.clickSignUp();

        // Verify the redirect (using a helper method and a key from configuration).
        navigationHelper.verifyRedirect("addUserUrl");

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL after clicking Sign Up: {}", currentUrl);
    }

    @When("the user enters valid sign up data")
    public void enterValidSignUpData() {
        // Generate random test data for signup.
        String firstName = DataGeneratorManager.getRandomFirstName();
        String lastName = DataGeneratorManager.getRandomLastName();
        String email = DataGeneratorManager.getRandomEmail();
        String password = DataGeneratorManager.getRandomPassword();

        LOG.info("Entering sign up data: {} {} {} {}", firstName, lastName, email, password);

        // Fill in the sign-up form.
        signUpPage.setFirstName(firstName);
        signUpPage.setLastName(lastName);
        signUpPage.setEmail(email);
        signUpPage.setPassword(password);
    }

    @And("the user clicks the Submit button")
    public void clickSubmitButton() {
        signUpPage.clickSubmit();
    }

    @Then("the user is redirected to the Contact List page")
    public void verifyRedirectionToContactListPage() {
        String expectedUrl = configReader.getProperty("contactListUrl");
        if (expectedUrl == null || expectedUrl.isEmpty()) {
            LOG.error("Expected contact list URL is missing in the configuration.");
            throw new IllegalArgumentException("Contact list URL is missing in the configuration file.");
        }

        // Wait until redirection to the expected URL completes.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL: {}", currentUrl);
        Assert.assertEquals("Redirection failed: the current URL does not match the expected URL.", expectedUrl, currentUrl);
        LOG.info("Test Passed: User successfully redirected to: {}", currentUrl);
    }
}
