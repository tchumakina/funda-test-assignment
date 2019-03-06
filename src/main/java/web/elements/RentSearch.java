package web.elements;

import common.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RentSearch extends BuySearch {

    @Override
    public WebElement getMaxPrice() {
        return DriverManager.getInstance().findElement(By.id("range-filter-selector-select-filter_huurprijstot"));
    }

    @Override
    public WebElement getMinPrice() {
        return DriverManager.getInstance().findElement(By.id("range-filter-selector-select-filter_huurprijsvan"));
    }

    @Override
    protected void loggingInitialisation() {
        System.out.println("DEBUG: Rent search component initialized");
    }
}
