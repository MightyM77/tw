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

import utile.Highlighter;
import utile.ReportStatus;
import utile.Resource;
import utile.ResourceBundleUtil;
import utile.Troop;
import config.TwConfiguration;

public class FarmEntry {
	private final static int WOOD_POSITION = 0;
	private final static int CLAY_POSITION = 1;
	private final static int IRON_POSITION = 2;

	private final WebElement row;
	private final String dateformat;
	private final String timeformat;
	private final TwConfiguration config;

	private Boolean isMaxLoot;
	private ReportStatus reportStatus;
	private Point destCoords;
	private Boolean isGettingAttacked;
	private Date lastFarmingTime;
	private Resource resources;
	private Integer wall;
	private Double distance;

	public FarmEntry(TwConfiguration pConfig, WebElement row) {
		this.config = pConfig;
		this.dateformat = ResourceBundleUtil.getFarmassistantBundleString("dateformat", config.getLocale());
		this.timeformat = ResourceBundleUtil.getFarmassistantBundleString("timeformat", config.getLocale());
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
		simpleDateFormat.applyPattern(dateformat);
		lastFarmingTimeString = lastFarmingTimeString.replace(ResourceBundleUtil.getFarmassistantBundleString("today", config.getLocale()), " " + simpleDateFormat.format(new Date()));
		lastFarmingTimeString = lastFarmingTimeString.replaceAll("[A-Za-z]", "");
		lastFarmingTimeString = lastFarmingTimeString.replaceAll(" +", " ");
		lastFarmingTimeString = lastFarmingTimeString.substring(1);
		lastFarmingTimeString = lastFarmingTimeString.replace(" ", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

		try {
			simpleDateFormat.applyPattern(dateformat + "yyyy" + timeformat);
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

	private boolean findMaxLoot() {
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

	public boolean isMaxLoot() {
		if (this.isMaxLoot == null) {
			this.isMaxLoot = findMaxLoot();
		}
		return this.isMaxLoot;
	}

	public ReportStatus getOrFindReportStatus() {
		if (this.reportStatus == null) {
			String farmStatusImageSrc = getColumn(1).findElement(By.tagName("img")).getAttribute("src");
			this.reportStatus = ReportStatus.stringContainsReportStatusColor(farmStatusImageSrc);
		}
		return this.reportStatus;
	}

	public Point getOrFindDestCoords() {
		if (this.destCoords == null) {
			WebElement villageLink = getColumn(3).findElement(By.tagName("a"));
			String[] villageCoordArray = villageLink.getText().replaceAll("\\s+", "").substring(1, 8).split("\\|");
			Point villageCoord = new Point(Integer.valueOf(villageCoordArray[0]), Integer.valueOf(villageCoordArray[1]));
			this.destCoords = villageCoord;
		}
		return this.destCoords;
	}

	public boolean isGettingAttacked() {
		if (this.isGettingAttacked == null) {
			boolean alreadyAttacking = false;
			List<WebElement> attackImage = getColumn(3).findElements(By.tagName("img"));
			if (attackImage.size() > 0) {
				alreadyAttacking = true;
			}
			this.isGettingAttacked = alreadyAttacking;
		}
		
		return this.isGettingAttacked;
	}

	public Date getLastFarmingTime() {
		if (this.lastFarmingTime == null) {
			String lastFarmingTimeString = getColumn(4).getText();
			this.lastFarmingTime = parseLastFarmingStringToDate(lastFarmingTimeString);
			this.lastFarmingTime = parseLastFarmingStringToDate(lastFarmingTimeString);
		}
		return this.lastFarmingTime;
	}

	/**
	 * @param resourcePosition
	 *            the position of the resource (wood = 0, clay = 1, iron = 2)
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

	public Resource getResources() {
		if (this.resources == null) {
			this.resources = new Resource(getResources(WOOD_POSITION), getResources(CLAY_POSITION), getResources(IRON_POSITION));
		}
		return this.resources;
	}

	public int getWallLevel() {
		if (this.wall == null) {
			int wallLevel;
			try {
				wallLevel = Integer.valueOf(getColumn(6).getText());
			} catch (java.lang.NumberFormatException e) {
				wallLevel = -1;
			}
			this.wall = wallLevel;
		}
		
		return this.wall;
	}

	public double getDistance() {
		if (this.distance == null) {
			this.distance = Double.valueOf(getColumn(7).getText());
		}
		return this.distance;
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
		return isFarmButtonEnabled(FarmButton.valueOf(farmTemplate));
	}

	public void clickFarmButton(FarmButton withFarmButton) {
		WebElement farmButton = getFarmbutton(withFarmButton);
		if (farmButton != null) {
			config.getJavascriptExecutor().executeScript(String.format("window.scrollTo(0, %s);", farmButton.getLocation().y));
			farmButton.click();
		} else {
			System.out.println(withFarmButton.name() + "-Button ist nicht vorhanden!");
		}
	}
}
