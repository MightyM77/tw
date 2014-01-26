package config;

import java.util.Locale;
import java.util.TimeZone;

import utile.ResourceBundleUtil;

public class Configuration {

	public static final Locale LOCALE = Locale.US;
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone(ResourceBundleUtil.getGeneralBundleString("timezone"));
	public static final int WORLD = 13;
	public static final String USERNAME = "MightyM";
	public static final String PASSWORD = "dakommstenetrein";
	
	private Configuration() {
		
	}
	
	static {
		TimeZone.setDefault(TIME_ZONE);
	}
}