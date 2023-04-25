package com.lambda.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LambdaTests {

    private static final String BASEURL = "https://www.lambdatest.com";
    boolean status = false;
    private String ltUsername ;
    private String ltAccessKey;
    private static ThreadLocal<RemoteWebDriver> driver = null;
    private static final String GRIDURL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    @Parameters({"ltUsername", "ltAccessKey", "browserName", "browserVersion", "platform"})
    public void setUp(String ltUsername, String ltAccessKey, String browserName, String browserVersion, String platform) {

        try {
            this.ltUsername = ltUsername;
            this.ltAccessKey = ltAccessKey;

            // Define the LambdaTest URL
            String remoteURL = "https://" + ltUsername + ":" + ltAccessKey + GRIDURL;
            driver = new ThreadLocal<>();

            if (browserName.equalsIgnoreCase("chrome")) {

                // Define the desired capabilities
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPlatformName(platform);
                chromeOptions.setBrowserVersion(browserVersion);
                HashMap<String, Object> ltOptions = lambdaOptions();
                chromeOptions.setCapability("LT:Options", ltOptions);


                // Initialize the Remote WebDriver with remote URL and capabilities
                driver.set(new RemoteWebDriver(new URL(remoteURL), chromeOptions));

            } else if (browserName.equalsIgnoreCase("MicrosoftEdge")) {

                // Define the desired capabilities
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPlatformName(platform);
                edgeOptions.setBrowserVersion(browserVersion);
                HashMap<String, Object> ltOptions = lambdaOptions();
                edgeOptions.setCapability("LT:Options", ltOptions);

                // Initialize the Remote WebDriver with remote URL and capabilities
                driver.set(new RemoteWebDriver(new URL(remoteURL), edgeOptions));
            } else
                System.out.println("The browser is not supported");

        } catch (NullPointerException e) {
            System.out.println("NullPointerException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void lambdaSampleTest() {
        // Navigate to the base URL
        driver.get().get(BASEURL);

        // Perform an explicit wait till the time all the elements in the DOM are available
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"__next\"]//a[contains(@class, 'uppercase')]")));

        // Scroll to the WebElement ‘SEE ALL INTEGRATIONS’ using the scrollIntoView() method and open it to a new tab
        WebElement seeAllIntegrations = driver.get().findElement(By.xpath("//*[@id=\"__next\"]//a[contains(@class, 'uppercase')]"));
        new Actions(driver.get()).moveToElement(seeAllIntegrations).perform();
        ((JavascriptExecutor) driver.get()).executeScript("window.open(arguments[0].href,'_blank');", seeAllIntegrations);

        // Get window handle
        String currentWindowHandle = driver.get().getWindowHandle();

        List<String> windowHandles = new ArrayList<String>(driver.get().getWindowHandles());
        windowHandles.remove(currentWindowHandle);
        driver.get().switchTo().window(windowHandles.get(0));

        // Save the window handles in a List (or array)
        windowHandles = new ArrayList<>(driver.get().getWindowHandles());
        System.out.println("Window handles: " + windowHandles);

        // Verify whether the URL is the same as the expected URL
        String expectedURL = "https://www.lambdatest.com/integrations";
        String actualURL = driver.get().getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);

        if (actualURL.equals(expectedURL)) {
            status = true;
            driver.get().executeScript("lambda-status=passed");
        } else
            driver.get().executeScript("lambda-status=failed");


        // Close the current browser window
        driver.get().close();

        // Switch back to the first window
        driver.get().switchTo().window(currentWindowHandle);
    }

    @AfterMethod
    public void tearDown() {
        if (driver.get() != null) {
            ((JavascriptExecutor) driver.get()).executeScript("lambda-status=" + status);
            driver.get().quit();
        }
    }

    private HashMap<String, Object> lambdaOptions() {
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("console", true);
        ltOptions.put("network", true);
        ltOptions.put("build", "LambdaTestDemo");
        ltOptions.put("project", "DemoRunSeleniumProjectOnLambda");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");

        return ltOptions;
    }
}
