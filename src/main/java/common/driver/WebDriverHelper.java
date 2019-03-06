package common.driver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class WebDriverHelper {
    public static void waitForElement(By byLocator) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getInstance(), 5);
            wait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Element with path '%s' is not found.", byLocator));
        }
    }

    public static WebElement findElement(By by) {
        return findElement(DriverManager.getInstance(), by, 5);
    }

    public static WebElement findElement(SearchContext el, By by, int timeoutInSeconds) {
        WebDriverWait waiter = new WebDriverWait(DriverManager.getInstance(), timeoutInSeconds);
        waiter.until(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = el.findElement(by);
        try {
            scrollWithJS(element);
        } catch (Exception ex) {
            System.out.println(String.format("DEBUG: Cannot set focus to element with path '%s': %s", by, ex.getMessage()));
        } // try to make focus
        return element;
    }

    private static void scrollWithJS(WebElement element) {
        ((JavascriptExecutor) DriverManager.getInstance()).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public static void copyPaste(String value) {
        StringSelection stringSelection = new StringSelection(value);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
