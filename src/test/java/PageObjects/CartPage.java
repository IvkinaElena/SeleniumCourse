package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

public class CartPage extends Page{

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public int getCountRow() {
        return driver.findElements(By.cssSelector(".dataTable .item")).size();
    }

    public int getCountProduct() {
        return driver.findElements(By.cssSelector(".shortcut a")).size();
    }

    @FindBy(name="remove_cart_item")
    public WebElement remove;

    public void removeProduct() {
        WebElement shortcut = driver.findElement(By.cssSelector(".shortcut a"));
        shortcut.click();
        remove.click();
        wait.until(stalenessOf(shortcut));
    }

    public void removeLastProduct() {
        remove.click();
    }

    public void assertCartIsEmpty() {
        wait.until(textToBePresentInElementLocated(By.id("checkout-cart-wrapper"), "There are no items in your cart."));
    }
}
