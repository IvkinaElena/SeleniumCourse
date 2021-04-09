import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SortCountriesTests {
    private WebDriver driver;
    private String baseUrl = "http://localhost/litecart/admin/";
    private boolean listIsSorted(ArrayList<String> list) {
        ArrayList<String> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        return list.equals(sortedList);
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
    public void testSortCountries() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        ArrayList<String> countriesList = new ArrayList<>();
        ArrayList<String> linkCountryEditList = new ArrayList<>();
        for (WebElement element : driver.findElements(By.cssSelector(".dataTable tr td:nth-child(5)"))) {
            countriesList.add(element.getText());
            if (!element.findElement(By.xpath("./following::td")).getText().equals("0")) {
                linkCountryEditList.add(element.findElement(By.cssSelector("a")).getAttribute("href"));
            }
        }
        assertTrue(listIsSorted(countriesList));

        for (String link : linkCountryEditList) {
            driver.get(link);
            ArrayList<String> zonesList = new ArrayList<>();
            for (WebElement element : driver.findElements(By.cssSelector("#table-zones tr td:nth-child(3)"))){
                if (!element.getText().equals("")) {
                    zonesList.add(element.getText());
                }
            }
            assertTrue(listIsSorted(zonesList));
        }
    }
    @Test
    public void testSortZones() {
        String url = "http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones";
        driver.get(url);
        int countCountries = driver.findElements(By.cssSelector(".dataTable tr td:nth-child(3)")).size();
        for (int i = 0; i < countCountries; i++) {
            driver.findElements(By.cssSelector(".dataTable tr td:nth-child(3) a")).get(i).click();
            ArrayList<String> zonesList = new ArrayList<>();
            for (WebElement element : driver.findElements(By.cssSelector("#table-zones tr td:nth-child(3) select"))) {
                zonesList.add(element.getText());
            }
            assertTrue(listIsSorted(zonesList));
            driver.get(url);
        }

    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }
}
