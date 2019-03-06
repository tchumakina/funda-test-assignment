package search;

import common.driver.DriverManager;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import web.elements.EuropeSearch;
import web.elements.result.ResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class EuropeTest {
    @ClassRule
    public static final TestResources res = new TestResources();

    private EuropeSearch europeSearch;

    @Before
    public void beforeEach() {
        DriverManager.openUrl();
        europeSearch = new EuropeSearch();
        europeSearch.clickNavigationItem("Europa");
    }

    @Test
    public void unavailableCountry() {
        europeSearch.chooseCountry("Finland");
        europeSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getResultsCount().getText()).contains("0 resultaten");
    }

    @Test
    public void availableCountry() {
        String country = "Duitsland";
        europeSearch.chooseCountry(country);
        europeSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(country);
    }

    @Test
    public void allResultsCount() {
        String resultsCount = europeSearch.chooseCountry("Alle landen");
        europeSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        String expectedResult = String.format("%s resultaten\nin europese woningen", resultsCount);
        assertThat(resultPage.getResultsCount().getText()).isEqualTo(expectedResult);
    }

    @Test
    public void changeCountry() {
        String country = "Frankrijk";
        europeSearch.chooseCountry("Duitsland");
        europeSearch.chooseCountry(country);
        europeSearch.getSearchButton().click();

        ResultPage resultPage = new ResultPage();
        assertThat(resultPage.getSearchResultContainer().getResultHeader().getSubtitle()).contains(country);
    }

}
