package tool;

import java.awt.Point;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utile.Resource;
import config.TwConfiguration;

public class Market extends Site {
	
	private Market(TwConfiguration pConfig) {
		super(pConfig, "/game.php", "market");
	}
	
	private WebElement getMaxLink(String resourceId) {
		return findElement(By.className("a.insert " + resourceId));
	}
	
	private WebElement getInputX() {
		return findElement(By.id("inputx"));
	}
	
	private WebElement getInputY() {
		return findElement(By.id("inputy"));
	}
	
	private WebElement getOkBtn() {
		return findElement(By.xpath(".//input[@value='OK']"));
	}
	
	public void sendMax(Point destinationCoords) {
		getInputX().sendKeys(String.valueOf((int) destinationCoords.getX()));
		getInputY().sendKeys(String.valueOf((int) destinationCoords.getY()));
		getMaxLink(Resource.IRON_ID).click();
		getMaxLink(Resource.WOOD_ID).click();
		getMaxLink(Resource.CLAY_ID).click();
		getOkBtn().click();
	}
}
