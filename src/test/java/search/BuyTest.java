package search;

import common.driver.DriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import web.elements.BuySearch;
import web.elements.RentSearch;
import web.elements.result.ResultPage;

import static org.assertj.core.api.Assertions.*;

public class BuyTest {
    @ClassRule
    public static final TestResources res = new TestResources();

    private BuySearch buySearch;

    @Before
    public void beforeEach() {
        DriverManager.openUrl();
        buySearch = new BuySearch();
    }

    @Test
    public void noFilters() {
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getResultsCount().isEnabled()).isTrue();
    }

    @Test
    public void setPlace() {
        String place = "Amsterdam";
        buySearch.fillInSearch(place);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void setDistrict() {
        String district = "Centrum Almere Stad";
        buySearch.fillInSearch(district);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains(district);
    }

    @Test
    public void setAddress() {
        String street = "Prinseneiland";
        buySearch.fillInSearch(street);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getTitle()).contains(street);
    }

    // Place is out of Funda market, set distance to show apartments from near places in Netherlands
    @Test
    public void placeWithDistance() {
        String place = "Kranenburg Groningen";
        buySearch.fillInSearch(place);
        buySearch.selectDistance("15");
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains("Kranenburg");
        assertThat(resultPage.getSearchResultContainer()).isNotNull();

    }

    @Test
    public void minPrice() {
        String place = "Amsterdam";
        int price = 1000000;
        buySearch.fillInSearch(place);
        buySearch.setMinPrice(Integer.toString(price));
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
        assertThat(resultPage.getSearchResultContainer().getResultPrice("k.k.") >= price).isTrue();
    }

    @Test
    public void maxPrice() {
        String place = "Amsterdam";
        int price = 200000;
        buySearch.fillInSearch(place);
        buySearch.setMaxPrice(Integer.toString(price));
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
        assertThat(resultPage.getSearchResultContainer().getResultPrice("k.k.") <= price).isTrue();
    }


    @Test
    public void lastSearchLinkWithPlace() {
        String place = "Weesp";
        buySearch.fillInSearch(place);
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        assertThat(buySearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void lastSearchLinkFullFilter() {
        buySearch.fillInSearch("Weesp");
        buySearch.selectDistance("5");
        buySearch.setMinPrice("200000");
        buySearch.setMaxPrice("300000");
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        String expectedResult = String.format("Weesp, +5km, %1$s 200.000 - %1$s 300.000", "\u20ac");
        assertThat(buySearch.getLastSearchLink().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void openLastSearchResult() {
        String place = "Diemen";
        buySearch.fillInSearch(place);
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        buySearch.getLastSearchLink().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void caseInsensitive() {
        buySearch.fillInSearch("GoUDa");
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains("Gouda");
    }

    @Test
    public void invalidMaxPrice() {
        buySearch.setMinPrice("500000");
        buySearch.setMaxPrice("200000");

        assertThat(buySearch.getMaxColor()).isEqualTo("#f03c30");
    }

    @Test
    public void invalidFilterValue() {
        String randomValue = RandomStringUtils.randomAlphabetic(8); // random string with len 8
        buySearch.getSearchBox().sendKeys(randomValue);
        buySearch.clickSearchButton();

        assertThat(buySearch.getNoSuggestion().getText()).isEqualTo("Ai! Deze locatie kunnen we helaas niet vinden.");
    }

    @Test
    public void savedSearch() {
        String place = "Weesp";
        buySearch.fillInSearch(place);
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        buySearch.clickNavigationItem("Huur");
        RentSearch rentSearch = new RentSearch();
        rentSearch.fillInSearch("Almere");
        rentSearch.getSearchButton().click();

        DriverManager.openUrl();
        assertThat(buySearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void changePlace() {
        String place = "Amsterdam";
        buySearch.fillInSearch("Weesp");
        buySearch.fillInSearch(place);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void postCode() {
        String postCode = "1011 HT";
        buySearch.fillInSearch(postCode);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }

    @Test
    public void partialCode() {
        String postCode = "1382";
        buySearch.fillInSearch(postCode);
        buySearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }
}
