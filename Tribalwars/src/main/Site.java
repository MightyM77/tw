package main;

import java.util.List;
import java.util.ResourceBundle;

import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Site {

	protected String relativeUrl;
	protected WebDriver driver;

	public Site(String pUrl, WebDriver pDriver) {
		this.relativeUrl = pUrl;
		this.driver = pDriver;
	}

	public void goToSite() {
		String url = Configuration.LOCALE.getCountry() + Configuration.WORLD
				+ "." + ResourceBundle.getBundle("main.resourcebundles.GeneralBundle", Configuration.LOCALE).getString("hostname") + this.relativeUrl;
		this.driver.get(url);
	}

	public WebElement findElement(By by) {
		WebElement results = null;
		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			results = driver.findElement(by);
			if (results.isDisplayed()) {
				break;
			}
		}
		return results;
	}

	public List<WebElement> findElements(By by) {
		List<WebElement> results = null;
		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			results = driver.findElements(by);
			if (results.size() > 0) {
				break;
			}
		}
		return results;
	}
}
