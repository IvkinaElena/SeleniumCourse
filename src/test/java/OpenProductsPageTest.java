import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenProductsPageTest {
    private WebDriver driver;
    private String baseUrl = "http://localhost/litecart";
    private boolean colorIsGrey(String rgba) {
        Color color = Color.fromString(rgba);
        int r = color.getColor().getRed();
        int g = color.getColor().getGreen();
        int b = color.getColor().getBlue();
        return r==g && r==b;
    }
    private boolean colorIsRed(String rgba) {
        Color color = Color.fromString(rgba);
        int g = color.getColor().getGreen();
        int b = color.getColor().getBlue();
        return g==0 && b==0;

    }

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(baseUrl);
    }

    @Test
    public void testProductName() {
        WebElement plate = driver.findElement(By.cssSelector("#box-campaigns .product .name"));
        String nameOnPlate = plate.getText();
        plate.click();
        assertTrue(nameOnPlate.equals(driver.findElement(By.cssSelector("#box-product .title")).getText()));
    }

    @Test
    public void testPrice() {
        String regularPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .regular-price")).getText();
        String campaignPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .campaign-price")).getText();
        driver.findElement(By.cssSelector("#box-campaigns .product")).click();
        assertTrue(regularPriceOnPlate.equals(driver.findElement(By.cssSelector(".regular-price")).getText()) &&
                campaignPriceOnPlate.equals(driver.findElement(By.cssSelector(".campaign-price")).getText()));
    }

    @Test
    public void testRegularPriceStyle() {
        WebElement regularPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .regular-price"));
        String textDecoration = regularPriceOnPlate.getCssValue("text-decoration-line");
        String color = regularPriceOnPlate.getCssValue("color");
        assertTrue(textDecoration.equals("line-through")
                        && colorIsGrey(color));

        driver.findElement(By.cssSelector("#box-campaigns .product")).click();
        WebElement regularPriceOnProductPage = driver.findElement(By.cssSelector(".regular-price"));
        textDecoration = regularPriceOnProductPage.getCssValue("text-decoration-line");
        color = regularPriceOnProductPage.getCssValue("color");
        assertTrue(textDecoration.equals("line-through")
                && colorIsGrey(color));
    }

    @Test
    public void testCampaignPriceStyle() {
        WebElement campaignPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .campaign-price"));
        String fontWeight = campaignPriceOnPlate.getCssValue("font-weight");
        String color = campaignPriceOnPlate.getCssValue("color");
        assertTrue(fontWeight.equals("700")
                && colorIsRed(color));

        driver.findElement(By.cssSelector("#box-campaigns .product")).click();
        WebElement campaignPriceOnProductPage = driver.findElement(By.cssSelector(".campaign-price"));
        fontWeight = campaignPriceOnProductPage.getCssValue("font-weight");
        color = campaignPriceOnProductPage.getCssValue("color");
        assertTrue(fontWeight.equals("700")
                && colorIsRed(color));
    }

    @Test
    public void testFontSizePrice() {
        WebElement regularPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .regular-price"));
        WebElement campaignPriceOnPlate = driver.findElement(By.cssSelector("#box-campaigns .product .campaign-price"));
        String sizeRegularPrice = regularPriceOnPlate.getCssValue("font-size");
        String sizeCampaignPrice = campaignPriceOnPlate.getCssValue("font-size");
        assertTrue(Float.parseFloat(sizeRegularPrice.substring(0, (sizeRegularPrice.length() - 2))) <
                Float.parseFloat(sizeCampaignPrice.substring(0, (sizeCampaignPrice.length() - 2))));

        driver.findElement(By.cssSelector("#box-campaigns .product")).click();
        WebElement regularPriceOnProductPage = driver.findElement(By.cssSelector(".regular-price"));
        WebElement campaignPriceOnProductPage = driver.findElement(By.cssSelector(".campaign-price"));
        sizeRegularPrice = regularPriceOnProductPage.getCssValue("font-size");
        sizeCampaignPrice = campaignPriceOnProductPage.getCssValue("font-size");
        assertTrue(Float.parseFloat(sizeRegularPrice.substring(0, (sizeRegularPrice.length() - 2))) <
                Float.parseFloat(sizeCampaignPrice.substring(0, (sizeCampaignPrice.length() - 2))));
    }

    @AfterEach
    public void backHomePage() {
        driver.get(baseUrl);
    }
    @AfterAll
    public void tearDown() {
        driver.quit();}

}
