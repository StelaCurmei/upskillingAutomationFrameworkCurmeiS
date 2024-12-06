package online.upskillingtests;

import online.upskillingtests.managers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class TestRunner {
   public static void main(String[] args) throws InterruptedException {
       WebDriver driver = DriverManager.getInstance().getDriver();

       String currentTabName = driver.getWindowHandle();
       Thread.sleep(5000);
       driver.switchTo().newWindow(WindowType.TAB);
       driver.get("https://thinking-tester-contact-list.herokuapp.com/");
       Thread.sleep(10000);
       driver.close();

       driver.switchTo().window(currentTabName);
       driver.get("https://thinking-tester-contact-list.herokuapp.com/addUser");
       Thread.sleep(10000);
       driver.quit();

       System.out.println("The test is finished and the driver is closed");

   }
}