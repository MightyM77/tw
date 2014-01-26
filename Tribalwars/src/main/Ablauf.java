package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import tool.Overview;
import tool.Tribalwars;
import tool.farmassistant.Farmassistant;
import tool.headquarters.Building;
import tool.headquarters.Headquarters;
import utile.Helper;
import utile.ResourceBundleUtil;
import utile.Troop;

public class Ablauf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Tribalwars tw = new Tribalwars(driver);
		tw.goToSite();
		tw.login();

		Tester tester = new Tester(driver);
		tester.headquartersTest();
	}

}
