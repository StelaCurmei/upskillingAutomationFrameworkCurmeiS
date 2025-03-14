package managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverManager {
    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static String webDriverType = "Chrome";
    private static DriverManager instance;
    private WebDriver driver;

    private DriverManager() {
        switch (webDriverType.toUpperCase()) {
            case "CHROME":
                driver = new ChromeDriver();
                LOG.info("The CHROME driver was initiated.");
                break;
            case "EDGE":
                driver = new EdgeDriver();
                LOG.info("The EDGE driver was initiated.");
                break;
            case "FIREFOX":
                driver = new FirefoxDriver();
                LOG.info("The Firefox driver was initiated.");
                break;
            case "SAFARI":
                driver = new SafariDriver();
                LOG.info("The Safari driver was initiated.");
                break;
            default:
                LOG.info("The webDriverType is not defined: {}", webDriverType);
        }
        assert driver != null;
        driver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance.driver;
    }

//    public static void quitDriver() {
//        if (instance != null && instance.driver != null) {
//            instance.driver.quit();
//            instance = null;
//        }
//    }
}
