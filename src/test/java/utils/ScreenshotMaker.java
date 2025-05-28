package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotMaker {
    public static String takeScreenshot(WebDriver driver, String namePrefix) {
        try {
            // Timestamped filename
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = namePrefix + "_" + timestamp + ".png";

            // Capture
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Destination
            Path dest = Path.of("target", "screenshots", filename);
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest);

            return dest.toString();
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
