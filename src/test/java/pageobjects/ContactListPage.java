package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactListPage {

    @FindBy(id = "logout")
    private WebElement logout;

    @FindBy(id = "add-contact")
    private WebElement addnewcontact;

    public ContactListPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getLogout() {
        return logout;
    }

    public void clickLogout() {
        logout.click();  // Clicking on logout button
    }

    public WebElement getAddNewContact() {
        return addnewcontact;
    }
    public void clickAddNewContact() {
        addnewcontact.click();
    }
}