package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.ResourceBundleUtil;
import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestLauncher {

	public static void main(String[] args) {
		Locale currentLocale = new Locale("en", "US");
		// System.out.println(currentLocale);
		// System.out.println("Country: " + currentLocale.getCountry());
		// System.out.println("Language: " + currentLocale.getLanguage());
		// System.out.println("DiplayName:" + currentLocale.getDisplayName());
		// System.out.println("DiplayLanguage:" +
		// currentLocale.getDisplayLanguage());
		// System.out.println("DiplayScript:" +
		// currentLocale.getDisplayScript());
		// System.out.println("DiplayVariant:" +
		// currentLocale.getDisplayVariant());
		// System.out.println("ISO3Country:" + currentLocale.getISO3Country());
		// System.out.println("ISO3Language:" +
		// currentLocale.getISO3Language());
		// System.out.println("Script:" + currentLocale.getScript());
		// System.out.println("Variant:" + currentLocale.getVariant());
		// System.out.println("toLanguage:" + currentLocale.toLanguageTag());
		//
		// for (char extions : currentLocale.getExtensionKeys()) {
		// System.out.println(extions);
		// }
		

//		String text = " (637|548) K56";
//		String[] villageCoordArray = text.replaceAll("\\s+","").substring(1,8).split("\\|");
//		System.out.println(Arrays.toString(villageCoordArray));
//		Point villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));
//		System.out.println("Coords: " + villageCoord.x + "|" + villageCoord.y);
		
		
		
		// for (Locale locale : Locale.getAvailableLocales()) {
		// System.out.println(locale.getDisplayName());
		// }
		
		String lastFarmingTimeString = "today at 04:02:01";
		Date finalLastFarmingTime = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		String dateformat = ResourceBundleUtil.getFarmassistantBundleString("dateformat");
		String timeformat = ResourceBundleUtil.getFarmassistantBundleString("timeformat");
		String todayString = ResourceBundleUtil.getFarmassistantBundleString("today");
				
		dateFormat.applyPattern(dateformat);
		lastFarmingTimeString = lastFarmingTimeString.replace(todayString, " " + dateFormat.format(new Date()));
		System.out.println(lastFarmingTimeString);
		lastFarmingTimeString = lastFarmingTimeString.replaceAll("[A-Za-z]", "");
		lastFarmingTimeString = lastFarmingTimeString.replaceAll(" +", " ");
		lastFarmingTimeString = lastFarmingTimeString.substring(1);
		lastFarmingTimeString = lastFarmingTimeString.replace(" ", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		System.out.println(lastFarmingTimeString);
		
		try {
			dateFormat.applyPattern(dateformat + "yyyy" + timeformat);
			finalLastFarmingTime = dateFormat.parse(lastFarmingTimeString);
		} catch (ParseException e) {
			System.out.println("\"" + lastFarmingTimeString + "\" konnte nicht in ein Datum umgewandelt werden");
			e.printStackTrace();
		}
		
		dateFormat.applyPattern("dd.MM.yyyy hh:mm:ss");
		System.out.println(dateFormat.format(finalLastFarmingTime));
//		String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
		
		 
//		 testSite.goToSite();
//		 driver.findElement(By.id("asdfasgs"));
//		 List<WebElement> test = testSite.getSome();
//		 if (test.size() == 0) {
//			 System.out.println("kein element gefunden");
//		 } else {
//			 System.out.println(test.size() + " Element(e) gefunden");
//		 }
	}

	public TestLauncher() {

	}

}
