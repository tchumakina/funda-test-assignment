package web.elements;

import common.driver.DriverManager;
import common.driver.Synchronization;
import common.driver.WebDriverHelper;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class RecreationSearch extends QuickSearchBlock {

    @FindBy(id = "autocomplete-input")
    private WebElement searchBox;

    @FindBy(className = "autocomplete-list-outer")
    private WebElement autocompleteList;

    @FindBy(id = "Straal")
    private WebElement distance;

    @FindBy(className = "link-alternative")
    private WebElement lastSearchLink;

    public WebElement getLastSearchLink() {
        return this.lastSearchLink;
    }

    public WebElement getSearchBox() {
        return this.searchBox;
    }

    public WebElement getNoSuggestion() {
        By findBy = By.className("autocomplete-no-suggestion-message");
        WebDriverHelper.waitForElement(findBy);
        return DriverManager.getInstance().findElement(findBy);
    }

    public RecreationSearch() {
        PageFactory.initElements(DriverManager.getInstance(), this);
    }

    @Override
    protected void loggingInitialisation() {
        System.out.println("DEBUG: Recreation search component initialized");
    }

    public String fillInSearch(String value) {
        // searchBox.sendKeys(value); works not stable in Huur / Nieuwbouw pages
        // emulate click to filed + paste text into search field
        searchBox.clear();
        searchBox.click();
        WebDriverHelper.copyPaste(value);

        // select the first type ahead from the list that contains the first word from value
        String valueToFind = value.split(" ")[0];
        valueToFind = StringUtils.capitalize(valueToFind.toLowerCase());
        String path = String.format("//span[contains(text(), '%s')]", valueToFind);
        String resultsCount = "0";

        try {
            WebElement elementToClick = WebDriverHelper.findElement(autocompleteList, By.xpath(path), 5);
            try {
                resultsCount = elementToClick.findElement(By.xpath("./../../div[@class='autocomplete-count']")).getText();
        } catch (NoSuchElementException ex) {
                System.out.println(String.format("DEBUG: cannot identify number of results for '%s': %s", valueToFind, ex.getMessage()));
            }
            elementToClick.click();
            Synchronization.waitPageSource();
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Cannot click on typeahead value '%s': %s.", valueToFind, ex.getMessage()));
        }

        return resultsCount;
    }

    public void selectDistance(String value) {
        new Select(distance).selectByValue(value);
    }
}
