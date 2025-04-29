package stepdefinitions;

import configreader.ConfigReader;
import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.DataGeneratorManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.ContactListPage;
import pageobjects.LogInPage;
import pageobjects.SignUpPage;
import utils.CommonActions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogInSteps {

    private static final Logger LOG = LogManager.getLogger(LogInSteps.class);

    private CommonActions commonActions;
    private WebDriver driver;
    private ConfigReader configReader;
    private LogInPage loginPage;
    private SignUpPage signUpPage;
    private ContactListPage contactListPage;

    public LogInSteps() {
        // Instantiate our helper that sets up the driver and configuration.
        commonActions = new CommonActions();
        driver = commonActions.getDriver();
        configReader = commonActions.getConfigReader();

        // Initialize page objects using the driver from CommonActions.
        loginPage = new LogInPage(driver);
        signUpPage = new SignUpPage(driver);
        contactListPage = new ContactListPage(driver);
    }

    // -------------------------------------------------
    // Positive Scenario Steps (unchanged)
    // -------------------------------------------------

    @And("a new user is signed up")
    public void signUp() {
        loginPage.clickSignUp();

        String firstName = DataGeneratorManager.getRandomFirstName();
        String lastName = DataGeneratorManager.getRandomLastName();
        String email = DataGeneratorManager.getRandomEmail();
        String password = DataGeneratorManager.getRandomPassword();

        LOG.info("Signing up new user: {} {} {} {}", firstName, lastName, email, password);

        signUpPage.signUp(firstName, lastName, email, password);
        contactListPage.clickLogout();

        ScenarioContext.setScenarioContext(ContextKey.EMAIL, email);
        ScenarioContext.setScenarioContext(ContextKey.PASSWORD, password);
    }

    @When("the signed up user enters valid login data")
    public void enterValidLoginData() {
        String email = ScenarioContext.getScenarioContext(ContextKey.EMAIL);
        String password = ScenarioContext.getScenarioContext(ContextKey.PASSWORD);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL: {}", currentUrl);
        Assert.assertEquals("Test Failed: Redirect didn't happen or a wrong URL was opened.", expectedUrl, currentUrl);
        LOG.info("Test Passed: Redirect successful to: {}", currentUrl);
    }

    // -------------------------------------------------
    // Negative Scenario Steps (Refactored)
    // -------------------------------------------------

    // Negative Scenario Steps (Refactored to clear fields)

    @When("the user attempts to log in with invalid credentials:")
    public void attemptInvalidLogin(DataTable dataTable) {
        // Convert the DataTable to a list of maps.
        List<Map<String, String>> invalidCases = dataTable.asMaps(String.class, String.class);

        // Prepare lists to store expected and actual error messages.
        //List<String> expectedErrors = new ArrayList<>();
        List<String> actualErrors = new ArrayList<>();

        // Create a WebDriverWait instance with a suitable timeout.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Iterate over each invalid credential set.
        for (Map<String, String> testCase : invalidCases) {
            String email = testCase.get("email");
            String password = testCase.get("password");
            //String expectedError = testCase.get("expected_error_message");

            // Populate the login form with invalid credentials.
            loginPage.setEmail(email);
            loginPage.setPassword(password);

            // Submit the login form.
            loginPage.clickSubmit();
            LOG.info("Attempting login with username: '{}' and password: '{}'", email, password);

            // Wait until the error message element is visible.
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

            // Retrieve the displayed error message.
            String actualError = loginPage.getMessageText();

            // Store the expected and actual error messages.
            actualErrors.add(actualError);

            // Clear the input fields (assumes your loginPage has these methods) to prepare for the next test case.
            loginPage.clearEmail();
            loginPage.clearPassword();
        }

        // Store the lists in ScenarioContext for verification in the @Then step.
        ScenarioContext.setScenarioContext(ContextKey.NEGATIVE_ACTUAL_ERRORS, actualErrors);
    }

    @Then("the system displays an error message for each set of credentials")
    public void verifyErrorMessages(DataTable dataTable) {
        List<Map<String, String>> messages = dataTable.asMaps(String.class, String.class);
        // Retrieve stored expected and actual error messages.
        List<String> expectedErrors = new ArrayList<>();
        List<String> actualErrors = ScenarioContext.getScenarioContext(ContextKey.NEGATIVE_ACTUAL_ERRORS);

        // Iterate over each error message
        for (Map<String, String> testCase : messages) {
            expectedErrors.add(testCase.get("expected_error_message"));
        }
        Assert.assertEquals("Error message mismatch for credential set " , expectedErrors, actualErrors);
        LOG.info("All invalid login attempts have been verified for correct error messages.");
    }
}
