import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class StudentsTests {
    private WebDriver driver;

    @Before
    public void setup() {
        String homepageLink = "https://www.wiley.com/en-us";
        this.driver = Utilities.webDriverForBaseUrl(homepageLink);
        Utilities.closeWileyPopupWindow(driver);
    }

    //Task #3
    @Test
    public void testLearnMoreLinks() {
        String dropdownMenuPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[1]";
        WebElement dropdownMenu = driver.findElement(By.xpath(dropdownMenuPath));
        Actions actions = new Actions(driver);
        actions.moveToElement(dropdownMenu).build().perform();

        String studentLinkPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[1]/div[1]/ul[1]/li[1]/a[1]";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement studentLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(studentLinkPath)));
        studentLink.click();

        String urlBeforeClick = driver.getCurrentUrl();
        String urlAfterClick = driver.getCurrentUrl();
        Assert.assertEquals("Current page is not the homepage!", urlBeforeClick, urlAfterClick);

        String studentsHeaderPath = "/html[1]/body[1]/main[1]/div[2]/div[1]/div[2]/div[2]/article[1]/div[1]/p[2]/span[1]";
        boolean studentsHeaderElementsExists = !driver.findElements(By.xpath(studentsHeaderPath)).isEmpty();
        Assert.assertTrue("'Students' header doesn't exist!", studentsHeaderElementsExists);

        String expectedDomain = "www.wileyplus.com";
        List<WebElement> learnMoreButtons = driver.findElements(By.xpath("//*[contains(text(), 'Learn More')]"));
        List<String> links = learnMoreButtons.stream()
                .map(link -> (link.findElement(By.xpath("../..")).getAttribute("href")))
                .collect(Collectors.toList());

        for (String link : links) {
            Assert.assertTrue("Not every link leads to WileyPlus website", link.contains(expectedDomain));
        }
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
