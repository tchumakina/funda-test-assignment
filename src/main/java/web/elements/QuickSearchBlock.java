package web.elements;

import common.driver.DriverManager;
import common.driver.Synchronization;
import common.driver.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class QuickSearchBlock {

    @FindBy(className = "search-block__navigation-items")
    private WebElement navigationItems;

    @FindBy(xpath = "//div[@class='search-block__submit']/button")
    private WebElement searchButton;

    public WebElement getSearchButton() {
        return searchButton;
    }

    public void clickSearchButton() {
        searchButton.click();
        Synchronization.waitPageSource();
    }

    QuickSearchBlock() {
        PageFactory.initElements(DriverManager.getInstance(), this);
        WebDriverHelper.waitForElement(By.className("search-block__form"));
        loggingInitialisation();
    }

    public void clickNavigationItem(String element) {
        String path = String.format("//a[contains(text(), '%s')]", element);
        try {
            navigationItems.findElement(By.xpath(path)).click();
            Synchronization.waitPageSource();
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Cannot find element with xpath '%s'.", path));
        }
    }

    protected abstract void loggingInitialisation();
}
