package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Site {

	private String relativeUrl;
	private WebDriver driver;

	protected Map<String, String> urlParameters = new HashMap<String, String>();
	
	public Site(String pUrl, WebDriver pDriver) {
		this.relativeUrl = pUrl;
		this.driver = pDriver;
	}
	
	protected WebDriver driver() {
		return this.driver;
	}

	public void goToSite() {
		String url = Configuration.LOCALE.getCountry() + Configuration.WORLD
				+ "." + ResourceBundle.getBundle("main.resourcebundles.GeneralBundle", Configuration.LOCALE).getString("hostname") + this.relativeUrl;
		
		Iterator<Entry<String, String>> iterator = urlParameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) iterator.next();
			url += "&" + pairs.getKey() + "=" + pairs.getValue();
		}
		
		this.driver.get(url);
	}

	public Map<String, String> getCurrentUrlsParameters() {
		Map<String, String> currentUrlParameters = new HashMap<String, String>();
		String[] currentUrlParametersString = driver.getCurrentUrl().split("\\?")[1].split("&");
		String[] keyVal;
		for (String parameter : currentUrlParametersString) {
			keyVal = parameter.split("=");
			currentUrlParameters.put(keyVal[0], keyVal[1]);
		}
		return currentUrlParameters;
	}
	
	
	protected WebElement findElement(By by) {
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

	protected List<WebElement> findElements(By by) {
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
