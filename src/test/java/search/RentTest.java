package search;

import common.driver.DriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import web.elements.BuySearch;
import web.elements.RentSearch;
import web.elements.result.ResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class RentTest {
    @ClassRule
    public static final TestResources res = new TestResources();

    private RentSearch rentSearch;

    @Before
    public void beforeEach() {
        DriverManager.openUrl();
        rentSearch = new RentSearch();
        rentSearch.clickNavigationItem("Huur");
    }

    @Test
    public void noFilters() {
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getResultsCount().isEnabled()).isTrue();
    }

    @Test
    public void setPlace() {
        String place = "Amsterdam";
        rentSearch.fillInSearch(place);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void setDistrict() {
        String district = "Centrum Almere Stad";
        rentSearch.fillInSearch(district);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains(district);
    }

    @Test
    public void setAddress() {
        String street = "Prinseneiland";
        rentSearch.fillInSearch(street);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getTitle()).contains(street);
    }

    // Place is out of Funda market, set distance to show apartments from near places in Netherlands
    @Test
    public void placeWithDistance() {
        String place = "Kranenburg Groningen";
        rentSearch.fillInSearch(place);
        rentSearch.selectDistance("15");
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains("Kranenburg");
        assertThat(resultPage.getSearchResultContainer()).isNotNull();
    }

    @Test
    public void minPrice() {
        String place = "Amsterdam";
        int price = 2000;
        rentSearch.fillInSearch(place);
        rentSearch.setMinPrice(Integer.toString(price));
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
        assertThat(resultPage.getSearchResultContainer().getResultPrice("/mnd") >= price).isTrue();
    }

    @Test
    public void maxPrice() {
        String place = "Amsterdam";
        int price = 700;
        rentSearch.fillInSearch(place);
        rentSearch.setMaxPrice(Integer.toString(price));
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
        assertThat(resultPage.getSearchResultContainer().getResultPrice("/mnd") <= price).isTrue();
    }

    @Test
    public void lastSearchLinkWithPlace() {
        String place = "Weesp";
        rentSearch.fillInSearch(place);
        rentSearch.getSearchButton().click();

        DriverManager.openUrl();
        rentSearch.clickNavigationItem("Huur");
        assertThat(rentSearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void lastSearchLinkFullFilter() {
        rentSearch.fillInSearch("Weesp");
        rentSearch.selectDistance("5");
        rentSearch.setMinPrice("800");
        rentSearch.setMaxPrice("1500");
        rentSearch.getSearchButton().click();

        DriverManager.openUrl();
        rentSearch.clickNavigationItem("Huur");
        String expectedResult = String.format("Weesp, +5km, %1$s 800 - %1$s 1.500", "\u20ac");
        assertThat(rentSearch.getLastSearchLink().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void caseInsensitive() {
        rentSearch.fillInSearch("GoUDa");
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains("Gouda");
    }

    @Test
    public void openLastSearchResult() {
        String place = "Diemen";
        rentSearch.fillInSearch(place);
        rentSearch.getSearchButton().click();

        DriverManager.openUrl();
        rentSearch.clickNavigationItem("Huur");
        rentSearch.getLastSearchLink().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void invalidMaxPrice() {
        rentSearch.setMinPrice("1750");
        rentSearch.setMaxPrice("600");

        assertThat(rentSearch.getMaxColor()).isEqualTo("#f03c30");
    }

    @Test
    public void invalidFilterValue() {
        String randomValue = RandomStringUtils.randomAlphabetic(8); // random string with len 8
        rentSearch.getSearchBox().sendKeys(randomValue);
        rentSearch.clickSearchButton();

        assertThat(rentSearch.getNoSuggestion().getText()).isEqualTo("Ai! Deze locatie kunnen we helaas niet vinden.");
    }

    @Test
    public void savedSearch() {
        String place = "Weesp";
        rentSearch.fillInSearch(place);
        rentSearch.getSearchButton().click();

        DriverManager.openUrl();
        rentSearch.clickNavigationItem("Koop");
        BuySearch buySearch = new BuySearch();
        buySearch.fillInSearch("Almere");
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        rentSearch.clickNavigationItem("Huur");
        assertThat(rentSearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void changePlace() {
        String place = "Amsterdam";
        rentSearch.fillInSearch("Weesp");
        rentSearch.fillInSearch(place);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void postCode() {
        String postCode = "1011 LZ";
        rentSearch.fillInSearch(postCode);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }

    @Test
    public void partialCode() {
        String postCode = "1382";
        rentSearch.fillInSearch(postCode);
        rentSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }
}
