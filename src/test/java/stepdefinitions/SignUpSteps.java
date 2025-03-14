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
        LOG.info("Log in page is accessed.");
    }

    @And("Sign Up button is clicked")
    public void clickSignUp() {
        logInPage.clickSignUp();

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL:{}", currentUrl);
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Timeout after 10 seconds
        wait.until(ExpectedConditions.urlToBe(configReader.getProperty("contactListUrl")));

        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        if (currentUrl.equals(configReader.getProperty("contactListUrl"))) {
            LOG.info("Test Passed: Redirect successful to: {}", currentUrl);
        } else {
            LOG.info("Test failed: Redirect didn't happen or a wrong URL was opened. Current URL is: {}", currentUrl);
        }
    }
}
