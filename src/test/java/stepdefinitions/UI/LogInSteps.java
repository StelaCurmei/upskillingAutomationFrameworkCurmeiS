package stepdefinitions.UI;

import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
import utils.ConfigReader;
import utils.DataGenerator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogInSteps {
    private static final Logger LOG = LogManager.getLogger(LogInSteps.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final LogInPage loginPage;
    private final SignUpPage signUpPage;
    private final ContactListPage contactListPage;

    public LogInSteps() {
        CommonActions actions = new CommonActions();
        this.driver = actions.getDriver();
        this.wait = new WebDriverWait(driver, TIMEOUT);

        this.loginPage = new LogInPage(driver);
        this.signUpPage = new SignUpPage(driver);
        this.contactListPage = new ContactListPage(driver);
    }

    // Utility methods
    private void waitForVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitForUrl(String url) {
        wait.until(ExpectedConditions.urlToBe(url));
    }

    private void fillLoginForm(String email, String password) {
        waitForVisible(By.id("email"));
        waitForVisible(By.id("password"));
        loginPage.setEmail(email);
        loginPage.setPassword(password);
    }

    @And("a new user is signed up")
    public void signUp() {
        loginPage.clickSignUp();

        String firstName = DataGenerator.getRandomFirstName();
        String lastName = DataGenerator.getRandomLastName();
        String email = DataGenerator.getRandomEmail();
        String password = DataGenerator.getRandomPassword();

        LOG.info("Signing up new user: {} {}", firstName, lastName);
        signUpPage.signUp(firstName, lastName, email, password);
        contactListPage.clickLogout();

        ScenarioContext.setScenarioContext(ContextKey.EMAIL, email);
        ScenarioContext.setScenarioContext(ContextKey.PASSWORD, password);
    }

    @When("the signed up user enters valid login data")
    public void enterValidLoginData() {
        String email = ScenarioContext.getScenarioContext(ContextKey.EMAIL);
        String password = ScenarioContext.getScenarioContext(ContextKey.PASSWORD);
        fillLoginForm(email, password);
    }

    @And("the user clicks on the submit button")
    public void clicksSubmitButton() {
        loginPage.clickSubmit();
    }

    @Then("the user is redirected to contact list page")
    public void userIsRedirectedToContactListPage() {
        String expectedUrl = ConfigReader.getProperty("contactListUrl");
        if (expectedUrl == null || expectedUrl.isEmpty()) {
            throw new IllegalStateException("Missing 'contactListUrl' in configuration.");
        }

        waitForUrl(expectedUrl);
        String currentUrl = driver.getCurrentUrl();

        LOG.info("Current URL: {}", currentUrl);
        Assert.assertEquals("Redirect to Contact List failed.", expectedUrl, currentUrl);
        LOG.info("Redirect successful.");
    }

    @When("the user attempts to log in with invalid credentials:")
    public void attemptInvalidLogin(DataTable table) {
        List<Map<String, String>> cases = table.asMaps();
        List<String> actualErrors = new ArrayList<>();

        for (Map<String, String> data : cases) {
            fillLoginForm(data.get("email"), data.get("password"));
            loginPage.clickSubmit();
            LOG.info("Attempting invalid login: {}", data);

            waitForVisible(By.id("error"));
            actualErrors.add(loginPage.getMessageText());

            loginPage.clearEmail();
            loginPage.clearPassword();
        }

        ScenarioContext.setScenarioContext(ContextKey.NEGATIVE_ACTUAL_ERRORS, actualErrors);
    }

    @Then("the system displays an error message for each set of credentials")
    public void verifyErrorMessages(DataTable table) {
        List<String> expected = new ArrayList<>();
        table.asMaps().forEach(row -> expected.add(row.get("expected_error_message")));

        List<String> actual = ScenarioContext.getScenarioContext(ContextKey.NEGATIVE_ACTUAL_ERRORS);
        Assert.assertEquals("Error messages mismatch.", expected, actual);
        LOG.info("Verified all invalid login error messages.");
    }
}
