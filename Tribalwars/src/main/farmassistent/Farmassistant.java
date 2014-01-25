package main.farmassistent;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.Highlighter;
import main.ResourceBundleUtil;
import main.Site;
import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Farmassistant extends Site {

	private SimpleDateFormat someDateFormat = new SimpleDateFormat();
	private String dateformat = ResourceBundleUtil.getFarmassistantBundleString("dateformat");;
	private String timeformat = ResourceBundleUtil.getFarmassistantBundleString("timeformat");
	
	public Farmassistant(WebDriver pDriver) {
		super("/game.php?screen=am_farm", pDriver);
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

	public void setOnlyAttacksFromThisVillage(boolean state) {
		WebElement allVillagesCheckbox = findElement(By.id("all_village_checkbox"));
		if (state && !allVillagesCheckbox.isSelected()) {
			allVillagesCheckbox.click();
		} else if (!state && allVillagesCheckbox.isSelected()) {
			allVillagesCheckbox.click();
		}
	}

	public void setOnlyFullLosses(boolean state) {
		WebElement onlyFullLosses = findElement(By.id("full_losses_checkbox"));
		if (state && !onlyFullLosses.isSelected()) {
			onlyFullLosses.click();
		} else if (!state && onlyFullLosses.isSelected()) {
			onlyFullLosses.click();
		}
	}

	public void setAvailableTroops(String[] troopNamesToEnable) {
		List<WebElement> troopCheckboxes = findElement(By.id("units_home")).findElements(By.xpath("//*[@type='checkbox']"));

		for (WebElement troopCheckbox : troopCheckboxes) {
			if (Arrays.asList(troopNamesToEnable).contains(troopCheckbox.getAttribute("name"))) {
				if (!troopCheckbox.isSelected()) {
					troopCheckbox.click();
				}
			} else {
				if (troopCheckbox.isSelected()) {
					troopCheckbox.click();
				}
			}
		}
	}

	public Map<String, Integer> getAvailableTroops() {
		List<WebElement> troopAmounts = findElement(By.id("units_home")).findElements(By.cssSelector("td.unit-item,td.unit-item hidden"));
		Map<String, Integer> availableTroops = new HashMap<String, Integer>();

		for (WebElement troopAmount : troopAmounts) {
			availableTroops.put(troopAmount.getAttribute("id"), Integer.parseInt(troopAmount.getText()));
		}

		return availableTroops;
	}

	private Map<String, Integer> getAtemplateTroops(int templateNumber) {
		List<WebElement> inputs = findElements(By.tagName("form")).get(templateNumber).findElements(By.xpath("//input[@type='text']"));
		Map<String, Integer> aTemplateTroops = new HashMap<String, Integer>();

		for (WebElement input : inputs) {
			try {
				aTemplateTroops.put(input.getAttribute("name"), Integer.valueOf(input.getAttribute("value")));
			} catch (java.lang.NumberFormatException e) {
				System.out.println("\"" + input.getAttribute("value") + "\" konnte nicht in eine Zahl umgewandelt werden");
				e.printStackTrace();
			}
		}

		return aTemplateTroops;
	}

	public Map<String, Integer> getAtemplateTroops() {
		return getAtemplateTroops(0);
	}

	public Map<String, Integer> getBtemplateTroops() {
		return getAtemplateTroops(1);
	}

	private void setTemplateTroops(int templateNumber, Map<String, Integer> troopAmounts) {
		WebElement form = findElements(By.tagName("form")).get(templateNumber);

		Iterator<Entry<String, Integer>> iterator = troopAmounts.entrySet().iterator();
		WebElement input;
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			input = form.findElement(By.xpath("//input[@name='" + pairs.getKey() + "']"));
			input.click();
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), pairs.getValue().toString());
		}

		form.findElement(By.xpath("//input[@value='" + ResourceBundleUtil.getFarmassistantBundleString("save") + "']")).click();
		;
	}

	public void setAtemplateTroops(Map<String, Integer> troopAmounts) {
		setTemplateTroops(0, troopAmounts);
	}

	public void setBtemplateTroops(Map<String, Integer> troopAmounts) {
		setTemplateTroops(1, troopAmounts);
	}

	private int getTemplateHaulCapacity(int templateNumber) {
		List<WebElement> tds = findElements(By.tagName("form")).get(templateNumber).findElements(By.tagName("tr")).get(1).findElements(By.tagName("td"));
		return Integer.valueOf(tds.get(tds.size() - 1).getText());
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

	public void sortByCistanceAbwaerts() {
		sortByDistance("desc");
	}
	
	public FarmEntry[] getFarmEntries() {
		long before = System.nanoTime();

		List<WebElement> tableRows = getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b"));
		FarmEntry[] farmEntries = new FarmEntry[tableRows.size()];
		for (int i = 0; i < tableRows.size(); i++) {
			List<WebElement> rowCells = tableRows.get(i).findElements(By.tagName("td"));

			// DELETE BUTTON
			WebElement deleteButton = rowCells.get(0).findElement(By.tagName("a"));
			// FARM_STATUS
			String farmStatusImageSrc = rowCells.get(1).findElement(By.tagName("img")).getAttribute("src");
			int farmStatus = -1;
			if (farmStatusImageSrc.contains("green")) {
				farmStatus = FarmEntry.FARMSTATUS_GREEN;
			} else if (farmStatusImageSrc.contains("yellow")) {
				farmStatus = FarmEntry.FARMSTATUS_YELLOW;
			} else if (farmStatusImageSrc.contains("red")) {
				farmStatus = FarmEntry.FARMSTATUS_RED;
			} else if (farmStatusImageSrc.contains("blue")) {
				farmStatus = FarmEntry.FARMSTATUS_BLUE;
			}
			// MAX_LOOT
			String maxLootImageSrc = rowCells.get(2).findElement(By.tagName("img")).getAttribute("src");
			boolean maxLoot = false;
			if (maxLootImageSrc.contains("0.png")) {
				maxLoot = false;
			} else if (maxLootImageSrc.contains("1.png")) {
				maxLoot = true;
			}
			// VILLAGE_LINK & VILLAGE_COORD
			WebElement villageLink = rowCells.get(3).findElement(By.tagName("a"));

			String[] villageCoordArray = villageLink.getText().replaceAll("\\s+", "").substring(1, 8).split("\\|");
			Point villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));

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
			FarmEntry farmEntry = new FarmEntry();
			farmEntry.setDeleteButton(deleteButton);
			farmEntry.setFarmStatus(farmStatus);
			farmEntry.setMaxLoot(maxLoot);
			farmEntry.setVillageCoord(villageCoord);
			farmEntry.setVillageLink(villageLink);
			farmEntry.setLastFarmingTime(lastFarmingTime);
			farmEntry.setWood(wood);
			farmEntry.setClay(clay);
			farmEntry.setIron(iron);
			farmEntry.setWallLevel(wallLevel);
			farmEntry.setDistance(distance);
			farmEntry.setaButton(aButton);
			farmEntry.setbButton(bButton);
			farmEntry.setcButton(cButton);
			farmEntry.setReallyPointLink(reallyPointLink);

			farmEntries[i] = farmEntry;
		}

		long after = System.nanoTime() - before;
		System.out.println(after);
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
			this.urlParameters.put("Farm_page", String.valueOf(getCurrentPageNumber()+1));
			goToSite();
		} else {
			System.out.println("Es gibt keine nächste Seite mehr");
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
