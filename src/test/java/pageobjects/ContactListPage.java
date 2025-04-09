package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContactListPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "logout")
    private WebElement logout;

    @FindBy(id = "add-contact")
    private WebElement addNewContact;

    public ContactListPage(WebDriver driver) {
        this.driver = driver;
        // initialize PageFactory elements
        PageFactory.initElements(driver, this);
        // initialize wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public WebElement getLogout() {
        return logout;
    }

    public void clickLogout() {
        // Wait until the logout button is clickable before clicking
        wait.until(ExpectedConditions.elementToBeClickable(logout));
        logout.click();
    }

    public WebElement getAddNewContact() {
        return addNewContact;
    }

    public void clickAddNewContact() {
        // Wait until the add new contact button is clickable before clicking
        wait.until(ExpectedConditions.elementToBeClickable(addNewContact));
        addNewContact.click();
    }
}
