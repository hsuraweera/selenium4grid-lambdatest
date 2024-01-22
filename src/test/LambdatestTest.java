import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LambdatestTest {

    private WebDriver driver;

    public void setup() throws MalformedURLException {

        String hubURL = "https://hub.lambdatest.com/wd/hub";

        String[] Tags = new String[] { "Feature", "Falcon", "Severe" };

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "MicrosoftEdge");
        capabilities.setCapability("browserVersion", "latest");

        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("user", "{LT_USERNAME}");
        ltOptions.put("accessKey", "{TL_ACCESS_KEY}");
        ltOptions.put("build", "Selenium 4 macOS Sonoma MicrosoftEdge");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("platformName", "macOS Sonoma");
        ltOptions.put("seCdp", true);
        ltOptions.put("selenium_version", "4.0.0");
        ltOptions.put("tags", Tags);
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("network", true);
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        System.out.println(driver);
    }

    public void testMethod() {
        driver.get("https://lambdatest.com");

        Duration twenty = Duration.ofSeconds(20);
        WebDriverWait wait = new WebDriverWait(driver, twenty);

        WebElement seeAllIntegrations = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'See All Integrations')]")));

        Actions actions = new Actions(driver);
        actions.moveToElement(seeAllIntegrations).perform();

        String mainWindowHandle = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", seeAllIntegrations);

        Assert.assertEquals("Current URL is not as expected", "https://www.lambdatest.com/integrations", driver.getCurrentUrl());

        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());

        System.out.println("Window Handles after opening new window: " + windowHandles);

    }

    public void tearDown() {
        driver.quit();
    }

    public static void markStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
        jsExecute.executeScript("lambda-status=" + status);
        System.out.println(reason);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        LambdatestTest test = new LambdatestTest();
        test.setup();
        test.testMethod();
        test.tearDown();
    }

}