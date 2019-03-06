package common.driver;

public class Synchronization {
    // Get source of element, wait 500ms and check that source is not updated, if some changes during this time -
    // wait until it is not stopped to be updated. Max 10 seconds.
    public static void waitPageSource() {
        try {
            for (int i = 0; i < 20; i++) {
                String currentSource = DriverManager.getInstance().getPageSource();
                Thread.sleep(500);
                String newSource = DriverManager.getInstance().getPageSource();
                if (currentSource.equals(newSource))
                    break;
                else
                    Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {
            System.out.println("DEBUG: Page source was not updated during 10 seconds.");
        }
    }
}
