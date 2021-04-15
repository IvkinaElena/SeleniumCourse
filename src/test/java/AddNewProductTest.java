import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddNewProductTest {
    private WebDriver driver;
    private String baseUrl = "http://localhost/litecart/admin/";
    private String createName() {
        Random RANDOM = new Random();
        return RANDOM.nextInt() + " name";
    }
    private Path imagesPath = Paths.get("src/test/resources/dog.jpg");
    private String keywords = "keywords";
    private String shortDescription = "short description";
    private String description = "description";
    private String headTitle = "head title";
    private String metaDescription = "meta description";



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
    public void testAddNewProduct() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5/*seconds*/);
        WebElement catalog = wait.until(presenceOfElementLocated(By.xpath("//span[contains(text(),'Catalog')]")));
        Thread.sleep(2000);
        catalog.click();
        driver.findElement(By.xpath("//a[contains(text(),' Add New Product')]")).click();
        //Заполняем вкладку General
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@name='status']")).click();
        String name = createName();
        driver.findElement(By.name("name[en]")).sendKeys(name);
        driver.findElements(By.xpath("//input[@name='product_groups[]']")).get(2).click();
        driver.findElement(By.name("quantity")).clear();
        driver.findElement(By.name("quantity")).sendKeys("1");
        driver.findElement(By.name("new_images[]")).sendKeys(imagesPath.toAbsolutePath().normalize().toString());
        driver.findElement(By.name("date_valid_from")).sendKeys("15.04.2021");
        driver.findElement(By.name("date_valid_to")).sendKeys("20.04.2021");
        //Заполняем вкладку Information
        driver.findElement(By.xpath("//a[contains(text(),'Information')]")).click();
        Thread.sleep(2000);
        Select select = new Select(driver.findElement(By.name("manufacturer_id")));
        select.selectByVisibleText("ACME Corp.");
        driver.findElement(By.name("keywords")).sendKeys(keywords);
        driver.findElement(By.name("short_description[en]")).sendKeys(shortDescription);
        driver.findElement(By.className("trumbowyg-editor")).sendKeys(description);
        driver.findElement(By.name("head_title[en]")).sendKeys(headTitle);
        driver.findElement(By.name("meta_description[en]")).sendKeys(metaDescription);
        //Заполняем вкладку Prices
        driver.findElement(By.xpath("//a[contains(text(),'Prices')]")).click();
        Thread.sleep(2000);
        driver.findElement(By.name("purchase_price")).clear();
        driver.findElement(By.name("purchase_price")).sendKeys("5");
        driver.findElement(By.name("prices[USD]")).clear();
        driver.findElement(By.name("prices[USD]")).sendKeys("10");
        driver.findElement(By.name("prices[EUR]")).clear();
        driver.findElement(By.name("prices[EUR]")).sendKeys("8");
        //Сохраняем и проверяем продукт в каталоге
        driver.findElement(By.name("save")).click();
        assertTrue(driver.findElement(By.partialLinkText(name)).isDisplayed());
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
