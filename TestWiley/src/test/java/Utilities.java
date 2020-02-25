import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class Utilities {

    static WebDriver webDriverForBaseUrl(String baseUrl) {
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.get(baseUrl);
        return driver;
    }

    static void waitForPageToLoad(WebDriver driver)  {
        new WebDriverWait(driver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

    }

    static void closeWileyPopupWindow(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-dialog")));
        WebElement closeButton = form.findElement(By.className("changeLocationConfirmBtn"));
        closeButton.click();

        wait.until(ExpectedConditions.invisibilityOf(form));
    }
}

