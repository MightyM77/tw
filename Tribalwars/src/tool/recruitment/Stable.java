package tool.recruitment;

import org.openqa.selenium.WebDriver;

public class Stable extends RecruitmentBuilding{

	public Stable(WebDriver pDriver) {
		super("/game.php", "stable", pDriver);
	}

}
