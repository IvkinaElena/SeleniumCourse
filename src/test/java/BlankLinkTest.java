import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BlankLinkTest {
    private WebDriver driver;
    private String baseUrl = "http://localhost/litecart/admin/";
    private ExpectedCondition<String> anyWindowOtherThen(Set<String> oldWindows) {
        return new ExpectedCondition<String>() {
            @NullableDecl
            @Override
            public String apply(@NullableDecl WebDriver input) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

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
    public void testLink(){
        WebDriverWait wait = new WebDriverWait(driver, 5/*seconds*/);
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        String mainWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();
        driver.findElement(By.xpath("//a[text()=' Add New Country']")).click();
        int countLink = driver.findElements(By.className("fa-external-link")).size();
        for(int i = 0; i < countLink; i++) {
            driver.findElements(By.className("fa-external-link")).get(i).click();
            String newWindow = wait.until(anyWindowOtherThen(oldWindows));
            driver.switchTo().window(newWindow);
            driver.close();
            driver.switchTo().window(mainWindow);
        }
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
