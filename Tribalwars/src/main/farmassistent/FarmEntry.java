package main.farmassistent;

import java.awt.Point;
import java.util.Date;

import org.openqa.selenium.WebElement;

public class FarmEntry {

	public static int FARMSTATUS_GREEN = 0;
	public static int FARMSTATUS_YELLOW = 1;
	public static int FARMSTATUS_RED = 2;
	public static int FARMSTATUS_BLUE = 3;
	
	private WebElement deleteButton;
	private int farmStatus;
	private boolean maxLoot;
	private WebElement villageLink;
	private Point villageCoord;
	private Date lastFarmingTime;
	private int clay;
	private int wood;
	private int iron;
	private int wallLevel;
	private double distance;
	private FarmButton aButton;
	private FarmButton bButton;
	private FarmButton cButton;
	private WebElement reallyPointLink;
	
	public FarmEntry() {
		
	}
	
	public WebElement getDeleteButton() {
		return this.deleteButton;
	}
	public void setDeleteButton(WebElement pDeleteButton) {
		this.deleteButton = pDeleteButton;
	}
	public WebElement getVillageLink() {
		return this.villageLink;
	}
	public void setVillageLink(WebElement pVillageLink) {
		this.villageLink = pVillageLink;
	}
	public Point getVillageCoord() {
		return villageCoord;
	}
	public void setVillageCoord(Point villageCoord) {
		this.villageCoord = villageCoord;
	}
	public Date getLastFarmingTime() {
		return lastFarmingTime;
	}
	
	public void setLastFarmingTime(Date lastFarmingTime) {
		this.lastFarmingTime = lastFarmingTime;
	}
	public int getClay() {
		return clay;
	}
	
	
	
	public void setClay(int clay) {
		this.clay = clay;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getIron() {
		return iron;
	}
	public void setIron(int iron) {
		this.iron = iron;
	}
	public int getWallLevel() {
		return wallLevel;
	}
	public void setWallLevel(int wallLevel) {
		this.wallLevel = wallLevel;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public FarmButton getaButton() {
		return aButton;
	}
	public void setaButton(FarmButton aButton) {
		this.aButton = aButton;
	}
	public FarmButton getbButton() {
		return bButton;
	}
	public void setbButton(FarmButton bButton) {
		this.bButton = bButton;
	}
	public FarmButton getcButton() {
		return cButton;
	}
	public void setcButton(FarmButton cButton) {
		this.cButton = cButton;
	}
	public WebElement getReallyPointLink() {
		return reallyPointLink;
	}
	public void setReallyPointLink(WebElement reallyPointLink) {
		this.reallyPointLink = reallyPointLink;
	}
	public int getFarmStatus() {
		return farmStatus;
	}
	public void setFarmStatus(int farmStatus) {
		this.farmStatus = farmStatus;
	}
	public boolean isMaxLoot() {
		return maxLoot;
	}
	public void setMaxLoot(boolean maxLoot) {
		this.maxLoot = maxLoot;
	}
}
