package main;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import main.farmassistent.Farmassistant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Ablauf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Tribalwars tw = new Tribalwars(driver);
		tw.goToSite();
		tw.login();

		Farmassistant fm = new Farmassistant(driver);
		fm.goToSite();
		
//		fm.getFarmEntries();
		
//		List<WebElement> ressis = fm.getRessis();
//		for (WebElement ressi : ressis) {
//			System.out.println(ressi.getText().replaceAll("[^0-9]", ""));
//		}
//		System.out.println(ressis.size());
//		WebElement[] disabledCbuttonsArray = new WebElement[ressis.size()];
//		Highlighter.getInstance().highlightElementsFlash(driver, ressis.toArray(disabledCbuttonsArray));
	}

}
