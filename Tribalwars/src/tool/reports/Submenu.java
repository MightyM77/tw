package tool.reports;

/**
 * Die verschiedenen Untermenüs, die einem auf der Berichtsseite zur Verfügung stehen.
 * submenuName entspricht dem 'mode'-Parameterwert, der in der URL angegeben wird.
 * @author Mike_2
 */
public enum Submenu {

	SUBMENU_ALL_REPORTS ("all"),
	SUBMENU_ATTACKS("attack"),
	SUBMENU_DEFENSES("defense"),
	SUBMENU_SUPPORT("support"),
	SUBMENU_TRADE ("trade"),
	SUBMENU_MISCELLANEOUS ("other"),
	SUBMENU_FORWARDED ("forwarded"),
	SUBMENU_FILTER ("filter"),
	SUBMENU_PUBLICIZED ("public"),
	SUBMENU_FOLDERS ("groups");
	
	private String submenuName;
	
	private Submenu(String pSubmenuName) {
		this.submenuName = pSubmenuName;
	}
	
	public static boolean contains(String submenuName) {
		for (Submenu submenu : Submenu.values()) {
			if (submenu.getSubmenuName().equals(submenuName)) {
				return true;
			}
		}
		return false;
	}
	
	public String getSubmenuName() {
		return this.submenuName;
	}
}
