import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MenuItemTest {
    private WebDriver driver;

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testMenuItem() {
        String url = "http://localhost/litecart/admin/";
        driver.get(url);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5/*seconds*/);
        WebElement menu = wait.until(presenceOfElementLocated(By.id("app-")));
        int countMenuItem = driver.findElements(By.id("app-")).size();
        if (countMenuItem != 0) {
            for (int i = 0; i < countMenuItem; i++) {
                driver.findElements(By.id("app-")).get(i).click();
                assertTrue(driver.findElement(By.cssSelector("h1")).isDisplayed());
                int countSubMenuItem = driver.findElements(By.cssSelector(".docs li")).size();
                if (countSubMenuItem != 0) {
                    for (int j = 0; j < countSubMenuItem; j++) {
                        driver.findElements(By.cssSelector(".docs li")).get(j).click();
                        assertTrue(driver.findElement(By.cssSelector("h1")).isDisplayed());
                    }
                }
            }
        }
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
