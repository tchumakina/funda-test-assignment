package web.elements.result;

import common.driver.DriverManager;
import common.driver.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(className = "search-output-result-count")
    private WebElement resultsCount;

    private final SearchResultContainer searchResultContainer;
    private final FilterList filterList;

    public SearchResultContainer getSearchResultContainer() {
        return searchResultContainer;
    }

    public FilterList getFilterList() {
        return filterList;
    }

    public WebElement getResultsCount() {
        return resultsCount;
    }

    public ResultPage() {
        PageFactory.initElements(DriverManager.getInstance(), this);
        searchResultContainer = new SearchResultContainer();
        filterList = new FilterList();
        WebDriverHelper.waitForElement(By.className("search-main"));
        System.out.println("DEBUG: result page initialized");
    }
}
