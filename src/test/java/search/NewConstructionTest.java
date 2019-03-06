package search;

import common.driver.DriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import web.elements.BuySearch;
import web.elements.NewConstructionSearch;
import web.elements.result.ResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class NewConstructionTest {
    @ClassRule
    public static final TestResources res = new TestResources();

    private NewConstructionSearch newConstructionSearch;

    @Before
    public void beforeEach() {
        DriverManager.openUrl();
        newConstructionSearch = new NewConstructionSearch();
        newConstructionSearch.clickNavigationItem("Nieuwbouw");
    }

    @Test
    public void noFilters() {
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getResultsCount().isEnabled()).isTrue();
    }

    @Test
    public void setPlace() {
        String place = "Amsterdam";
        newConstructionSearch.fillInSearch(place);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void setDistrict() {
        String district = "Centrum Almere Stad";
        newConstructionSearch.fillInSearch(district);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getFilterList().getFilters()).contains(district);
    }

    @Test
    public void setAddress() {
        String project = "Bold Sky";
        newConstructionSearch.fillInSearch(project);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getTitle()).contains(project);
    }

    // Place is out of Funda market, set distance to show apartments from near places in Netherlands
    @Test
    public void placeWithDistance() {
        String place = "Ven-Zelderheide";
        newConstructionSearch.fillInSearch(place);
        newConstructionSearch.selectDistance("15");
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer()).isNotNull();
    }

    @Test
    public void lastSearchLinkWithPlace() {
        newConstructionSearch.fillInSearch("Hilversum");
        newConstructionSearch.selectDistance("2");
        newConstructionSearch.getSearchButton().click();

        DriverManager.openUrl();
        newConstructionSearch.clickNavigationItem("Nieuwbouw");
        String expectedResult = "Hilversum, +2km";
        assertThat(newConstructionSearch.getLastSearchLink().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void openLastSearchResult() {
        String place = "Hilversum";
        newConstructionSearch.fillInSearch(place);
        newConstructionSearch.getSearchButton().click();

        DriverManager.openUrl();
        newConstructionSearch.clickNavigationItem("Nieuwbouw");
        newConstructionSearch.getLastSearchLink().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void resultsCount() {
        String resultsCount = newConstructionSearch.fillInSearch("Laren");
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        String expectedResult = String.format("%s resultaten\nin nieuwbouwprojecten", resultsCount);
        assertThat(resultPage.getResultsCount().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void invalidFilterValue() {
        String randomValue = RandomStringUtils.randomAlphabetic(8); // random string with len 8
        newConstructionSearch.getSearchBox().sendKeys(randomValue);
        newConstructionSearch.clickSearchButton();

        assertThat(newConstructionSearch.getNoSuggestion().getText()).isEqualTo("Ai! Deze locatie kunnen we helaas niet vinden.");
    }

    @Test
    public void caseInsensitive() {
        newConstructionSearch.fillInSearch("GoUDa");
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains("Gouda");
    }

    @Test
    public void savedSearch() {
        String place = "Amsterdam";
        newConstructionSearch.fillInSearch(place);
        newConstructionSearch.getSearchButton().click();

        DriverManager.openUrl();
        newConstructionSearch.clickNavigationItem("Koop");
        BuySearch buySearch = new BuySearch();
        buySearch.fillInSearch("Almere");
        buySearch.getSearchButton().click();

        DriverManager.openUrl();
        newConstructionSearch.clickNavigationItem("Nieuwbouw");
        assertThat(newConstructionSearch.getLastSearchLink().getText()).isEqualTo(place);
    }

    @Test
    public void changePlace() {
        String place = "Amsterdam";
        newConstructionSearch.fillInSearch("Den Haag");
        newConstructionSearch.fillInSearch(place);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(place);
    }

    @Test
    public void postCode() {
        String postCode = "2517 KH";
        newConstructionSearch.fillInSearch(postCode);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }

    @Test
    public void partialCode() {
        String postCode = "2516";
        newConstructionSearch.fillInSearch(postCode);
        newConstructionSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(postCode);
    }
}
