package tool;

import java.awt.Point;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utile.Resource;

public class Market extends Site {

	private static final Market INSTANCE = new Market();
	
	private Market() {
		super("/game.php", "market");
	}
	
	public static Market getInstance() {
		return INSTANCE;
	}
	
	private WebElement getMaxLink(Resource ofResource) {
		return findElement(By.className("a.insert " + ofResource.getId()));
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
		getMaxLink(Resource.IRON).click();
		getMaxLink(Resource.WOOD).click();
		getMaxLink(Resource.CLAY).click();
		getOkBtn().click();
	}
}
