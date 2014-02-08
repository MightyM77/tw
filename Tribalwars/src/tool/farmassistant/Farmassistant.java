package tool.farmassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tool.Site;
import utile.ResourceBundleUtil;
import utile.Troop;
import utile.TroopTemplate;

public class Farmassistant extends Site {

	private static final Farmassistant INSTANCE = new Farmassistant();

	private Farmassistant() {
		super("/game.php", "am_farm");
	}

	public static Farmassistant getInstance() {
		return Farmassistant.INSTANCE;
	}

	private WebElement getFarmEntriesTable() {
		WebElement entriesFatherDiv = findElement(By.id("am_widget_Farm"));
		return entriesFatherDiv.findElement(By.tagName("table"));
	}

	private void setTemplateTroops(int templateNumber, Map<String, Integer> troopAmounts) {
		WebElement form = findElements(By.tagName("form")).get(templateNumber);

		Iterator<Entry<String, Integer>> iterator = troopAmounts.entrySet().iterator();
		WebElement input;
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			input = form.findElement(By.xpath(".//input[@name='" + pairs.getKey() + "']"));
			input.click();
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), pairs.getValue().toString());
		}

		form.findElement(By.xpath(".//input[@value='" + ResourceBundleUtil.getFarmassistantBundleString("save") + "']")).click();
		;
	}

	private void setTemplateTroops(int templateNumber, int spear, int sword, int axe, int archer, int spy, int light, int marcher, int heavy, int knight) {
		Map<String, Integer> troopAmounts = new HashMap<String, Integer>();
		troopAmounts.put(Troop.SPEAR.getId(), spear);
		troopAmounts.put(Troop.SWORD.getId(), sword);
		troopAmounts.put(Troop.AXE.getId(), axe);
		troopAmounts.put(Troop.ARCHER.getId(), archer);
		troopAmounts.put(Troop.SPY.getId(), spy);
		troopAmounts.put(Troop.LIGHT.getId(), light);
		troopAmounts.put(Troop.MARCHER.getId(), marcher);
		troopAmounts.put(Troop.HEAVY.getId(), heavy);
		troopAmounts.put(Troop.PALADIN.getId(), knight);
		setTemplateTroops(templateNumber, troopAmounts);
	}

	private void sort(String sortBy, String sortModus) {
		this.urlParameters.put("order", sortBy);
		this.urlParameters.put("dir", sortModus);
		goToSite();
	}

	private void sortByDate(String sortModus) {
		sort("date", sortModus);
	}

	private void sortByDistance(String sortModus) {
		sort("distance", sortModus);
	}

	private void setCheckboxState(WebElement checkbox, boolean state) {
		if (state && !checkbox.isSelected()) {
			checkbox.click();
		} else if (!state && checkbox.isSelected()) {
			checkbox.click();
		}
	}

	private int getTemplateHaulCapacity(int templateNumber) {
		List<WebElement> tds = findElements(By.tagName("form")).get(templateNumber).findElements(By.tagName("tr")).get(1).findElements(By.tagName("td"));
		return Integer.valueOf(tds.get(tds.size() - 1).getText().replaceAll("[^0-9]", ""));
	}

	public int getAtemplateHaulCapacity() {
		return getTemplateHaulCapacity(0);
	}

	public int getBtemplateHaulCapacity() {
		return getTemplateHaulCapacity(1);
	}

	public void setAtemplateTroops(Map<String, Integer> troopAmounts) {
		setTemplateTroops(0, troopAmounts);
	}

	public void setAtemplateTroops(int spear, int sword, int axe, int archer, int spy, int light, int marcher, int heavy, int knight) {
		setTemplateTroops(0, spear, sword, axe, archer, spy, light, marcher, heavy, knight);
	}

	public void setBtemplateTroops(Map<String, Integer> troopAmounts) {
		setTemplateTroops(1, troopAmounts);
	}

	public void setBtemplateTroops(int spear, int sword, int axe, int archer, int spy, int light, int marcher, int heavy, int knight) {
		setTemplateTroops(1, spear, sword, axe, archer, spy, light, marcher, heavy, knight);
	}

	public void setOnlyAttacksFromThisVillage(boolean state) {
		setCheckboxState(findElement(By.id("all_village_checkbox")), state);
	}

	public void setOnlyFullLosses(boolean state) {
		setCheckboxState(findElement(By.id("full_losses_checkbox")), state);
	}

	public void setAvailableTroops(String[] troopNamesToEnable) {
		WebElement troopRow = findElement(By.id("units_home"));
		List<WebElement> troopCheckboxes = troopRow.findElements(By.xpath(".//input[@type='checkbox']"));
		List<String> checkboxNames = new ArrayList<String>();
		List<String> troopNamesToEnableList = new ArrayList<String>();

		for (String troopName : troopNamesToEnable) {
			troopNamesToEnableList.add(troopName);
		}
		for (WebElement troopCheckbox : troopCheckboxes) {
			checkboxNames.add(troopCheckbox.getAttribute("name"));
		}
		WebElement troopCheckbox;
		for (int i = 0; i < checkboxNames.size(); i++) {
			troopCheckbox = troopRow.findElement(By.xpath(".//input[@name='" + checkboxNames.get(i) + "']"));
			if (troopNamesToEnableList.contains(checkboxNames.get(i))) {
				if (!troopCheckbox.isSelected()) {
					troopCheckbox.click();
				}
				troopNamesToEnableList.remove(checkboxNames.get(i));
			} else {
				if (troopCheckbox.isSelected()) {
					troopCheckbox.click();
				}
			}
		}

		for (String troopName : troopNamesToEnableList) {
			System.out.println("Es konnte keine Checkbox mit dem Attributwert '" + troopName + "' des Attributs 'name' gefunden werden");
		}
	}

	public TroopTemplate getAvailableTroops() {
		List<WebElement> troopAmounts = findElement(By.id("units_home")).findElements(By.cssSelector("td.unit-item,td.unit-item hidden"));
		Map<Troop, Integer> availableTroops = new HashMap<Troop, Integer>();

		for (WebElement troopAmount : troopAmounts) {
			availableTroops.put(Troop.convertTroopIdToTroop(troopAmount.getAttribute("id")), Integer.parseInt(troopAmount.getText()));
		}

		return new TroopTemplate(availableTroops);
	}

	private TroopTemplate getTroopTemplate(int templateNumber) {
		List<WebElement> inputs = findElements(By.tagName("form")).get(templateNumber).findElements(By.xpath(".//input[@type='text']"));
		Map<Troop, Integer> aTemplateTroops = new HashMap<Troop, Integer>();

		for (WebElement input : inputs) {
			try {
				aTemplateTroops.put(Troop.convertTroopIdToTroop(input.getAttribute("name")), Integer.valueOf(input.getAttribute("value")));
			} catch (java.lang.NumberFormatException e) {
				System.out.println("\"" + input.getAttribute("value") + "\" konnte nicht in eine Zahl umgewandelt werden");
				e.printStackTrace();
			}
		}

		return new TroopTemplate(aTemplateTroops);
	}

	public TroopTemplate getTemplateTroops(FarmTemplate farmButton) {
		return getTroopTemplate(farmButton.getIndex());
	}

	public void sortByDateAufwaerts() {
		sortByDate("asc");
	}

	public void sortByDateAbwaerts() {
		sortByDate("desc");
	}

	public void sortByDistanceAufwaerts() {
		sortByDistance("asc");
	}

	public void sortByDistanceAbwaerts() {
		sortByDistance("desc");
	}

	public int getFarmEntriesCount() {
		return getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b")).size();
	}

	public FarmEntry getFarmEntry(int index) {
		return new FarmEntry(getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b")).get(index));
	}

	public FarmEntry[] getFarmEntries() {
		List<WebElement> tableRows = getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b"));
		FarmEntry[] farmEntries = new FarmEntry[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			FarmEntry farmEntry = new FarmEntry(tableRows.get(i));
			farmEntries[i] = farmEntry;
		}
		return farmEntries;
	}

	public int getCurrentPageNumber() {
		if (this.urlParameters.containsKey("Farm_page")) {
			return Integer.valueOf(this.urlParameters.get("Farm_page")) + 1;
		} else {
			return 1;
		}
	}

	public int getAmountOfPages() {
		return getFarmEntriesTable().findElements(By.cssSelector("a.paged-nav-item")).size() + 1;
	}

	public boolean hasNextPage() {
		if (getCurrentPageNumber() < getAmountOfPages()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasPreviousPage() {
		if (getCurrentPageNumber() > 1) {
			return true;
		} else {
			return false;
		}
	}

	public void nextPage() {
		if (hasNextPage()) {
			this.urlParameters.put("Farm_page", String.valueOf(getCurrentPageNumber()));
			goToSite();
		} else {
			System.out.println("Es gibt keine nächste Seite mehr");
		}
	}

	public void goToPage(int pPageNumber) {
		if (pPageNumber > 0 && pPageNumber <= getAmountOfPages()) {
			this.urlParameters.put("Farm_page", String.valueOf(pPageNumber - 1));
			goToSite();
		} else {
			System.out.println("Der Farmassistent hat kein " + pPageNumber + " Seiten");
		}
	}

	public void previousPage() {
		if (hasPreviousPage()) {
			this.urlParameters.put("Farm_page", String.valueOf(getCurrentPageNumber() - 1));
			goToSite();
		} else {
			System.out.println("Es gibt keine vorherige Seite mehr");
		}
	}
}
