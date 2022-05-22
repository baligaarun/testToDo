package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/** Class to create and manage WebDriver instance.
 */
public class Driver {
	static private WebDriver driver;
	private Driver(){
    }
	
	public static WebDriver getDriver(String browser){
        if (driver == null) {
        	//Define the driver based on the browser type.
            switch (browser){
                case "chrome":
                    driver = new ChromeDriver();
                    System.setProperty("webdriver.gecko.driver", "chromedriver.exe");
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
                    break;
            }
        }
        driver.manage().window().maximize();
        System.out.println("Driver returned for " + browser);
        return driver;
    }

    public static void closeDriver(){
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("Driver closed successfully.");
        }
    }
}
