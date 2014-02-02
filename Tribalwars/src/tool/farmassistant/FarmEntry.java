package tool.farmassistant;

import java.awt.Point;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebElement;

import utile.ReportStatus;
import utile.Troop;

public class FarmEntry {

	private WebElement deleteButton;
	private ReportStatus reportStatus;
	private boolean maxLoot;
	private WebElement villageLink;
	private int attacks;
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

	public FarmEntry(WebElement pDeleteButton, ReportStatus pReportStatus, boolean pMaxLoot, WebElement pVillageLink, int pAttacks, Point pVillageCoord, Date pLastFarmingTime, int pWood, int pClay, int pIron,
			int pWall, double pDistance, FarmButton pAbutton, FarmButton pBbutton, FarmButton pCbutton, WebElement pReallyPoint) {
		this.deleteButton = pDeleteButton;
		this.reportStatus = pReportStatus;
		this.maxLoot = pMaxLoot;
		this.villageLink = pVillageLink;
		this.attacks = pAttacks;
		this.villageCoord = pVillageCoord;
		this.lastFarmingTime = pLastFarmingTime;
		this.clay = pClay;
		this.wood = pWood;
		this.iron = pIron;
		this.wallLevel = pWall;
		this.distance = pDistance;
		this.aButton = pAbutton;
		this.bButton = pBbutton;
		this.cButton = pCbutton;
		this.reallyPointLink = pReallyPoint;
	}

	public WebElement getDeleteButton() {
		return this.deleteButton;
	}

	public WebElement getVillageLink() {
		return this.villageLink;
	}

	public Point getVillageCoord() {
		return villageCoord;
	}

	public int getAttacks() {
		return this.attacks;
	}
	
	public Date getLastFarmingTime() {
		return lastFarmingTime;
	}

	public int getClay() {
		return clay;
	}

	public int getWood() {
		return wood;
	}

	public int getIron() {
		return iron;
	}

	public int getWallLevel() {
		return wallLevel;
	}

	private double getDistance() {
		return distance;
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

	public FarmButton getaButton() {
		return aButton;
	}

	public FarmButton getbButton() {
		return bButton;
	}

	public FarmButton getcButton() {
		return cButton;
	}

	public WebElement getReallyPointLink() {
		return reallyPointLink;
	}

	public ReportStatus getFarmStatus() {
		return reportStatus;
	}

	public boolean isMaxLoot() {
		return maxLoot;
	}
}
