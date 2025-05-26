import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks", "utils"},
        plugin = {"pretty",
                "json:target/cucumber-report/cucumber-report.json",
                "html:target/cucumber-report/cucumber-report.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@run",
        monochrome = true
)

public class TestRunner {
}