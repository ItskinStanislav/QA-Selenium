import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;


public class SearchTests {
    private WebDriver driver;

    @Before
    public void setup() {
        String homepageLink = "https://www.wiley.com/en-us";
        this.driver = Utilities.webDriverForBaseUrl(homepageLink);
        Utilities.closeWileyPopupWindow(driver);
    }

    //Task #8
    @Test
    public void testSearchFromHomepage() {
        String searchString = "Java";

        doSearchForString(searchString);

        String sidePanelPath = "/html[1]/body[1]/main[1]/div[3]/div[1]/div[1]/div[1]/div[3]/div[1]/div[3]/div[1]/div[1]/div[1]/div[3]";
        WebElement sidePanel = driver.findElement(By.xpath(sidePanelPath));
        List<WebElement> links = sidePanel.findElements(By.className("product-item"));
        Assert.assertEquals("There're not 10 elements in products list", 10, links.size());

        for (WebElement link : links) {
            String linkText = link.findElement(By.className("product-title")).findElement(By.tagName("a")).getText().toLowerCase();
            boolean containsSearchString = StringUtils.contains(linkText.toLowerCase(), searchString.toLowerCase());
            Assert.assertTrue("An element does not contain search string!", containsSearchString);
            WebElement addToCartButton = link.findElement(By.className("product-button")).findElement(By.className("small-button"));

            Assert.assertTrue("An elements does not contain 'add to cart' button", addToCartButton.isDisplayed());
        }
    }

    //Task #9
    @Test
    public void testSearchFromSearchPage() {
        String searchString = "Java";

        doSearchForString(searchString);
        String[] originalTitles = findSearchTitles();

        doSearchForString(searchString);
        String[] newTitles = findSearchTitles();

        Assert.assertArrayEquals("Titles are mismatched", originalTitles, newTitles);
    }

    private String[] findSearchTitles() {
        String sidePanelPath = "/html[1]/body[1]/main[1]/div[3]/div[1]/div[1]/div[1]/div[3]/div[1]/div[3]/div[1]/div[1]/div[1]/div[3]";
        WebElement sidePanel = driver.findElement(By.xpath(sidePanelPath));
        List<WebElement> links = sidePanel.findElements(By.className("product-item"));

        return links.stream()
                .map(link -> (link.findElement(By.className("product-title")).findElement(By.tagName("a")).getText()))
                .collect(Collectors.toList())
                .toArray(new String[links.size()]);
    }

    private void doSearchForString(String query) {
        String searchBarPath = "//input[@id='js-site-search-input']";
        WebElement searchBar = driver.findElement(By.xpath(searchBarPath));
        searchBar.clear();
        searchBar.sendKeys(query);

        String searchClickPath = "//button[contains(text(),'Search')]";
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement searchClick = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchClickPath)));
        searchClick.click();

        Utilities.waitForPageToLoad(driver);
    }

    @After
    public void tearDown(){
        driver.quit();
    }
}
