package managers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class ScrollManager {
    public static void scrollToElement(WebElement WebElement) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) DriverManager.getDriver();
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", WebElement);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
