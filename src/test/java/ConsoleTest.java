import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsoleTest {
    private WebDriver driver;
    private String baseUrl = "http://localhost/litecart/admin/";

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(baseUrl);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void testConsole() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5/*seconds*/);
        WebElement catalog = wait.until(presenceOfElementLocated(By.xpath("//span[contains(text(),'Catalog')]")));
        Thread.sleep(2000);
        catalog.click();
        //раскрываем папки
        while (driver.findElements(By.cssSelector("td i+a")).size() > 0) {
            driver.findElement(By.cssSelector("td i+a")).click();
        }
        //открываем страницы товаров
        int product_count = driver.findElements(By.cssSelector(".row td img + a")).size();
        for (int i = 0; i < product_count; i ++) {
            wait.until(presenceOfElementLocated(By.cssSelector(".row td img + a")));
            driver.findElements(By.cssSelector(".row td img + a")).get(i).click();
            System.out.println(i);
            driver.navigate().back();
            //проверяем ошибки в консоли, если будут, то выводим и фейлим тест
            for (LogEntry l : driver.manage().logs().get("browser").getAll()) {
                System.out.println(l);
                assertTrue(l == null);
            }
        }
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }

}
