import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartTest {
    private WebDriver driver;
    private final String baseUrl = "http://localhost/litecart";

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testCart() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5/*seconds*/);
        driver.get(baseUrl);
        for(int i = 0; i < 3; i++) {
            //установлена пауза, т.к. не всегда происходит переход на страницу товара,
            // хотя команда Click() отрабатывает без ошибок (гипотеза, что плитка не имеет ссылки оказалась неверной)
            //wait.until(attributeContains(By.cssSelector(".product a"), "href", "http"));
            Thread.sleep(1000);
            driver.findElement(By.cssSelector(".product a")).click();
            //иногда у товара обязательно указать размер
            if (driver.findElements(By.name("options[Size]")).size() != 0) {
                driver.findElement(By.name("options[Size]")).click();
                driver.findElement(By.cssSelector("option[value='Small']")).click();
            }
            driver.findElement(By.name("add_cart_product")).click();
            wait.until(textToBePresentInElementLocated(By.className("quantity"),String.valueOf(i+1)));
            driver.findElement(By.id("logotype-wrapper")).click();
        }
        //так же пришлось добавить паузу, явные ожидания не помогали
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[contains(text(), 'Checkout')]")).click();

        int countRow = driver.findElements(By.cssSelector(".dataTable .item")).size();
        int countProduct = driver.findElements(By.cssSelector(".shortcut a")).size();
        for(int i = 0; i < countProduct; i++) {
            Thread.sleep(1000);
            //проверяем изменение в таблице при удалении товара
            if(i != countProduct-1) {
                WebElement shortcut = driver.findElement(By.cssSelector(".shortcut a"));
                shortcut.click();
                driver.findElement(By.name("remove_cart_item")).click();
                wait.until(stalenessOf(shortcut));
                Assertions.assertTrue(driver.findElements(By.cssSelector(".dataTable .item")).size() < countRow);
                countRow = driver.findElements(By.cssSelector(".dataTable .item")).size();
            }
            //если товар в корзине единственный, проверяем текст на странице
            else {
                driver.findElement(By.name("remove_cart_item")).click();
                wait.until(textToBePresentInElementLocated(By.id("checkout-cart-wrapper"), "There are no items in your cart."));
            }
        }
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
