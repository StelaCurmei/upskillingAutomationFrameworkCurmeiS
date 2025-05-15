package pageobjects;

import io.cucumber.datatable.DataTable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class SignUpPage {

    @FindBy(id = "firstName")
    private WebElement firstName;

    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "submit")
    private WebElement submit;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstNameInput) {
        this.firstName.sendKeys(firstNameInput); // Entering firstName into firstName text field
    }

    public void clearFirstName() {
        this.firstName.clear();
    }

    public WebElement getLastName() {
        return lastName;
    }

    public void setLastName(String lastNameInput) {
        this.lastName.sendKeys(lastNameInput);  // Entering lastName into lastName text field
    }

    public void clearLastName() {
        this.lastName.clear();
    }

    public WebElement getEmail() {
        return email;
    }

    @FindBy(id = "error")
    private WebElement error;

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

    public WebElement getSignUpError() {
        return error;
    }

    public String getSignUpErrorMessage() {
        return error.getText();
    }

    public void clearForm() {
        clearFirstName();
        clearLastName();
        clearEmail();
        clearPassword();
    }

    public void signUp(String firstName, String lastName, String email, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        clickSubmit();
    }
}