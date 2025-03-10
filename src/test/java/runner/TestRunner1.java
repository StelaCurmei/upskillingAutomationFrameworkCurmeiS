//package runner;
//
//import online.upskillingtests.ConfigReader;
//import managers.DataGeneratorManager;
//import online.upskillingtests.managers.DriverManager;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.WindowType;
//
//public class TestRunner {
//    static ConfigReader reader = new ConfigReader();
//   public static void main(String[] args) throws InterruptedException {
//       WebDriver driver = DriverManager.getInstance().getDriver();
//
//       String currentTabName = driver.getWindowHandle();
//       driver.switchTo().newWindow(WindowType.TAB);
//       driver.get(reader.getProperty("logInUrl"));
//       System.out.println("The current URL is: " + driver.getCurrentUrl());
//       System.out.println("The current page title is: " + driver.getTitle());
//
//       WebElement SignUpButton = driver.findElement(By.xpath("//button[@id='signup']"));
//       SignUpButton.click();
//       System.out.println("The current URL is: " + driver.getCurrentUrl());
//       System.out.println("The current page title is: " + driver.getTitle());
//
//       WebElement firstNameInput = driver.findElement(By.id("firstName"));
//       firstNameInput.sendKeys("Abc");
//       WebElement lastNameInput = driver.findElement(By.id("lastName"));
//       lastNameInput.sendKeys("Def");
//       WebElement emailInput = driver.findElement(By.id("email"));
//
//       String emailData = DataGeneratorManager.getRandomEmail();
//       emailInput.sendKeys(emailData);
//       System.out.println("Email:" + emailData);
//
//       WebElement passwordInput = driver.findElement(By.id("password"));
//       passwordInput.sendKeys("Welcome123@");
//       Thread.sleep(3000);
//       WebElement submitButton = driver.findElement(By.id("submit"));
//       submitButton.click();
//
//       driver.close();
//
//       driver.switchTo().window(currentTabName);
//       driver.get("https://thinking-tester-contact-list.herokuapp.com/login");
//       System.out.println("The current URL is: " + driver.getCurrentUrl());
//       System.out.println("The current page title is: " + driver.getTitle());
//       WebElement emailLogin = driver.findElement(By.id("email"));
//       emailLogin.sendKeys(emailData);
//       WebElement passwordLogin = driver.findElement(By.id("password"));
//       passwordLogin.sendKeys("Welcome123@");
//       Thread.sleep(3000);
//       WebElement submitButtonLogin = driver.findElement(By.id("submit"));
//       submitButtonLogin.click();
//       Thread.sleep(3000);
//       System.out.println("The current page title is: " + driver.getTitle());
//       Thread.sleep(3000);
//       driver.quit();
//
//       System.out.println("The test is finished and the driver is closed");
//
//   }
//}