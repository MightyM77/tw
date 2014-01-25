package main.farmassistent;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import main.Highlighter;
import main.ResourceBundleUtil;
import main.Site;
import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Farmassistant extends Site {

	private SimpleDateFormat someDateFormat = new SimpleDateFormat(); 
	private String dateformat = ResourceBundleUtil.getFarmassistantBundleString("dateformat");;
	private String timeformat = ResourceBundleUtil.getFarmassistantBundleString("timeformat");
	
	public Farmassistant(WebDriver pDriver) {
		super("/game.php?screen=am_farm", pDriver);
	}

	public WebElement getFarmEntriesTable() {
		WebElement entriesFatherDiv = findElement(By.id("am_widget_Farm"));
		return entriesFatherDiv.findElement(By.tagName("table"));
	}

	public List<WebElement> getRessis() {
		return findElements(By.cssSelector("span.warn,span.res,span.warn_90"));
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

	public FarmEntry[] getFarmEntries() {
		long before = System.nanoTime();
		
		List<WebElement> tableRows = getFarmEntriesTable().findElements(By.cssSelector(".row_a,.row_b"));
		FarmEntry[] farmEntries = new FarmEntry[tableRows.size()];
		FarmEntry farmEntry;
		List<WebElement> rowCells;
		WebElement deleteButton;
		String farmStatusImageSrc;
		String maxLootImageSrc;
		WebElement villageLink;
		String[] villageCoordArray;
		Point villageCoord;
		String lastFarmingTimeString;
		Date lastFarmingTime;
		List<WebElement> resources;
		int wood;
		int clay;
		int iron;
		int wallLevel;
		double distance;
		FarmButton aButton;
		FarmButton bButton;
		FarmButton cButton;
		WebElement reallyPointLink;
		for (int i = 0; i < tableRows.size(); i++) {
			rowCells = tableRows.get(i).findElements(By.tagName("td"));

			// DELETE BUTTON
			deleteButton = rowCells.get(0).findElement(By.tagName("a"));
			// FARM_STATUS
			farmStatusImageSrc = rowCells.get(1).findElement(By.tagName("img")).getAttribute("src");
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
			maxLootImageSrc = rowCells.get(2).findElement(By.tagName("img")).getAttribute("src");
			boolean maxLoot = false;
			if (maxLootImageSrc.contains("0.png")) {
				maxLoot = false;
			} else if (maxLootImageSrc.contains("1.png")) {
				maxLoot = true;
			}
			// VILLAGE_LINK & VILLAGE_COORD
			villageLink = rowCells.get(3).findElement(By.tagName("a"));


			villageCoordArray = villageLink.getText().replaceAll("\\s+", "").substring(1, 8).split("\\|");
			villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));

			// LAST_FARMING_TIME
			lastFarmingTimeString = rowCells.get(4).getText();
			lastFarmingTime = parseLastFarmingStringToDate(lastFarmingTimeString);

			// WOOD & CLAY & IRON
			resources = rowCells.get(5).findElements(By.cssSelector("span.warn,span.res,span.warn_90"));
			
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
			
			try {
				wallLevel = Integer.valueOf(rowCells.get(6).getText());
			} catch (java.lang.NumberFormatException e) {
				wallLevel = -1;
			}

			// DISTANCE
			distance = Double.valueOf(rowCells.get(7).getText());
			// A_BUTTON
			aButton = getFarmButton(rowCells.get(8));
			// B_BUTTON
			bButton = getFarmButton(rowCells.get(9));
			// C_BUTTON
			cButton = getFarmButton(rowCells.get(10));
			// REALLYPOINT_LINK
			reallyPointLink = rowCells.get(11).findElement(By.tagName("a"));
//			Highlighter.getInstance().highlightElementFlash(driver, reallyPointLink);

			// Initialize FarmEntry
			farmEntry = new FarmEntry();
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
		
		long after = System.nanoTime()-before;
		System.out.println(after);
		return farmEntries;
	}

	public List<WebElement> getDisabledCbuttons() {
		List<WebElement> farmIcons = findElements(By.cssSelector(".farm_icon_c.farm_icon_disabled"));
		return farmIcons;
	}
}
