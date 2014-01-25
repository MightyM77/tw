package main;

import java.util.ResourceBundle;

import main.config.Configuration;

public class ResourceBundleUtil {
	
	private static final String GENERAL_BUNDLE = "main.resourcebundles.GeneralBundle";
	private static final String FARMASSISTANT_BUNDLE = "main.resourcebundles.FarmassistantBundle";
	
	private ResourceBundleUtil() {
		
	}
	
	public static String getGeneralBundleString(String key) {
		return ResourceBundle.getBundle(GENERAL_BUNDLE, Configuration.LOCALE).getString(key);
	}
	
	public static String getFarmassistantBundleString(String key) {
		return ResourceBundle.getBundle(FARMASSISTANT_BUNDLE, Configuration.LOCALE).getString(key);
	}
}
