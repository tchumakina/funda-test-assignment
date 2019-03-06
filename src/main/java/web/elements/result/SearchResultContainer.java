package web.elements.result;

import common.driver.DriverManager;
import common.driver.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultContainer {
    private final ResultHeader resultHeader;

    @FindBy(className = "search-result-price")
    private WebElement resultPrice;

    public ResultHeader getResultHeader() {
        return resultHeader;
    }

    public class ResultHeader {
        @FindBy(className = "search-result-header")
        private WebElement resultHeader;

        ResultHeader() {
            PageFactory.initElements(DriverManager.getInstance(), this);
        }

        public String getTitle() {
            return WebDriverHelper.findElement(resultHeader, By.tagName("h3"), 5).getText().trim();
        }

        public String getSubtitle() {
            return WebDriverHelper.findElement(resultHeader, By.tagName("small"), 5).getText().trim();
        }
    }

    public SearchResultContainer() {
        PageFactory.initElements(DriverManager.getInstance(), this);
        this.resultHeader = new ResultHeader();
    }

    public int getResultPrice(String unit) {
        String priceText = resultPrice.getText()
                .replace("\u20ac", "").replace(unit, "")
                .replace(".", "").trim();
        return Integer.parseInt(priceText);
    }
}
