package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static utils.DriverManager.getDriver;
import static utils.DriverManager.quitDriver;

public class ExecutionHooks {
    private static final Logger LOG = LogManager.getLogger(ExecutionHooks.class);

    // Logger hook: Set logging context before scenario using the @Logger tag
    @Before(order = 0)
    public void setupLogger(Scenario scenario) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Sanitize scenario name to use as folder name for logs
        String testName = scenario.getName().replaceAll("[^a-zA-Z0-9\\-_]", "_");
        ThreadContext.put("testTime", timeStamp);
        ThreadContext.put("testName", testName);
        LOG.info("Executing the scenario: " + scenario.getName());
    }

    // UI hook: Setup WebDriver before scenario using the @Ui tag
    @Before(order = 1, value = "@Ui")
    public void setUp() {
        getDriver();  // Initialize WebDriver using your DriverManager
        LOG.info("Opening the browser...");
    }

    // UI hook: Teardown WebDriver after scenario using the @Ui tag
    @After(order = 1, value = "@Ui")
    public void tearDown() {
        quitDriver(); // Quit WebDriver using your DriverManager
        LOG.info("Closing the browser...");
    }

    // Logger hook: Clear logging context after scenario using the @Logger tag
    @After(order = 0)
    public void clearLogger(Scenario scenario) {
        ThreadContext.get("testTime");
        ThreadContext.get("testName");
        LOG.info("Clearing logger context for test: " + scenario.getName());
        ThreadContext.clearAll(); // Clear ThreadContext to prevent data leakage between tests
    }
}
