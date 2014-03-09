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

import config.TwConfiguration;
import tool.VillageSite;
import utile.ResourceBundleUtil;
import utile.Troop;
import utile.TroopTemplate;

public class Farmassistant extends VillageSite {
	
	public Farmassistant(TwConfiguration pConfig) {
		super(pConfig, "am_farm");
	}

	private WebElement getFarmEntriesTable() {
		WebElement entriesFatherDiv = findElement(By.id("am_widget_Farm"));
		return entriesFatherDiv.findElement(By.tagName("table"));
	}

	public void setTemplateTroops(FarmTemplate farmTemplate, TroopTemplate troopTemplate) {
		WebElement form = findElements(By.tagName("form")).get(farmTemplate.getIndex());

		Iterator<Entry<Troop, Integer>> iterator = troopTemplate.getAllTroops().entrySet().iterator();
		WebElement input;
		while (iterator.hasNext()) {
			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
			input = form.findElement(By.xpath(".//input[@name='" + pairs.getKey().getId() + "']"));
			input.click();
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), pairs.getValue().toString());
		}

		form.findElement(By.xpath(".//input[@value='" + ResourceBundleUtil.getFarmassistantBundleString("save", config().getLocale()) + "']")).click();
		;
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

	public void setOnlyAttacksFromThisVillage(boolean state) {
		setCheckboxState(findElement(By.id("all_village_checkbox")), state);
	}

	public void setOnlyFullLosses(boolean state) {
		setCheckboxState(findElement(By.id("full_losses_checkbox")), state);
	}

	public void setAvailableTroops(Troop[] troopNamesToEnable) {
		WebElement troopRow = findElement(By.id("units_home"));
		List<WebElement> troopCheckboxes = troopRow.findElements(By.xpath(".//input[@type='checkbox']"));
		List<String> checkboxNames = new ArrayList<String>();
		List<String> troopNamesToEnableList = new ArrayList<String>();

		for (Troop troop : troopNamesToEnable) {
			troopNamesToEnableList.add(troop.getId());
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

	public List<Troop> getEnabledTroopsForCFarming() {
		WebElement unitsHomeTable = getUnitsHomeTable();
		List<WebElement> enabledTroopCheckboxes = unitsHomeTable.findElements(By.xpath("//input[@type='checkbox' and @checked='checked']"));
		List<Troop> enabledTroops = new ArrayList<Troop>();
		for (WebElement enabledTroopCheckbox : enabledTroopCheckboxes) {
			enabledTroops.add(Troop.valueOfId(enabledTroopCheckbox.getAttribute("name")));
		}
		return enabledTroops;
	}
	
	private WebElement getUnitsHomeTable() {
		return findElement(By.id("units_home"));
	}
	
	public TroopTemplate getAvailableTroops() {
		List<WebElement> troopAmounts = getUnitsHomeTable().findElements(By.cssSelector("td.unit-item,td.unit-item hidden"));
		Map<Troop, Integer> availableTroops = new HashMap<Troop, Integer>();

		for (WebElement troopAmount : troopAmounts) {
			availableTroops.put(Troop.valueOfId(troopAmount.getAttribute("id")), Integer.parseInt(troopAmount.getText()));
		}

		return new TroopTemplate(availableTroops);
	}

	public TroopTemplate getTroopTemplate(FarmTemplate farmTemplate) {
		List<WebElement> inputs = findElements(By.tagName("form")).get(farmTemplate.getIndex()).findElements(By.xpath(".//input[@type='text']"));
		Map<Troop, Integer> aTemplateTroops = new HashMap<Troop, Integer>();

		for (WebElement input : inputs) {
			try {
				aTemplateTroops.put(Troop.valueOfId(input.getAttribute("name")), Integer.valueOf(input.getAttribute("value")));
			} catch (java.lang.NumberFormatException e) {
				System.out.println("\"" + input.getAttribute("value") + "\" konnte nicht in eine Zahl umgewandelt werden");
				e.printStackTrace();
			}
		}

		return new TroopTemplate(aTemplateTroops);
	}
	
	public boolean enoughTroops(FarmButton farmButton) {
		return enoughTroops(getAvailableTroops(), farmButton);
	}
	public boolean enoughTroops(TroopTemplate availableTroops, FarmButton farmButton) {
		boolean enoughTroops = true;
		if (farmButton == FarmButton.A || farmButton == FarmButton.B) {
			FarmTemplate farmTemplate = FarmTemplate.valueOf(farmButton);
			TroopTemplate troopTemplate = getTroopTemplate(farmTemplate);
			if (!troopTemplate.lessOrEqualTroops(availableTroops)) {
				enoughTroops = false;
			}
		} else if (farmButton == FarmButton.C) {
			enoughTroops = false;
			List<Troop> enabledTroopsForCFarming =  getEnabledTroopsForCFarming();
			Map<Troop, Integer> availableTroopsMap = availableTroops.getAllTroops();
			for (Troop troop : enabledTroopsForCFarming) {
				if (availableTroopsMap.get(troop) > 5) {
					enoughTroops = true;
				}
			}
		}
		return enoughTroops;
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
		return new FarmEntry(config(), getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b")).get(index));
	}

	public FarmEntry[] getFarmEntries() {
		List<WebElement> tableRows = getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b"));
		FarmEntry[] farmEntries = new FarmEntry[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			FarmEntry farmEntry = new FarmEntry(config(), tableRows.get(i));
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