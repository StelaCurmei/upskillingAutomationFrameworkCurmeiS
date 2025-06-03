package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import io.restassured.filter.Filter;
import java.util.Date;

import static utils.DriverManager.getDriver;
import static utils.DriverManager.quitDriver;
import utils.ConfigReader;

public class ExecutionHooks {
    private static final Logger LOG = LogManager.getLogger(ExecutionHooks.class);

    // Logger setup before any scenario
    @Before(order = 0)
    public void setupLogger(Scenario scenario) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String testName  = scenario.getName().replaceAll("[^a-zA-Z0-9\\-_]", "_");
        ThreadContext.put("testTime",  timeStamp);
        ThreadContext.put("testName",  testName);
        LOG.info("=== STARTING SCENARIO: {} ===", scenario.getName());
    }

    // setup before API scenarios
    @Before(order = 1, value = "@api")
    public void setupRestAssured() {
        String baseUri = ConfigReader.getProperty("petStore.baseUri");
        RestAssured.baseURI = baseUri;
        // log request/response if validation fails
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        LOG.info("RestAssured configured for API tests. BaseURI={}", baseUri);
    }

    // WebDriver setup before UI scenarios
    @Before(order = 1, value = "@Ui")
    public void setUpUi() {
        getDriver();
        LOG.info("Opening the browser for UI test...");
    }

    // Screenshot after EVERY STEP - UI scenarios
    @AfterStep(order = 2, value = "@Ui")
    public void afterEachStepUi(Scenario scenario) {
        WebDriver driver = getDriver();
        if (driver == null) return;

        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(png, "image/png", "Step: " + scenario.getName());
        Allure.addAttachment("Screenshot - " + scenario.getName(),
                new ByteArrayInputStream(png));
        LOG.info("Attached screenshot for UI step in scenario: {}", scenario.getName());
    }

    // WebDriver teardown after UI scenarios
    @After(order = 1, value = "@Ui")
    public void tearDownUi() {
        quitDriver();
        LOG.info("Browser closed for UI test.");
    }

    // cleanup after API scenarios
    @After(order = 0, value = "@api")
    public void tearDownApi() {
        RestAssured.replaceFiltersWith(Collections.<Filter>emptyList());
        LOG.info("RestAssured filters cleared after API test.");
    }

    // Logger cleanup after any scenario
    @After(order =  -1)
    public void clearLogger(Scenario scenario) {
        LOG.info("=== ENDING SCENARIO: {} ===", scenario.getName());
        ThreadContext.clearAll();
    }
}
