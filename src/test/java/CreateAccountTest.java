import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateAccountTest {
    private WebDriver driver;
    private final String baseUrl = "http://localhost/litecart";
    private final String firstName = "Elon";
    private final String lastName = "Musk";
    private final String address1 = "my address";
    private final String postcode = "12345";
    private final String city = "Washington";
    private final String phone = "+1234567890";
    private String createEmail() {
        Random RANDOM = new Random();
        return RANDOM.nextInt() + "@example.com";
    }
    private final String password = "123";

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(baseUrl);
    }

    @Test
    public void createAccountTest() {
        driver.findElement(By.xpath("//a[contains(text(),'New customers')]")).click();
        driver.findElement(By.name("firstname")).sendKeys(firstName);
        driver.findElement(By.name("lastname")).sendKeys(lastName);
        driver.findElement(By.name("address1")).sendKeys(address1);
        driver.findElement(By.name("postcode")).sendKeys(postcode);
        driver.findElement(By.name("city")).sendKeys(city);
        Select select = new Select(driver.findElement(By.name("country_code")));
        select.selectByVisibleText("United States");
        String uniqueEmail = createEmail();
        driver.findElement(By.name("email")).sendKeys(uniqueEmail);
        driver.findElement(By.name("phone")).sendKeys(phone);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("confirmed_password")).sendKeys(password);
        driver.findElement(By.name("create_account")).click();

        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();

        driver.findElement(By.name("email")).sendKeys(uniqueEmail);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();

        driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
    }

    @AfterAll
    public void tearDown() {
        driver.quit();}
}
