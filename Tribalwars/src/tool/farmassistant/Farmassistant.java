package tool.farmassistant;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import utile.ReportStatus;
import utile.ResourceBundleUtil;
import utile.Troop;

public class Farmassistant extends Site {

	private static final Farmassistant INSTANCE = new Farmassistant();
	private SimpleDateFormat someDateFormat = new SimpleDateFormat();
	private String dateformat = ResourceBundleUtil.getFarmassistantBundleString("dateformat");;
	private String timeformat = ResourceBundleUtil.getFarmassistantBundleString("timeformat");
	
	private Farmassistant() {
		super("/game.php", "am_farm");
	}
	
	public static Farmassistant getInstance() {
		return Farmassistant.INSTANCE;
	}

	private Date parseLastFarmingStringToDate(String lastFarmingTimeString) {
		Date finalLastFarmingTime = null;

		someDateFormat.applyPattern(dateformat);
		lastFarmingTimeString = lastFarmingTimeString.replace(ResourceBundleUtil.getFarmassistantBundleString("today"), " " + someDateFormat.format(new Date()));
		lastFarmingTimeString = lastFarmingTimeString.replaceAll("[A-Za-z]", "");
		lastFarmingTimeString = lastFarmingTimeString.replaceAll(" +", " ");
		lastFarmingTimeString = lastFarmingTimeString.substring(1);
		lastFarmingTimeString = lastFarmingTimeString.replace(" ", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

		try {
			someDateFormat.applyPattern(dateformat + "yyyy" + timeformat);
			finalLastFarmingTime = someDateFormat.parse(lastFarmingTimeString);
		} catch (ParseException e) {
			System.out.println("\"" + lastFarmingTimeString + "\" konnte nicht in ein Datum umgewandelt werden");
			e.printStackTrace();
		}

		return finalLastFarmingTime;
	}

	private FarmButton getFarmButton(WebElement td) {
		FarmButton aButton;
		try {
			WebElement aButtonElement = td.findElement(By.tagName("a"));

			if (aButtonElement.getAttribute("class").contains("farm_icon_disabled")) {
				aButton = new FarmButton(aButtonElement, false);
			} else {
				aButton = new FarmButton(aButtonElement, true);
			}

		} catch (org.openqa.selenium.NoSuchElementException e) {
			aButton = new FarmButton(null, false);
		}
		return aButton;
	}

	private WebElement getFarmEntriesTable() {
		WebElement entriesFatherDiv = findElement(By.id("am_widget_Farm"));
		return entriesFatherDiv.findElement(By.tagName("table"));
	}

	private void setCheckboxState(WebElement checkbox, boolean state) {
		wait.until(ExpectedConditions.elementToBeClickable(checkbox));
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

	public Map<Troop, Integer> getAvailableTroops() {
		List<WebElement> troopAmounts = findElement(By.id("units_home")).findElements(By.cssSelector("td.unit-item,td.unit-item hidden"));
		Map<Troop, Integer> availableTroops = new HashMap<Troop, Integer>();

		for (WebElement troopAmount : troopAmounts) {
			availableTroops.put(Troop.convertTroopIdToTroop(troopAmount.getAttribute("id")), Integer.parseInt(troopAmount.getText()));
		}

		return availableTroops;
	}

	private Map<Troop, Integer> getAtemplateTroops(int templateNumber) {
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

		return aTemplateTroops;
	}

	public Map<Troop, Integer> getAtemplateTroops() {
		return getAtemplateTroops(0);
	}

	public Map<Troop, Integer> getBtemplateTroops() {
		return getAtemplateTroops(1);
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

	private void sort(String sortBy, String sortModus){
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
	
	public FarmEntry[] getFarmEntries() {
		List<WebElement> tableRows = getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b"));
		FarmEntry[] farmEntries = new FarmEntry[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			List<WebElement> rowCells = tableRows.get(i).findElements(By.tagName("td"));

			// DELETE BUTTON
			WebElement deleteButton = rowCells.get(0).findElement(By.tagName("a"));
			// FARM_STATUS
			String farmStatusImageSrc = rowCells.get(1).findElement(By.tagName("img")).getAttribute("src");
			ReportStatus reportStatus = ReportStatus.stringContainsReportStatusColor(farmStatusImageSrc);
			// MAX_LOOT
			List<WebElement> maxLootImage = rowCells.get(2).findElements(By.tagName("img"));
			boolean maxLoot = false;
			// Wenn das Barbarendorf nur gespäht wurde, gibt es kein MaxLoot image
			if (maxLootImage.size() > 0) {
				String maxLootImageSrc = maxLootImage.get(0).getAttribute("src");
				if (maxLootImageSrc.contains("0.png")) {
					maxLoot = false;
				} else if (maxLootImageSrc.contains("1.png")) {
					maxLoot = true;
				}
			}
			
			// VILLAGE_LINK & VILLAGE_COORD
			WebElement villageLink = rowCells.get(3).findElement(By.tagName("a"));

			String[] villageCoordArray = villageLink.getText().replaceAll("\\s+", "").substring(1, 8).split("\\|");
			Point villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));

			// ATTACKS
			int attacks = 0;
			List<WebElement> attackImage = rowCells.get(3).findElements(By.tagName("img"));
			if (attackImage.size() > 0) {
				attacks = 1;
			}
			
			// LAST_FARMING_TIME
			String lastFarmingTimeString = rowCells.get(4).getText();
			Date lastFarmingTime = parseLastFarmingStringToDate(lastFarmingTimeString);

			// WOOD & CLAY & IRON
			List<WebElement> resources = rowCells.get(5).findElements(By.cssSelector("span.warn,span.res,span.warn_90"));
			int wood;
			int clay;
			int iron;
			if (resources.size() == 3) {
				wood = Integer.valueOf(resources.get(0).getText().replaceAll("[^0-9]", ""));
				clay = Integer.valueOf(resources.get(1).getText().replaceAll("[^0-9]", ""));
				iron = Integer.valueOf(resources.get(2).getText().replaceAll("[^0-9]", ""));
			} else {
				wood = -1;
				clay = -1;
				iron = -1;
			}
			// WALL_LEVEL
			int wallLevel;
			try {
				wallLevel = Integer.valueOf(rowCells.get(6).getText());
			} catch (java.lang.NumberFormatException e) {
				wallLevel = -1;
			}

			// DISTANCE
			double distance = Double.valueOf(rowCells.get(7).getText());
			// A_BUTTON
			FarmButton aButton = getFarmButton(rowCells.get(8));
			// B_BUTTON
			FarmButton bButton = getFarmButton(rowCells.get(9));
			// C_BUTTON
			FarmButton cButton = getFarmButton(rowCells.get(10));
			// REALLYPOINT_LINK
			WebElement reallyPointLink = rowCells.get(11).findElement(By.tagName("a"));

			// Initialize FarmEntry
			FarmEntry farmEntry = new FarmEntry(deleteButton, reportStatus, maxLoot, villageLink, attacks, villageCoord, lastFarmingTime, wood, clay, iron, wallLevel, distance, aButton, bButton, cButton, reallyPointLink);
			farmEntries[i] = farmEntry;
		}
		return farmEntries;
	}

	public int getCurrentPageNumber() {
		if (this.urlParameters.containsKey("Farm_page")) {
			return Integer.valueOf(this.urlParameters.get("Farm_page"))+1;
		} else {
			return 1;
		}
	}
	
	public int getAmountOfPages() {
		return getFarmEntriesTable().findElements(By.cssSelector("a.paged-nav-item")).size()+1;
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
			this.urlParameters.put("Farm_page", String.valueOf(pPageNumber-1));
			goToSite();
		} else {
			System.out.println("Der Farmassistent hat kein " + pPageNumber + " Seiten");
		}
	}
	
	public void previousPage() {
		if (hasPreviousPage()) {
			this.urlParameters.put("Farm_page", String.valueOf(getCurrentPageNumber()-1));
			goToSite();
		} else {
			System.out.println("Es gibt keine vorherige Seite mehr");
		}
	}
}
