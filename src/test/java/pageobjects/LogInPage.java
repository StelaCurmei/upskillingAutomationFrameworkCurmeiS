package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogInPage {

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "submit")
    private WebElement submit;

    @FindBy(id = "signup")
    private WebElement signUp;

    public LogInPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setEmail(String emailInput) {
        email.sendKeys(emailInput); // Entering email into email text field
    }

    public WebElement getPassword() {
        return password;
    }

    public void setPassword(String passwordInput) {
        password.sendKeys(passwordInput); // Setting password
    }

    public WebElement getSubmit() {
        return submit;
    }

    public void clickSubmit() {
        submit.click();  // Clicking on submit button
    }

    public WebElement getSignUp() {
        return signUp;
    }
    public void clickSignUp() {
        signUp.click();
    }
    public void logIn(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickSubmit();
    }
}

