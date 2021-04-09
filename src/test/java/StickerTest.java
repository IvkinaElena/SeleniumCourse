import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StickerTest {
    private WebDriver driver;

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSticker() {
        driver.get("http://localhost/litecart");
        for(WebElement element : driver.findElements(By.cssSelector(".product"))) {
            assertTrue((element.findElement(By.cssSelector(".sticker")).isDisplayed()) && (element.findElements(By.cssSelector(".sticker")).size() == 1));
        }

    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
