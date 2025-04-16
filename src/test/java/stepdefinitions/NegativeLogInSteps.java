package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LogInPage;
import pageobjects.SignUpPage;
import utilities.CommonActions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class NegativeLogInSteps {

    private static final Logger LOG = LogManager.getLogger(NegativeLogInSteps.class);

    private CommonActions commonActions;
    private LogInPage loginPage;
    private SignUpPage signUpPage;
    private final org.openqa.selenium.WebDriver driver;

    public NegativeLogInSteps() {
        commonActions = new CommonActions();
        driver = commonActions.getDriver();

        // Initialize NegativeLogInSteps-specific page objects.
        loginPage = new LogInPage(driver); // Uncomment initialization of loginPage
        signUpPage = new SignUpPage(driver);
    }

    // Assume that your feature file uses the common step defined in LogInSteps or a separate step
    // for "log in page is accessed" so no need to define it here again.

    @When("the user attempts to log in with invalid credentials:")
    public void attemptInvalidLogin(DataTable dataTable) {
        List<Map<String, String>> invalidCases = dataTable.asMaps(String.class, String.class);

        // Process each invalid credential set from the data table
        for (Map<String, String> testCase : invalidCases) {
            String username = testCase.get("username");
            String password = testCase.get("password");
            String expectedError = testCase.get("expected_error_message");

            // Populate login form with invalid credentials
            loginPage.setEmail(username);
            loginPage.setPassword(password);

            // Submit the login form
            loginPage.clickSubmit();
            LOG.info("Attempting login with username: {} and password: {}", username, password);

            // Wait until the error message is visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorMessage")));

            // Retrieve the displayed error message from the page object
            String actualError = String.valueOf(loginPage.getErrorMessage());
            LOG.info("Expected error: '{}', Actual error: '{}'", expectedError, actualError);

            // Verify that the error message matches
            Assert.assertEquals("Error message did not match!", expectedError, actualError);

            // Optionally, refresh the page to reset for the next test case
            driver.navigate().refresh();

            // Wait until the page loads again (e.g., the email field is visible)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        }
    }

    @Then("the corresponding error message is displayed for each invalid attempt")
    public void verifyErrorMessages() {
        LOG.info("All invalid login attempts have been verified for correct error messages.");
    }
}
