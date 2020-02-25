import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomepageTests {
    private WebDriver driver;
    private String homepageLink = "https://www.wiley.com/en-us";

    @Before
    public void setup() {
        this.driver = Utilities.webDriverForBaseUrl(homepageLink);
        Utilities.closeWileyPopupWindow(driver);
    }

    // Task #1
    @Test
    public void testAllHeadersExist() {
        String whoWeServeHeaderPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[1]/a[1]";
        boolean whoWeServeElementsExists = !driver.findElements(By.xpath(whoWeServeHeaderPath)).isEmpty();
        Assert.assertTrue("'Who we serve' header doesn't exist!", whoWeServeElementsExists);

        String subjectsHeaderPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[2]/a[1]";
        boolean subjectsHeaderExists = !driver.findElements(By.xpath(subjectsHeaderPath)).isEmpty();
        Assert.assertTrue("'Subjects' header doesn't exist!", subjectsHeaderExists);

        String aboutHeaderPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[4]/a[1]";
        boolean aboutHeaderExists = !driver.findElements(By.xpath(aboutHeaderPath)).isEmpty();
        Assert.assertTrue("'About' header doesn't exist!", aboutHeaderExists);
    }

    //Task #2
    @Test
    public void testWhoWeServeMenuTitles(){
        String[] expectedTitles = {
                "Students", "Instructors", "Book Authors",
                "Professionals", "Researchers", "Institutions",
                "Librarians", "Corporations", "Societies",
                "Journal Editors", /*"Bookstores",*/ "Government"
        };

        WebDriverWait wait = new WebDriverWait(driver, 10);

        String dropdownMenuPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/nav[1]/ul[1]/li[1]";
        WebElement dropdownMenu = driver.findElement(By.xpath(dropdownMenuPath));

        Actions actions = new Actions(driver);
        actions.moveToElement(dropdownMenu).build().perform();

        WebElement menuList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dropdown-items")));

        List<WebElement> links = menuList.findElements(By.tagName("li"));
        String[] realTitles = links.stream()
                .map( link -> (link.findElement(By.xpath("a[1]")).getAttribute("innerHTML").trim()))
                .collect(Collectors.toList())
                .toArray(new String[links.size()]);

        Assert.assertArrayEquals("Titles are mismatched", expectedTitles, realTitles);
    }

    // Task #5
    @Test
    public void testLogoClickLeadsToHomepage() {
        String logoPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[1]/div[1]/div[1]/div[1]/a[1]/img[1]";
        WebElement logoClick = driver.findElement(By.xpath(logoPath));
        logoClick.click();

        Utilities.waitForPageToLoad(this.driver);

        Assert.assertEquals("Current page is not the homepage!", this.homepageLink, driver.getCurrentUrl());
    }

    // Task #6
    @Test
    public void testUserRemainsOnHomepageAfterNoQuerySearchIconClick() {
        String urlBeforeClick = driver.getCurrentUrl();

        String searchIconPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[2]/div[1]/form[1]/div[1]/span[1]/button[1]";
        WebElement searchIcon = driver.findElement(By.xpath(searchIconPath));
        searchIcon.click();

        Utilities.waitForPageToLoad(this.driver);
        String urlAfterClick = driver.getCurrentUrl();

        Assert.assertEquals("Current page is not the homepage!", urlBeforeClick, urlAfterClick);
    }

    //Task #7
    @Test
    public void testSearchQuickResults() {
        String searchString = "Java";

        WebDriverWait wait = new WebDriverWait(driver, 10);

        String searchBarPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[2]/div[1]/form[1]/div[1]/input[1]";
        WebElement searchBar = driver.findElement(By.xpath(searchBarPath));
        searchBar.sendKeys(searchString);

        String suggestionPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[2]/div[1]/form[1]/aside[1]/section[1]";
        WebElement suggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(suggestionPath)));
        List<WebElement> links = suggestion.findElement(By.className("search-list")).findElements(By.className("searchresults-item"));
        Assert.assertEquals("There're less than 4 elements in search result", 4, links.size());

        for (WebElement link : links) {
            String linkText = link.findElement(By.xpath("//aside[@id='ui-id-2']//div[@class='search-list']")).findElement(By.className("ui-menu-item")).getText();
            boolean containsSearchString = StringUtils.contains(linkText.toLowerCase(), searchString.toLowerCase());
            Assert.assertTrue("An element does not contain search string!", containsSearchString);
        }

        String productsPath = "/html[1]/body[1]/main[1]/header[1]/div[1]/div[2]/div[1]/form[1]/aside[1]/section[2]";
        WebElement products = driver.findElement(By.xpath(productsPath));
        List<WebElement> relatedProducts = products.findElement(By.className("related-content-products")).findElements(By.className("searchresults-item"));
        Assert.assertEquals("There is more or less than expected elements in related products section", 4, relatedProducts.size());

        for (WebElement relatedProduct : relatedProducts) {
            String objectText = relatedProduct.findElement(By.xpath("//aside[@id='ui-id-2']//div[@class='related-content-products']")).findElement(By.className("ui-menu-item")).getText();
            boolean containsSearchString = StringUtils.contains(objectText.toLowerCase(), searchString.toLowerCase());
            Assert.assertTrue("A product doesn't contain search string!", containsSearchString);
        }
    }

    @After
    public void tearDown(){
        driver.quit();
    }

}
