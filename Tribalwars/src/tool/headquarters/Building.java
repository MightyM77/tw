package tool.headquarters;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebElement;

public class Building {

	private boolean fullyConstructed;
	private String name;
	private int level;
	private int woodForNextLevel;
	private int clayForNextLevel;
	private int ironForNextLevel;
	private int buildingDurationSeconds;
	private int populationForNextLevel;
	private Date resourcesAvailable;
	private WebElement levelUp;
	
	public Building(boolean pFullyConstructed, String pName, int pLevel, int pWoodForNextLevel, int pClayForNextLevel, int pIronForNextLevel, int pBuildingDurationSeconds, int pPopulationForNextLevel, Date pResourcesAvailable, WebElement pLevelUp) {
		this.fullyConstructed = pFullyConstructed;
		this.name = pName;
		this.level = pLevel;
		this.woodForNextLevel = pWoodForNextLevel;
		this.clayForNextLevel = pClayForNextLevel;
		this.ironForNextLevel = pIronForNextLevel;
		this.buildingDurationSeconds = pBuildingDurationSeconds;
		this.populationForNextLevel = pPopulationForNextLevel;
		this.resourcesAvailable = pResourcesAvailable;
		this.levelUp = pLevelUp;
	}

	public boolean isFullyConstructed() {
		return fullyConstructed;
	}

	public void setFullyConstructed(boolean fullyConstructed) {
		this.fullyConstructed = fullyConstructed;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getWoodForNextLevel() {
		return woodForNextLevel;
	}

	public void setWoodForNextLevel(int woodForNextLevel) {
		this.woodForNextLevel = woodForNextLevel;
	}

	public int getClayForNextLevel() {
		return clayForNextLevel;
	}

	public void setClayForNextLevel(int clayForNextLevel) {
		this.clayForNextLevel = clayForNextLevel;
	}

	public int getIronForNextLevel() {
		return ironForNextLevel;
	}

	public void setIronForNextLevel(int ironForNextLevel) {
		this.ironForNextLevel = ironForNextLevel;
	}

	public int getBuildingDurationSeconds() {
		return buildingDurationSeconds;
	}

	public void setBuildingDurationSeconds(int buildingDurationSeconds) {
		this.buildingDurationSeconds = buildingDurationSeconds;
	}

	public int getPopulationForNextLevel() {
		return populationForNextLevel;
	}

	public void setPopulationForNextLevel(int populationForNextLevel) {
		this.populationForNextLevel = populationForNextLevel;
	}

	public Date getResourcesAvailable() {
		return resourcesAvailable;
	}

	public void setResourcesAvailable(Date resourcesAvailable) {
		this.resourcesAvailable = resourcesAvailable;
	}

	public WebElement getLevelUp() {
		return levelUp;
	}

	public void setLevelUp(WebElement levelUp) {
		this.levelUp = levelUp;
	}
	
	public void levelUp() {
		if (this.resourcesAvailable == null && this.levelUp != null) {
			this.levelUp.click();
		} else if (this.resourcesAvailable != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
			System.out.println("Levelup von " + this.name + " erst am " + sdf.format(this.resourcesAvailable) + " vorhanden");
		} else {
			System.out.println("Levelup von " + this.name + " nicht möglich, Gebäude ist bereits voll ausgebaut");
		}
	}
	
	
}
