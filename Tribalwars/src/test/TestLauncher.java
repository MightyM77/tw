package test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestLauncher {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "Z:\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
	}
}