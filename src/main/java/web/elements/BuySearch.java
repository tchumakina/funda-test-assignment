package web.elements;

import common.driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class BuySearch extends NewConstructionSearch {

    @FindBy(id = "range-filter-selector-select-filter_koopprijsvan")
    private WebElement minPrice;

    @FindBy(id = "range-filter-selector-select-filter_koopprijstot")
    private WebElement maxPrice;

    WebElement getMinPrice() {
        return this.minPrice;
    }

    WebElement getMaxPrice() {
        return this.maxPrice;
    }

    public BuySearch() {
        PageFactory.initElements(DriverManager.getInstance(), this);
    }

    public void setMinPrice(String value) {
        try {
            new Select(this.getMinPrice()).selectByValue(value);
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Cannot select value '%s' for min price: %s", value, ex.getMessage()));
        }
    }

    public void setMaxPrice(String value) {
        try {
            new Select(this.getMaxPrice()).selectByValue(value);
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Cannot select value '%s' for max price: %s", value, ex.getMessage()));
        }
    }

    public String getMaxColor() {
        return "#" + Integer.toHexString(new Color(240, 60, 48, 1).getColor().getRGB()).substring(2);
    }

    @Override
    protected void loggingInitialisation() {
        System.out.println("DEBUG: Buy search component initialized");
    }
}
