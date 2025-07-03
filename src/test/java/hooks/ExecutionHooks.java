package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import utils.BaseApi;
import utils.ConfigReader;
import utils.DriverManager;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * Cucumber hooks for API and UI scenarios.
 * - Sets up ThreadContext for per-scenario logging
 * - Configures RestAssured filters and attaches HTTP logs
 * - Manages WebDriver lifecycle and screenshots for UI
 */
public class ExecutionHooks {
    private static final Logger LOG = LogManager.getLogger(ExecutionHooks.class);

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String safeName = scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_");
        ThreadContext.put("testTime", timestamp);
        ThreadContext.put("testName", safeName);
        LOG.info("=== STARTING SCENARIO: {} ===", scenario.getName());
    }

    @Before(order = 1, value = "@api")
    public void setupApi() {
        // Configure RestAssured for API tests
        RestAssured.baseURI = ConfigReader.getProperty("api.baseUrl");
        LOG.info("Configured RestAssured for API tests. BaseURI={}", RestAssured.baseURI);
    }

    @AfterStep(order = 1, value = "@api")
    public void attachApiLogs(Scenario scenario) {
        String httpLogs = BaseApi.pullLogs();
        if (!httpLogs.isEmpty()) {
            scenario.attach(httpLogs, "text/plain", "API Request/Response");
            LOG.debug("Attached API logs to report");
        }
    }

    @After(order = 0, value = "@api")
    public void teardownApi() {
        // Clear filters to avoid bleed between scenarios
        BaseApi.clearFilters();
        LOG.info("Cleared RestAssured filters after API test.");
    }

    @Before(order = 1, value = "@Ui")
    public void setupUi() {
        DriverManager.getDriver();
        LOG.info("Launched browser for UI scenario");
    }

    @AfterStep(order = 2, value = "@Ui")
    public void afterEachUiStep(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "UI Step Screenshot");
            Allure.addAttachment("Screenshot - " + scenario.getName(),
                    new ByteArrayInputStream(screenshot));
            LOG.info("Captured UI screenshot for scenario: {}", scenario.getName());
        }
    }

    @After(order = 1, value = "@Ui")
    public void teardownUi() {
        DriverManager.quitDriver();
        LOG.info("Closed browser after UI scenario");
    }

    @After(order = -1)
    public void afterScenario(Scenario scenario) {
        LOG.info("=== ENDING SCENARIO: {} ===", scenario.getName());
        ThreadContext.clearAll();
    }
}
