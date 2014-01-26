package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import utile.Highlighter;
import utile.ResourceBundleUtil;
import config.Configuration;

public class TestLauncher {

	public static void main(String[] args) {
		WebDriver driver = new FirefoxDriver();
		Google google = new Google(driver);
		google.goToSite();
		List<WebElement> elements = driver.findElements(By.xpath("//button[starts-with(@id,'gbqfb')]"));
		for (WebElement element : elements) {
			Highlighter.getInstance().highlightElementFlash(driver, element);
		}
	}
}