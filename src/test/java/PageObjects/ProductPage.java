package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

public class ProductPage extends Page{

    public ProductPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(name="add_cart_product")
    public WebElement addToCart;

    @FindBy(id="logotype-wrapper")
    public WebElement logo;


    public void addProductToCart(){
        addToCart.click();
    }

    public void inputSize() {
        if (driver.findElements(By.name("options[Size]")).size() != 0) {
            driver.findElement(By.name("options[Size]")).click();
            driver.findElement(By.cssSelector("option[value='Small']")).click();
        }
    }

    public void waitCountProductOfCart(int countBeforeAdd) {
        wait.until(textToBePresentInElementLocated(By.className("quantity"),String.valueOf(countBeforeAdd+1)));
    }

    public void backToMainPage() {
        logo.click();
    }
}
