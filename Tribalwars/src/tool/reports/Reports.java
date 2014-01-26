package tool.reports;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;

import tool.Site;

public class Reports extends Site {

	public static final String SUBMENU_ALL_REPORTS = "all";
	public static final String SUBMENU_ATTACKS = "attack";
	public static final String SUBMENU_DEFENSES = "defense";
	public static final String SUBMENU_SUPPORT = "support";
	public static final String SUBMENU_TRADE = "trade";
	public static final String SUBMENU_MISCELLANEOUS = "other";
	public static final String SUBMENU_FORWARDED = "forwarded";
	public static final String SUBMENU_FILTER = "filter";
	public static final String SUBMENU_PUBLICIZED = "public";
	public static final String SUBMENU_FOLDERS = "groups";
	public static final String[] SUBMENUS = new String[] {SUBMENU_ALL_REPORTS, SUBMENU_ATTACKS, SUBMENU_DEFENSES, SUBMENU_SUPPORT, SUBMENU_MISCELLANEOUS,SUBMENU_FORWARDED,SUBMENU_PUBLICIZED, SUBMENU_FOLDERS};
	
	public static final int FOLDER_ALL = -1;
	public static final int FOLDER_NEW = 0;
	
	public Reports(WebDriver pDriver) {
		super("/game.php", "report", pDriver);
	}

	public void goToSubmenu(String submenuName) {
		if (Arrays.asList(Reports.SUBMENUS).contains(submenuName)) {
			if (!this.urlParameters.containsKey("mode") || !this.urlParameters.get("mode").equals(submenuName)) {
				this.urlParameters.put("mode", submenuName);
				goToSite();
			}
			this.urlParameters.put("mode", submenuName);
		} else {
			System.out.println("'" + submenuName + "' ist ein ungültiges Submenü");
		}
		
	}
	
	
}
