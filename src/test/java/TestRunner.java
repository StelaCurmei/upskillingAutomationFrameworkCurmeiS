import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        //features = "classpath:features",
        glue = {"stepdefinitions","hooks", "utils"},
        plugin = {"pretty",
                "json:target/cucumber-reports/cucumber-report.json",
                "html:target/cucumber-reports/cucumber-report.html",
                "json:target/allure-results/cucumber-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@run",
        monochrome = true
)

public class TestRunner {
}