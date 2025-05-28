package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ByteArrayInputStream;

import static utils.DriverManager.getDriver;
import static utils.DriverManager.quitDriver;

public class ExecutionHooks {
    private static final Logger LOG = LogManager.getLogger(ExecutionHooks.class);

    // 1) Logger setup before any scenario
    @Before(order = 0)
    public void setupLogger(Scenario scenario) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String testName  = scenario.getName().replaceAll("[^a-zA-Z0-9\\-_]", "_");
        ThreadContext.put("testTime",  timeStamp);
        ThreadContext.put("testName",  testName);
        LOG.info("Executing the scenario: " + scenario.getName());
    }

    // 2) WebDriver setup before UI scenarios
    @Before(order = 1, value = "@Ui")
    public void setUp() {
        getDriver();
        LOG.info("Opening the browser...");
    }

    // 3) Screenshot after EVERY STEP in UI scenarios
    @AfterStep(order = 2, value = "@Ui")
    public void afterEachStep(Scenario scenario) {
        WebDriver driver = getDriver();
        if (driver == null) {
            return;
        }

        // grab screenshot bytes
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        // attach to Cucumber report
        scenario.attach(png, "image/png", "Step: " + scenario.getName());

        // attach to Allure
        Allure.addAttachment(
                "Screenshot - " + scenario.getName(),
                new ByteArrayInputStream(png)
        );

        LOG.info("Attached screenshot for step in scenario: " + scenario.getName());
    }

    // 4) WebDriver teardown after UI scenarios
    @After(order = 1, value = "@Ui")
    public void tearDown() {
        quitDriver();
        LOG.info("Closing the browser...");
    }

    // 5) Logger cleanup after any scenario
    @After(order = 0)
    public void clearLogger(Scenario scenario) {
        LOG.info("Clearing logger context for test: " + scenario.getName());
        ThreadContext.clearAll();
    }
}
