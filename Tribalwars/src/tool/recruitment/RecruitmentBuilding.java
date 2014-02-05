package tool.recruitment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tool.Site;
import utile.ResourceBundleUtil;
import utile.Troop;

public class RecruitmentBuilding extends Site {

	public RecruitmentBuilding(String pFile, String screen, WebDriver pDriver) {
		super(pFile, screen);
	}

	private WebElement getRecruitBtn() {
		return driver().findElement(By.xpath("//input[@type='submit' and @value='" + ResourceBundleUtil.getBarracksBundleString("recruit") + "']"));
	}
	
	private WebElement getRecruitInput(Troop troop) {
		return getNotNullTr(troop).findElement(By.id(troop.getId() + "_0"));
	}
	
	private WebElement getMaxRecruitLink(Troop troop) {
		return getNotNullTr(troop).findElement(By.id(troop.getId() + "_0_a"));
	}
	
	private WebElement getNotNullTr(Troop troop) {
		WebElement trElement = getTr(troop);
		assertThat("Die Truppe " + troop.getId() + " kann (noch) nicht in der Kaserne rekrutiert werden", trElement, notNullValue());
		return trElement;
	}
	
	private WebElement getTr(Troop troop) {
		List<WebElement> imgs = driver().findElements(By.tagName("img"));
		if (imgs.size() > 0){
			for (WebElement img : imgs) {
				if (img.getAttribute("src").contains(troop.getId() + ".png")) {
					return img.findElement(By.xpath("../../.."));
				}
			}
		}
		return null;
	}
	
	private WebElement getRequirementsDiv(Troop troop) {
		return getNotNullTr(troop).findElement(By.className("recruit_req"));
	}
	
	public void recruit(Troop troop, int amount) {
		getRecruitInput(troop).sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(amount));
		getRecruitBtn().click();
	}

	public void recruitMax(Troop troop) {
		getMaxRecruitLink(troop).click();
		getRecruitBtn().click();
	}

	public int getWoodCosts(Troop troop) {
		List<WebElement> requirements = getRequirementsDiv(troop).findElements(By.tagName("span"));
		return Integer.valueOf(requirements.get(0).getText().replaceAll("[^0-9]", ""));
	}
	
	public int getClayCosts(Troop troop) {
		List<WebElement> requirements = getRequirementsDiv(troop).findElements(By.tagName("span"));
		return Integer.valueOf(requirements.get(1).getText().replaceAll("[^0-9]", ""));
	}
	
	public int getIronCosts(Troop troop) {
		List<WebElement> requirements = getRequirementsDiv(troop).findElements(By.tagName("span"));
		return Integer.valueOf(requirements.get(2).getText().replaceAll("[^0-9]", ""));
	}
	
	public int getPopulation(Troop troop) {
		List<WebElement> requirements = getRequirementsDiv(troop).findElements(By.tagName("span"));
		return Integer.valueOf(requirements.get(3).getText().replaceAll("[^0-9]", ""));
	}
	
	public int getRecruitmentSeconds(Troop troop) {
		List<WebElement> span = getRequirementsDiv(troop).findElements(By.xpath(".//span"));
		String text = span.get(8).getText();
		String[] time = text.split("\\:");
		
		int seconds = 0;
		seconds += Integer.valueOf(time[0]) * 3600;
		seconds += Integer.valueOf(time[1]) * 60;
		seconds += Integer.valueOf(time[2]);
		
		return seconds;
	}
	
	public int getMaxRecruit(Troop troop) {
		if (isMaxLinkBtnEnabled(troop)) {
			return Integer.valueOf(getMaxRecruitLink(troop).getText().replaceAll("[^0-9]", ""));
		} else {
			return 0;
		}
	}
	
	public boolean isUnitAvailable(Troop troop) {
		WebElement troopTr = getTr(troop);
		if (troopTr == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isMaxLinkBtnEnabled(Troop troop) {
		List<WebElement> maxLink = getNotNullTr(troop).findElements(By.id(troop.getId() + "_0_a"));
		if (maxLink.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
