package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import managers.DriverManager;
import org.openqa.selenium.WebDriver;

public class ExecutionHooks {
    static WebDriver driver = DriverManager.getDriver();

    @Before(order = 1, value = "@OpenBrowser")
    public static void setUp() {
        // Initialize the WebDriver before test
        driver = DriverManager.getDriver();  // Use DriverManager to initialize WebDriver
        System.out.println("Opening the browser...");
    }

    @AfterAll
    public static void tearDown() {
        // Quit the WebDriver after test
        if (driver != null) {
            driver.quit();  // Close the browser
            System.out.println("Closing the browser...");
        }
    }
}