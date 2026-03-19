package driver;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class ChromeDriverFactory {
	private ChromeDriverFactory() {
	}

	public static WebDriver create() {
		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("windows")) {
			System.setProperty("webdriver.chrome.driver", "..\\Driver\\chromedriver_win32\\chromedriver.exe");
		} else {
			File chromedriverFile = new File("../Driver/chromedriver_linux/chromedriver");
			if (chromedriverFile.exists()) {
				System.setProperty("webdriver.chrome.driver", chromedriverFile.getAbsolutePath());
			} else {
				chromedriverFile = new File(System.getProperty("user.home") + "/App/Driver/chromedriver_linux/chromedriver");
				if (chromedriverFile.exists()) {
					System.setProperty("webdriver.chrome.driver", chromedriverFile.getAbsolutePath());
				}
			}
		}
		return new ChromeDriver();
	}
}