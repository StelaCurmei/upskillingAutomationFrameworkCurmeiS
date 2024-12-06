package online.upskillingtests.managers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.sql.SQLOutput;

public class DriverManager {
    private static String webDriverType = "Chrome";
    private static DriverManager instance;
    private WebDriver driver;

    private DriverManager(){
        switch (webDriverType.toUpperCase()){
            case "CHROME":
                driver = new ChromeDriver();
                System.out.println("The CHROME driver was initiated!");
                break;
            case "EDGE":
                driver = new EdgeDriver();
                System.out.println("The EDGE driver was initiated!");
                break;
            case "FIREFOX":
                driver = new FirefoxDriver();
                System.out.println("The Firefox driver was initiated!");
                break;
            case "SAFARI":
                driver = new SafariDriver();
                System.out.println("The Safari driver was initiated!");
                break;
            default:
                System.out.println("The webDriverType " + webDriverType + "is not defined!");
        }
    };
public static DriverManager getInstance(){
    if (instance == null){
        instance = new DriverManager();
    }
    return instance;
}
public WebDriver getDriver(){
    if(driver == null){
        getInstance();
    }
    return driver;
    }
}
