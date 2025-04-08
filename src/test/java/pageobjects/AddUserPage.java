package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AddUserPage {

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

    public AddUserPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstNameInput) {
        firstName.sendKeys(firstNameInput); // Entering firstName into firstName text field
    }

    public WebElement getLastName() {
        return lastName;
    }

    public void setLastName(String lastNameInput) {
        lastName.sendKeys(lastNameInput);  // Entering lastName into lastName text field
    }

    public WebElement getEmail() {
        return email;
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
    public void signUp(String firstName, String lastName, String email, String password) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        this.submit.click();
    }
}