package tool.headquarters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tool.Site;
import utile.Highlighter;
import utile.ResourceBundleUtil;

public class Headquarters extends Site {

	public static final String HEADQUARTERS = "main";
	public static final String BARRACKS = "barracks";
	public static final String SMITHY = "smith";
	public static final String REALLY_POINT = "place";
	public static final String STATUE = "statue";
	public static final String MARKET = "market";
	public static final String TIMBER_CAMP = "wood";
	public static final String CLAY_PIT = "stone";
	public static final String IRON_MINE = "iron";
	public static final String FARM = "farm";
	public static final String WAREHOUSE = "storage";
	public static final String HIDING_PLACE = "hide";
	public static final String WALL = "wall";
	public static final String STABLE = "stable";
	public static final String WORKSHOP = "garage";
	public static final String ACADEMY = "snob";

	public Headquarters(WebDriver pDriver) {
		super("/game.php", "main", pDriver);
	}

	private Date parseResourcesAvailableTextToDate(String resourcesAvailableText) {
		String dateformat = ResourceBundleUtil.getHeadquartersBundleString("dateformat");
		;
		String timeformat = ResourceBundleUtil.getHeadquartersBundleString("timeformat");
		Date finalLastFarmingTime = null;

		someDateFormat.applyPattern(dateformat);
		resourcesAvailableText = resourcesAvailableText.replace(ResourceBundleUtil.getFarmassistantBundleString("today"), " " + someDateFormat.format(new Date()));
		resourcesAvailableText = resourcesAvailableText.replaceAll("[A-Za-z]", "");
		resourcesAvailableText = resourcesAvailableText.replaceAll(" +", " ");
		resourcesAvailableText = resourcesAvailableText.substring(1);
		resourcesAvailableText = resourcesAvailableText.replace(" ", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

		try {
			someDateFormat.applyPattern(dateformat + "yyyy" + timeformat);
			finalLastFarmingTime = someDateFormat.parse(resourcesAvailableText);
		} catch (ParseException e) {
			System.out.println("\"" + resourcesAvailableText + "\" konnte nicht in ein Datum umgewandelt werden");
			e.printStackTrace();
		}

		return finalLastFarmingTime;
	}

	private Building createBuildingFromTrElement(WebElement trElement) {

		if (!trElement.getAttribute("id").startsWith("main_buildrow_")) {
			System.out.println("Building konnte nicht erstellt werden, '" + trElement.getAttribute("id") + "' ist eine ungültige ID, für ein Building-Tr-Element");
			return null;
		}

		Building building = null;
		List<WebElement> buildingTd = trElement.findElements(By.tagName("td"));
		// NAME
		String name = buildingTd.get(0).findElements(By.tagName("a")).get(1).getText();
		// BUILDING_LEVEL
		String levelString = buildingTd.get(0).findElement(By.tagName("span")).getText().replaceAll("[^0-9]", "");
		int level = 0;
		if (levelString.length() > 0) {
			level = Integer.valueOf(levelString);
		}
		if (buildingTd.size() != 2) {
			// RESOURCES
			int wood = Integer.valueOf(buildingTd.get(1).getText().replaceAll("[^0-9]", ""));
			int clay = Integer.valueOf(buildingTd.get(2).getText().replaceAll("[^0-9]", ""));
			int iron = Integer.valueOf(buildingTd.get(3).getText().replaceAll("[^0-9]", ""));
			// BUILDING_DURATION_SECONDS
			String duration = buildingTd.get(4).getText().replaceAll("[^0-9\\:]", "");
			String[] splitDuration = duration.split("\\:");
			int buildingDurationSeconds = Integer.valueOf(splitDuration[0]) * 3600 + Integer.valueOf(splitDuration[1]) * 60 + Integer.valueOf(splitDuration[2]);
			// POPULATION_FOR_NEXT_LEVEL
			int populationForNextLevel = 0;
			if (buildingTd.get(5).getText().length() > 0) {
				populationForNextLevel = Integer.valueOf(buildingTd.get(5).getText().replaceAll("[^0-9]", ""));
			}
			// RESOURCE_AVAILABLE & LEVELUP
			
			List<WebElement> levelUpList = buildingTd.get(6).findElements(By.xpath(".//a[@class='btn btn-build']"));
			Date resourcesAvailable = null;
			WebElement levelUp = null;
			if (levelUpList.size() == 1) {
				levelUp = levelUpList.get(0);
			} else if (levelUpList.size() == 0) {
				String availableText = buildingTd.get(6).findElement(By.tagName("span")).getText();
				resourcesAvailable = parseResourcesAvailableTextToDate(availableText);
			}

			building = new Building(false, name, level, wood, clay, iron, buildingDurationSeconds, populationForNextLevel, resourcesAvailable, levelUp);
		} else {
			building = new Building(true, name, level, -1, -1, -1, -1, -1, null, null);
		}

		return building;
	}

	private Building getBuilding(String buildingName) {
		Building building = null;
		try {
			building = createBuildingFromTrElement(findElement(By.id("main_buildrow_" + buildingName)));
		} catch (org.openqa.selenium.NoSuchElementException e) {
			System.out.println("Gebäude '" + buildingName + "' ist noch nicht verfügbar");
		}
		return building;
	}

	public Building getHeadquarters() {
		return getBuilding(HEADQUARTERS);
	}

	public Building getBarracks() {
		return getBuilding(BARRACKS);
	}

	public Building getSmithy() {
		return getBuilding(SMITHY);
	}

	public Building getReallyPoint() {
		return getBuilding(REALLY_POINT);
	}

	public Building getStatue() {
		return getBuilding(STATUE);
	}

	public Building getMarket() {
		return getBuilding(MARKET);
	}

	public Building getTimberCamp() {
		return getBuilding(TIMBER_CAMP);
	}

	public Building getClayPit() {
		return getBuilding(CLAY_PIT);
	}

	public Building getFarm() {
		return getBuilding(FARM);
	}

	public Building getWarehouse() {
		return getBuilding(WAREHOUSE);
	}

	public Building getIronHidingPlace() {
		return getBuilding(HIDING_PLACE);
	}

	public Building getWall() {
		return getBuilding(WALL);
	}

	public Building getStable() {
		return getBuilding(STABLE);
	}

	public Building getWorkshop() {
		return getBuilding(WORKSHOP);
	}

	public Building getAcademy() {
		return getBuilding(ACADEMY);
	}

	public List<Building> getAvailableBuildings() {
		List<WebElement> buildingsTr = findElements(By.xpath("//tr[starts-with(@id,'main_buildrow_')]"));
		List<Building> buildings = new ArrayList<Building>();
		for (WebElement buildingTr : buildingsTr) {
			buildings.add(createBuildingFromTrElement(buildingTr));
		}
		return buildings;
	}

	public void renameVillage(String newName) {
		WebElement nameInput = findElement(By.xpath("//input[@type='text' and @name='name']"));
		if (newName.length() <= Integer.valueOf(nameInput.getAttribute("maxlength"))){
			nameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newName);
			findElement(By.xpath("//input[@type=submit and @value='" + ResourceBundleUtil.getHeadquartersBundleString("change") +"']")).click();
		} else {
			System.out.println("Der Dorfname darf maximal " + nameInput.getAttribute("maxlength") + " Zeichen lang sein, der angegeben Wert ist jedoch " + newName.length() + " Zeichen lang");
		}
		
	}
}
