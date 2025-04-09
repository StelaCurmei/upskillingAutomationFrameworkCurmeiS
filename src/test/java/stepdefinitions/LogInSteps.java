package stepdefinitions;

import configreader.ConfigReader;
import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.DataGeneratorManager;
import managers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.AddUserPage;
import pageobjects.ContactListPage;
import pageobjects.LogInPage;

import java.time.Duration;


public class LogInSteps {

    private static final Logger LOG = LogManager.getLogger(LogInSteps.class);

    private WebDriver driver;
    private LogInPage loginPage;
    private AddUserPage addUserPage;
    ContactListPage contactListPage = new ContactListPage(driver);
    private ConfigReader configReader;

    // Cucumber requires a no-arg constructor if you're not using a DI framework
    public LogInSteps() {
        // 1. Initialize the WebDriver
        driver = DriverManager.getDriver();

        // 2. Initialize page objects
        loginPage = new LogInPage(driver);
        addUserPage = new AddUserPage(driver);
        contactListPage = new ContactListPage(driver);

        // 3. Initialize the ConfigReader (and any other utilities)
        configReader = new ConfigReader();
    }

    @Given("log in page is accessed")
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

    @And("a new user is signed up")
    public void signUp() {
        // Use your existing page object methods
        loginPage.clickSignUp();

        // Generate and fill in user data
        String firstName = DataGeneratorManager.getRandomFirstName();
        String lastName = DataGeneratorManager.getRandomLastName();
        String email = DataGeneratorManager.getRandomEmail();
        String password = DataGeneratorManager.getRandomPassword();
        System.out.println(firstName + " " + lastName + " " + email + " " + password);

        addUserPage.signUp(firstName, lastName, email, password);
        addUserPage.clickSubmit();
        contactListPage.clickLogout();

        // Store credentials in scenario context for later retrieval
        ScenarioContext.setScenarioContext(ContextKey.EMAIL, email);
        ScenarioContext.setScenarioContext(ContextKey.PASSWORD, password);
    }

    @When("the signed up user enters valid login data")
    public void enterValidLoginData() {
        // Retrieve credentials from your scenario context
        String email = ScenarioContext.getScenarioContext(ContextKey.EMAIL);
        String password = ScenarioContext.getScenarioContext(ContextKey.PASSWORD);

        // Create an explicit wait for the email element to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        // Populate the login form using your page object methods
        loginPage.setEmail(email);
        loginPage.setPassword(password);
    }

    @And("the user clicks on the submit button")
    public void clicksSubmitButton() {
        loginPage.clickSubmit();
    }

    @Then("the user is redirected to contact list page")
    public void userIsRedirectedToContactListPage() {
        String expectedUrl = configReader.getProperty("contactListUrl");
        if (expectedUrl == null || expectedUrl.isEmpty()) {
            LOG.error("Expected contact list URL is missing in the configuration.");
            throw new IllegalArgumentException("Contact list URL is missing in the configuration file.");
        }

        // Wait for the page to redirect
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        // Verify the URL
        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL: {}", currentUrl);
        Assert.assertEquals("Test Failed: Redirect didn't happen or a wrong URL was opened.", expectedUrl, currentUrl);

        LOG.info("Test Passed: Redirect successful to: {}", currentUrl);
    }
}