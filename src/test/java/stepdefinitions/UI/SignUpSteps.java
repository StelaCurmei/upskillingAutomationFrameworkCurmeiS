package stepdefinitions.UI;

import utils.ConfigReader;
import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.SignUpPage;
import utils.CommonActions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SignUpSteps {
    private static final Logger LOG = LogManager.getLogger(SignUpSteps.class);
    private static final int TIMEOUT = 10;

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final SignUpPage    signUpPage;

    public SignUpSteps() {
        CommonActions common = new CommonActions();
        this.driver     = common.getDriver();
        this.wait       = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        //this.config     = common.getConfigReader();
        this.signUpPage = new SignUpPage(driver);
    }

    // Positive Scenario

    @When("the user enters valid sign up data")
    public void enterValidSignUpData() {
        signUpPage.setFirstName(DataGenerator.getRandomFirstName());
        signUpPage.setLastName (DataGenerator.getRandomLastName());
        signUpPage.setEmail    (DataGenerator.getRandomEmail());
        signUpPage.setPassword (DataGenerator.getRandomPassword());
    }

    @And("the user clicks the Submit button")
    public void clickSubmitButton() {
        signUpPage.clickSubmit();
    }

    @Then("the user is redirected to the Contact List page")
    public void verifyRedirectionToContactListPage() {
        String expected = ConfigReader.getProperty("contactListUrl");
        wait.until(ExpectedConditions.urlToBe(expected));
        Assert.assertEquals("Redirect after signup failed", expected, driver.getCurrentUrl());
    }

    // Negative Scenario

    @When("the user attempts to sign in with invalid data:")
    public void attemptInvalidSignUp(DataTable table) {
        List<List<String>> allFragments = table.asMaps(String.class, String.class)
                .stream()
                .map(HashMap::new)
                .map(this::submitParseAndReset)
                .collect(Collectors.toList());

        ScenarioContext.setScenarioContext(ContextKey.NEGATIVE_EXPECTED_ERRORS, allFragments);
    }

    @Then("the system displays an error message for each set of invalid data")
    public void verifySignUpErrorMessages(DataTable expectedTable) {
        Object raw = ScenarioContext.getScenarioContext(ContextKey.NEGATIVE_EXPECTED_ERRORS);
        @SuppressWarnings("unchecked")
        List<List<String>> actual = (List<List<String>>) raw;

        List<List<String>> expected = expectedTable.asMaps(String.class, String.class)
                .stream()
                .map(m -> parseErrorMessages(m.get("expected_error_message")))
                .collect(Collectors.toList());

        Assert.assertEquals("Row count mismatch", expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(
                    "Mismatch on row #" + (i + 1),
                    expected.get(i),
                    actual.get(i)
            );
        }
    }

    // Helpers

    private List<String> submitParseAndReset(Map<String, String> row) {
        row.replaceAll((k, v) -> v == null ? "" : v);

        signUpPage.setFirstName(row.get("firstname"));
        signUpPage.setLastName (row.get("lastname"));
        signUpPage.setEmail    (row.get("email"));
        signUpPage.setPassword (row.get("password"));
        signUpPage.clickSubmit();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
        String banner = signUpPage.getSignUpErrorMessage();
        LOG.info("Captured banner: {}", banner);

        // Refresh
        driver.navigate().refresh();
        wait.until(ExpectedConditions.attributeToBe(By.id("firstName"), "value", ""));
        return parseErrorMessages(banner);
    }

    private List<String> parseErrorMessages(String banner) {
        String body = banner.replaceFirst("^[^:]+:\\s*", "");
        return List.of(body.split(", (?=[a-zA-Z]+:)"));
    }
}
