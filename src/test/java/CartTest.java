import PageObjects.CartPage;
import PageObjects.MainPage;
import PageObjects.ProductPage;
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
    private MainPage mainPage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        mainPage = new MainPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test
    public void testCart() throws InterruptedException {
        mainPage.open();
        for(int i = 0; i < 3; i++) {
            //установлена пауза, т.к. не всегда происходит переход на страницу товара,
            // хотя команда Click() отрабатывает без ошибок (гипотеза, что плитка не имеет ссылки оказалась неверной)
            //wait.until(attributeContains(By.cssSelector(".product a"), "href", "http"));
            Thread.sleep(1000);
            mainPage.clickOnPlate();
            //иногда у товара обязательно указать размер
            productPage.inputSize();
            productPage.addProductToCart();
            productPage.waitCountProductOfCart(i);
            productPage.backToMainPage();
        }
        //так же пришлось добавить паузу, явные ожидания не помогали
        Thread.sleep(1000);
        mainPage.openCart();

        int countRow = cartPage.getCountRow();
        int countProduct = cartPage.getCountProduct();
        for(int i = 0; i < countProduct; i++) {
            Thread.sleep(1000);
            //проверяем изменение в таблице при удалении товара
            if(i != countProduct-1) {
                cartPage.removeProduct();
                Assertions.assertTrue(cartPage.getCountRow() < countRow);
                countRow = cartPage.getCountRow();
            }
            //если товар в корзине единственный, проверяем текст на странице
            else {
                cartPage.removeLastProduct();
                cartPage.assertCartIsEmpty();
            }
        }
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
