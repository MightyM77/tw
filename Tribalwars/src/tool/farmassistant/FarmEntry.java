package tool.farmassistant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import config.Configuration;
import utile.Highlighter;
import utile.ReportStatus;
import utile.ResourceBundleUtil;
import utile.Troop;

public class FarmEntry {

	private final WebElement row;
	
	private final static String DATEFORMAT = ResourceBundleUtil.getFarmassistantBundleString("dateformat");;
	private final static String TIMEFORMAT = ResourceBundleUtil.getFarmassistantBundleString("timeformat");
	
	private final static int WOOD_POSITION = 0;
	private final static int CLAY_POSITION = 1;
	private final static int IRON_POSITION = 2;

	public FarmEntry(WebElement row) {
		assertThat(row.getTagName(), equalTo("tr"));
		this.row = row;
	}
	
	private WebElement getRow() {
		return this.row;
	}
	
	private WebElement getColumn(int column) {
		return getRow().findElements(By.tagName("td")).get(column);
	}
	
	private Date parseLastFarmingStringToDate(String lastFarmingTimeString) {
		Date finalLastFarmingTime = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern(DATEFORMAT);
		lastFarmingTimeString = lastFarmingTimeString.replace(ResourceBundleUtil.getFarmassistantBundleString("today"), " " + simpleDateFormat.format(new Date()));
		lastFarmingTimeString = lastFarmingTimeString.replaceAll("[A-Za-z]", "");
		lastFarmingTimeString = lastFarmingTimeString.replaceAll(" +", " ");
		lastFarmingTimeString = lastFarmingTimeString.substring(1);
		lastFarmingTimeString = lastFarmingTimeString.replace(" ", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

		try {
			simpleDateFormat.applyPattern(DATEFORMAT + "yyyy" + TIMEFORMAT);
			finalLastFarmingTime = simpleDateFormat.parse(lastFarmingTimeString);
		} catch (ParseException e) {
			System.out.println("\"" + lastFarmingTimeString + "\" konnte nicht in ein Datum umgewandelt werden");
			e.printStackTrace();
		}

		return finalLastFarmingTime;
	}
	
	public void delete() {
		getColumn(0).findElement(By.tagName("a")).click();
	}
	
	public boolean isMaxLoot() {
		List<WebElement> maxLootImage = getColumn(2).findElements(By.tagName("img"));
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
		return maxLoot;
	}
	
	
	public ReportStatus getFarmStatus() {
		String farmStatusImageSrc = getColumn(1).findElement(By.tagName("img")).getAttribute("src");
		return ReportStatus.stringContainsReportStatusColor(farmStatusImageSrc);
	}

	public Point getCoord() {
		WebElement villageLink = getColumn(3).findElement(By.tagName("a"));

		String[] villageCoordArray = villageLink.getText().replaceAll("\\s+", "").substring(1, 8).split("\\|");
		Point villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));
		return villageCoord;
	}

	public String attackTitle() {
		boolean alreadyAttacking = false;
		List<WebElement> attackImage = getColumn(3).findElements(By.tagName("img"));
		if (attackImage.size() > 0) {
			alreadyAttacking = true;
		}
		Highlighter.getInstance().highlightElement(Configuration.DRIVER, attackImage.get(0));
		return attackImage.get(0).getAttribute("title");
	}
	
	public boolean isGettingAttacked() {
		boolean alreadyAttacking = false;
		List<WebElement> attackImage = getColumn(3).findElements(By.tagName("img"));
		if (attackImage.size() > 0) {
			alreadyAttacking = true;
		}
		return alreadyAttacking;
	}
	
	public Date getLastFarmingTime() {
		String lastFarmingTimeString = getColumn(4).getText();
		return parseLastFarmingStringToDate(lastFarmingTimeString);
	}

	/**
	 * @param resourcePosition the position of the resource (wood = 0, clay = 1, iron = 2)
	 * @return
	 */
	private int getResources(int resourcePosition) {
		List<WebElement> resources = getColumn(5).findElements(By.cssSelector("span.warn,span.res,span.warn_90"));
		int resource;
		if (resources.size() == 3) {
			resource = Integer.valueOf(resources.get(resourcePosition).getText().replaceAll("[^0-9]", ""));
		} else {
			resource = -1;
		}
		return resource;
	}
	
	public int getWood() {
		return getResources(WOOD_POSITION);
	}
	
	public int getClay() {
		return getResources(CLAY_POSITION);
	}

	public int getIron() {
		return getResources(IRON_POSITION);
	}

	public int getWallLevel() {
		int wallLevel;
		try {
			wallLevel = Integer.valueOf(getColumn(6).getText());
		} catch (java.lang.NumberFormatException e) {
			wallLevel = -1;
		}
		return wallLevel;
	}

	private double getDistance() {
		return Double.valueOf(getColumn(7).getText());
	}
	
	public Calendar getArrivingDate(Troop troop) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, (int) (getDistance() * troop.getWalkingDurationSeconds()));
		return c;
	}
	
	public Calendar getBackToVillageTime(Troop troop) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, (int) (getDistance() * troop.getWalkingDurationSeconds() * 2));
		return c;
	}

	private WebElement getFarmbutton(FarmButton farmButton) {
		int farmButtonColumn = -1;
		switch (farmButton) {
		case A:
			farmButtonColumn = 8;
			break;
		case B:
			farmButtonColumn = 9;
			break;
		case C:
			farmButtonColumn = 10;
			break;
		}
		
		List<WebElement> farmButtonElement = getColumn(farmButtonColumn).findElements(By.tagName("a"));
		if (farmButtonElement.size() > 0) {
			return farmButtonElement.get(0);
		} else {
			return null;
		}
	}
	
	public boolean isFarmButtonExisting(FarmButton farmButton) {
		WebElement farmButtonElement = getFarmbutton(farmButton);
		if (farmButtonElement != null) {
			return true;
		}
		return false;
	}
	
	public boolean isFarmButtonEnabled(FarmButton farmButton) {
		WebElement farmButtonElement = getFarmbutton(farmButton);
		if (farmButtonElement != null) {
			if (!farmButtonElement.getAttribute("class").contains("farm_icon_disabled")) {
				return true;
			}
		}
		return false;		
	}
	
	public boolean isFarmButtonEnabled(FarmTemplate farmTemplate) {
		return isFarmButtonEnabled(farmTemplate.convertToFarmButton());		
	}
	
	public void clickFarmButton(FarmButton withFarmButton) {
		WebElement farmButton = getFarmbutton(withFarmButton);
		if (farmButton != null) {
			farmButton.click();
		} else {
			System.out.println(withFarmButton.name() + "-Button ist nicht vorhanden!");
		}
	}
}
