package main;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebElementHelper {
	
	private WebDriver driver;
	// Wie lange soll versucht werden ein Element zu finden (in ms)
	private int retrySearch = 5000;
	
	private WebElementHelper(WebDriver pDriver) {
		this.driver = pDriver;
	}
	
	public boolean isPartOfClass(WebElement element, String className) {
		String[] classes = getClasses(element);
		return Arrays.asList(classes).contains(className);
	}
	
	public String[] getClasses(WebElement element) {
		String classes = element.getAttribute("class");
		return classes.split(" ");
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
