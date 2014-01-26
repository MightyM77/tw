package utile;

import java.util.ResourceBundle;

import config.Configuration;

public class ResourceBundleUtil {
	
	private static final String GENERAL_BUNDLE = "resourcebundle.GeneralBundle";
	private static final String FARMASSISTANT_BUNDLE = "resourcebundle.FarmassistantBundle";
	private static final String HEADQUARTERS_BUNDLE = "resourcebundle.HeadquartersBundle";
	
	private ResourceBundleUtil() {
		
	}
	
	public static String getGeneralBundleString(String key) {
		return ResourceBundle.getBundle(GENERAL_BUNDLE, Configuration.LOCALE).getString(key);
	}
	
	public static String getFarmassistantBundleString(String key) {
		return ResourceBundle.getBundle(FARMASSISTANT_BUNDLE, Configuration.LOCALE).getString(key);
	}
	
	public static String getHeadquartersBundleString(String key) {
		return ResourceBundle.getBundle(HEADQUARTERS_BUNDLE, Configuration.LOCALE).getString(key);
	}
}
