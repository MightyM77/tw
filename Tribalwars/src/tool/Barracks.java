package tool;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utile.ResourceBundleUtil;
import utile.Troop;

public class Barracks extends Site {

	public Barracks(WebDriver pDriver) {
		super("/game.php", "barracks", pDriver);
		// TODO Auto-generated constructor stub
	}
	
	private WebElement getRecruitBtn() {
		return driver().findElement(By.xpath("//input[@type='submit' and @value='" + ResourceBundleUtil.getBarracksBundleString("recruit") + "']"));
	}
	
	private WebElement getRecruitInput(Troop troop) {
		List<WebElement> recruitInput = driver().findElements(By.id(troop.getId() + "_0"));
		assertThat("Die Truppe " + troop.getId() + " kann nicht in der Kaserne rekrutiert werden", recruitInput.toArray(), arrayWithSize(1));
		return recruitInput.get(0);
	}
	
	private WebElement getMaxRecruitLink(Troop troop) {
		List<WebElement> maxRecruitLink = driver().findElements(By.id(troop.getId() + "_0_a"));
		assertThat("Die Truppe " + troop.getId() + " kann nicht in der Kaserne rekrutiert werden", maxRecruitLink.toArray(), arrayWithSize(1));
		return maxRecruitLink.get(0);
	}
	
	public void recruit(Troop troop, int amount) {
		getRecruitInput(troop).sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(amount));
		getRecruitBtn().click();
	}

	public void recruitMax(Troop troop) {
		getMaxRecruitLink(troop).click();
		getRecruitBtn().click();
	}
}
