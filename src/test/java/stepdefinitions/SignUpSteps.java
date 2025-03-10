package stepdefinitions;

import configreader.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.DataGeneratorManager;
import managers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.AddUserPage;
import pageobjects.LogInPage;

import java.time.Duration;

public class SignUpSteps {
    WebDriver driver = DriverManager.getDriver();
    ConfigReader configReader = new ConfigReader();
    AddUserPage addUserPage = new AddUserPage(driver);
    LogInPage logInPage = new LogInPage(driver);

    @Given("Log in page is accessed")
    public void accessSite() {
        String url = configReader.getProperty("logInUrl");

        System.out.println("Loaded URL: " + configReader.getProperty("logInUrl"));

        System.out.println("Navigating to: " + url);  // ✅ Debugging output
        driver.get(url);
        System.out.println("Log in page is accessed");
    }
    @And("Sign Up button is clicked")
    public void clickSignUp() {
        logInPage.clickSignup();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
    }

    @When("Valid sign in data is entered")
    public void enterData() {
        addUserPage.setFirstName(DataGeneratorManager.getRandomFirstName());
        addUserPage.setLastName(DataGeneratorManager.getRandomLastName());
        addUserPage.setEmail(DataGeneratorManager.getRandomEmail());
        addUserPage.setPassword(DataGeneratorManager.getRandomPassword());
    }

    @And("Submit button is clicked")
    public void submitButtonIsClicked() {
        addUserPage.clickSubmit();
    }
    @Then("User is redirected to Contact list page")
    public void userIsRedirectedToContactListPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Timeout after 10 seconds
        wait.until(ExpectedConditions.urlToBe(configReader.getProperty("contactListUrl")));

        String currentUrl = driver.getCurrentUrl();
        assert currentUrl != null;
        if (currentUrl.equals(configReader.getProperty("contactListUrl"))) {
            System.out.println("Test Passed: Redirect successful to " + configReader.getProperty("contactListUrl"));
        } else {
            System.out.println("Test Failed: Redirect did not happen or went to a wrong URL. Current URL: " + currentUrl);
        }
    }
}
