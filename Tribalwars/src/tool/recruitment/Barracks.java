package tool.recruitment;

import org.openqa.selenium.WebDriver;

public class Barracks extends RecruitmentBuilding {

	public Barracks(WebDriver pDriver) {
		super("/game.php", "barracks", pDriver);
	}
}