package utile;

import java.util.Locale;
import java.util.ResourceBundle;

import config.TwConfiguration;

public class ResourceBundleUtil {
	
	private static final String GENERAL_BUNDLE = "resourcebundle.GeneralBundle";
	private static final String FARMASSISTANT_BUNDLE = "resourcebundle.FarmassistantBundle";
	private static final String HEADQUARTERS_BUNDLE = "resourcebundle.HeadquartersBundle";
	private static final String REPORTS_BUNDLE = "resourcebundle.ReportsBundle";
	private static final String BARRACKS_BUNDLE = "resourcebundle.BarracksBundle";
	private static final String INCOMING_BUNDLE = "resourcebundle.IncomingBundle";
	
	private ResourceBundleUtil() {
		// HIDDEN CLASS
	}
	
	public static String getGeneralBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(GENERAL_BUNDLE, locale).getString(key);
	}
	
	public static String getFarmassistantBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(FARMASSISTANT_BUNDLE, locale).getString(key);
	}
	
	public static String getHeadquartersBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(HEADQUARTERS_BUNDLE, locale).getString(key);
	}
	
	public static String getReportsBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(REPORTS_BUNDLE, locale).getString(key);
	}
	
	public static String getBarracksBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(BARRACKS_BUNDLE, locale).getString(key);
	}
	
	public static String getIncomingBundleString(String key, Locale locale) {
		return ResourceBundle.getBundle(INCOMING_BUNDLE, locale).getString(key);
	}
}
