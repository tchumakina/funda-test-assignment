package search;

import common.driver.DriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import web.elements.BuySearch;
import web.elements.RecreationSearch;
import web.elements.result.ResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class RecreationTest {
    @ClassRule
    public static final TestResources res = new TestResources();

    private RecreationSearch recreationSearch;

    @Before
    public void beforeEach() {
        DriverManager.openUrl();
        recreationSearch = new RecreationSearch();
        recreationSearch.clickNavigationItem("Recreatie");
    }


    @Test
    public void noFilters() {
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getResultsCount().isEnabled()).isTrue();
    }

    @Test
    public void setPlace() {
        String place = "Vijfhuizen";
        recreationSearch.fillInSearch(place);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void setDistrict() {
        String district = "Centrum Almere Stad";
        recreationSearch.fillInSearch(district);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains(district);
    }

    @Test
    public void setAddress() {
        String street = "Zonnehoek";
        recreationSearch.fillInSearch(street);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getTitle()).contains(street);
    }

    // There are no results in this place, but with adding of distance results are shown.
    @Test
    public void placeWithDistance() {
        String place = "Haarlem";
        recreationSearch.fillInSearch(place);
        recreationSearch.selectDistance("10");
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer()).isNotNull();
    }

    @Test
    public void resultsCount() {
        String resultsCount = recreationSearch.fillInSearch("Holten");
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        String expectedResult = String.format("%s resultaten\nin recreatiewoningen", resultsCount);
        assertThat(resultPage.getResultsCount().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void invalidFilterValue() {
        String randomValue = RandomStringUtils.randomAlphabetic(8); // random string with len 8
        recreationSearch.getSearchBox().sendKeys(randomValue);
        recreationSearch.clickSearchButton();

        assertThat(recreationSearch.getNoSuggestion().getText()).isEqualTo("Ai! Deze locatie kunnen we helaas niet vinden.");
    }

    @Test
    public void caseInsensitive() {
        recreationSearch.fillInSearch("HaArLE");
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains("Haarle");
    }

    @Test
    public void savedSearch() {
        String place = "Oisterwijk";
        recreationSearch.fillInSearch(place);
        recreationSearch.getSearchButton().click();

        DriverManager.openUrl();
        recreationSearch.clickNavigationItem("Koop");
        BuySearch buySearch = new BuySearch();
        buySearch.fillInSearch("Almere");
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        recreationSearch.clickNavigationItem("Recreatie");
        assertThat(recreationSearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void changePlace() {
        String place = "Geesbrug";
        recreationSearch.fillInSearch("Lathum");
        recreationSearch.fillInSearch(place);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void postCode() {
        String postCode = "6097 HA";
        recreationSearch.fillInSearch(postCode);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }

    @Test
    public void partialCode() {
        String postCode = "6097";
        recreationSearch.fillInSearch(postCode);
        recreationSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }
}
