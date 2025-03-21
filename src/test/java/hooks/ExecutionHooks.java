package hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static managers.DriverManager.getDriver;
import static managers.DriverManager.quitDriver;

public class ExecutionHooks {
    //static WebDriver driver = DriverManager.getDriver();
    private static final Logger LOG = LogManager.getLogger(ExecutionHooks.class);

    @Before(order = 1, value = "@OpenBrowser")
    public static void setUp() {
        // Initialize the WebDriver before test
        getDriver();  // Use DriverManager to initialize WebDriver
        LOG.info("Opening the browser...");
    }

    @AfterAll
    public static void tearDown() {
        // Quit the WebDriver after test
        quitDriver();
        LOG.info("Closing the browser...");
    }
}
