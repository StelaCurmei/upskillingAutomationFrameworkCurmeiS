package stepdefinitions;

import configreader.ConfigReader;
import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.DataGeneratorManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
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
    LogInPage loginPage = new LogInPage(driver);
    AddUserPage addUserPage = new AddUserPage(driver);
    ConfigReader configReader = new ConfigReader();
    ContactListPage contactListPage = new ContactListPage(driver);


    @When("A new user signs up")
    public void signUpUser() {
        accessLoginPage();
        loginPage.clickSignUp();

        String firstName = DataGeneratorManager.getRandomFirstName();
        String lastName = DataGeneratorManager.getRandomLastName();
        String email = DataGeneratorManager.getRandomEmail();
        String password = DataGeneratorManager.getRandomPassword();

        addUserPage.signUp(firstName, lastName, email, password);
        addUserPage.clickSubmit();
        contactListPage.clickLogout();

        ScenarioContext.setScenarioContext(ContextKey.EMAIL,email);
        ScenarioContext.setScenarioContext(ContextKey.PASSWORD, password);
    }

    @And("The login page is accessed")
    public void accessLoginPage() {
        try {
            String logInPage = configReader.getProperty("logInUrl");
            LOG.info("Navigating to: {}", logInPage);

            driver.get(logInPage);
            LOG.info("Login page accessed successfully.");
        } catch (InvalidArgumentException e) {
            LOG.error("Invalid or missing URL. Make sure LogInUrl property has a correct value: {}", e.getMessage());
        }
    }

    @When("The user enters valid login data")
    public void enterValidLoginData() {
        // Retrieve the credentials stored in the shared context by SignUpSteps
        String email = ScenarioContext.getScenarioContext(ContextKey.EMAIL);
        String password = ScenarioContext.getScenarioContext(ContextKey.PASSWORD);

        // Use the credentials on the login page
        loginPage.setEmail(email);
        loginPage.setPassword(password); // Assuming your LogInPage has an overloaded setPassword(String) method
    }

    @And("The user clicks on the submit button")
    public void clicksSubmitButton() {
        // Click the login/submit button
        loginPage.clickSubmit();
    }

    @Then("The user is redirected to contact list page")
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