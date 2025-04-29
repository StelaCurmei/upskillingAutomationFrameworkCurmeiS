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

    @FindBy(id = "error")
    private WebElement error;

    public LogInPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setEmail(String emailInput) {
        this.email.sendKeys(emailInput); // Entering email into email text field
    }

    public void clearEmail() {
        this.email.clear();
    }

    public WebElement getPassword() {
        return password;
    }

    public void setPassword(String passwordInput) {
        this.password.sendKeys(passwordInput); // Setting password
    }

    public void clearPassword() {
        this.password.clear();
    }

    public WebElement getSubmit() {
        return submit;
    }

    public void clickSubmit() {
        this.submit.click();  // Clicking on submit button
    }

    public WebElement getSignUp() {
        return signUp;
    }

    public void clickSignUp() {
        this.signUp.click();
    }

    public WebElement getErrorMessage() {
        return error;
    }

    public String getMessageText() {
        return error.getText();
    }

    public void logIn(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickSubmit();
    }

}

