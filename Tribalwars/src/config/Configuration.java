package config;

import java.util.Locale;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import utile.ResourceBundleUtil;

public class Configuration {

	public static final Locale LOCALE = Locale.US;
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(ResourceBundleUtil.getGeneralBundleString("timezone"));
	public static final int WORLD = 13;
	public static final String USERNAME = "MightyM";
	public static final String PASSWORD = "dakommstenetrein";
	
	public static final WebDriver DRIVER = new FirefoxDriver();
	
	private Configuration() {
		
	}
	
	static {
		TimeZone.setDefault(TIME_ZONE);
	}
}