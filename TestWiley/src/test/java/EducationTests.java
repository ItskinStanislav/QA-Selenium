import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;


public class EducationTests {
    private WebDriver driver;

    @Before
    public void setup() {
        String homepageLink = "https://www.wiley.com/en-us";
        this.driver = Utilities.webDriverForBaseUrl(homepageLink);
        Utilities.closeWileyPopupWindow(driver);
    }

    //Task #4
    @Test
    public void testEducationPage() {
        String[] expectedTitles = {
                "Information &amp; Library Science", "Education &amp; Public Policy",
                "K-12 General", "Higher Education General",
                "Vocational Technology", "Conflict Resolution &amp; Mediation (School settings)", "Curriculum Tools- General",
                "Special Educational Needs", "Theory of Education",
                "Education Special Topics", "Educational Research &amp; Statistics",
                "Literacy &amp; Reading", "Classroom Management"
        };

        String subjectDropDownPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[2]";
        WebElement subjectDropDown = driver.findElement(By.xpath(subjectDropDownPath));
        Actions actions = new Actions(driver);
        actions.moveToElement(subjectDropDown).build().perform();

        String educationPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[2]/div[1]/ul[1]/li[9]/a[1]";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement education = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(educationPath)));
        education.click();

        String educationHeaderPath = "/html[1]/body[1]/main[1]/div[3]/div[1]/div[1]/div[2]/div[2]/article[1]/div[1]/div[1]/h1[3]/span[1]";
        boolean educationHeaderElementsExists = !driver.findElements(By.xpath(educationHeaderPath)).isEmpty();
        Assert.assertTrue("'Education' header doesn't exist!", educationHeaderElementsExists);

        String menuListPath = "/html[1]/body[1]/main[1]/div[3]/div[1]/div[1]/div[4]/div[1]";
        WebElement menuList = driver.findElement(By.xpath(menuListPath));
        Actions scrollToMenuAction = new Actions(driver);
        scrollToMenuAction.moveToElement(menuList).build().perform();
        wait.until(ExpectedConditions.visibilityOf(menuList));

        List<WebElement> links = menuList.findElements(By.tagName("li"));
        String[] realTitles = links.stream()
                .map( link -> (link.findElement(By.xpath("a[1]")).getAttribute("innerHTML").trim()))
                .collect(Collectors.toList())
                .toArray(new String[links.size()]);

        Assert.assertArrayEquals("Titles are mismatched", expectedTitles, realTitles);

    }
    @After
    public void tearDown(){
        driver.quit();
    }

}
