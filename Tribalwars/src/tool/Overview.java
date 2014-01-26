package tool;

import org.openqa.selenium.WebDriver;

public class Overview extends Site {

	public Overview(WebDriver pDriver) {
		super("/game.php", "overview", pDriver);
	}

}
