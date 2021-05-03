package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage extends Page{

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("http://localhost/litecart");
    }

    @FindBy(css=".product a")
    public WebElement productPlate;

    @FindBy(xpath = "//a[contains(text(), 'Checkout')]")
    public WebElement cartLink;

    public void clickOnPlate() {
        productPlate.click();
    }

    public void openCart() {
        cartLink.click();
    }
}
