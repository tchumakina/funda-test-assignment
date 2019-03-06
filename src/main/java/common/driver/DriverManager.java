package common.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Optional;

public class DriverManager {
    private static WebDriver driver;

    private static WebDriver initDriver() {
        String resourcePath;
        String browser = Optional.ofNullable(System.getProperty("browser")).orElse("chrome");

        switch (browser) {
            case "firefox":
            case "ff":
                resourcePath = WebDriver.class.getResource("/geckodriver.exe").getPath();
                System.setProperty("webdriver.gecko.driver", resourcePath);
                return new FirefoxDriver();
            case "chrome":
            default:
                resourcePath = WebDriver.class.getResource("/chromedriver.exe").getPath();
                System.setProperty("webdriver.chrome.driver", resourcePath);
                return new ChromeDriver();
        }
    }

    public static WebDriver getInstance() {
        if (driver == null) {
            driver = initDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void closeDriver() {
        driver.close();
        driver = null;
    }

    public static void openUrl() {
        getInstance().navigate().to("https://www.funda.nl");
    }
}
