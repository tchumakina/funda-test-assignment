package web.elements.result;

import common.driver.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class FilterList {
    private final WebElement listElement;

    public List<String> getFilters() {
        List<WebElement> getFilterGroup = listElement.findElements(By.className("button-applied-filter"));
        return getFilterGroup.stream()
                .map(x -> x.getText().replace("Verwijder", "").trim())
                .collect(Collectors.toList());
    }

    public FilterList() {
        listElement = WebDriverHelper.findElement(By.className("applied-filter-list"));
    }
}
