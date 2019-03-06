package search;

import common.driver.DriverManager;
import org.junit.rules.ExternalResource;

public class TestResources extends ExternalResource {
    public void after() {
        DriverManager.closeDriver();
    }
}
