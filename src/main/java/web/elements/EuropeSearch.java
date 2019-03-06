package web.elements;

import common.driver.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class EuropeSearch extends QuickSearchBlock {

    @Override
    protected void loggingInitialisation() {
        System.out.println("DEBUG: Europe search component initialized");
    }

    public String chooseCountry(String country) {
        WebDriverHelper.findElement(By.className("selected-option")).click();

        By findCountryBy = By.xpath(String.format("//ul[@class='selectbox-list is-open']//span[text()='%s']/..", country));
        WebElement countryOption = WebDriverHelper.findElement(findCountryBy);

        String resultCount;
        try {
            resultCount = countryOption.findElement(By.xpath("./div")).getText();
        } catch (NoSuchElementException ignored) {
            System.out.println(String.format("Cannot identify number of results for '%s' country, set to 0.", country));
            resultCount = "0";
        }
        countryOption.findElement(By.xpath("./span")).click();
        return resultCount;
    }
}
