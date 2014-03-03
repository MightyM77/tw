package tool;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utile.ResourceBundleUtil;
import config.TwConfiguration;

public class Tribalwars extends Site {
	
	public Tribalwars(TwConfiguration pConfig) {
		super(pConfig, "", "");
	}
	
	@Override
	public void goToSite() {
		String url = "http://www." + ResourceBundleUtil.getGeneralBundleString("hostname", config().getLocale());
		driver().get(url);
	}

	private WebElement getLoginButton() {
		List<WebElement> middle_buttons = findElements(By
				.className("button_middle"));
		WebElement loginButton = null;
		for (WebElement middle_button : middle_buttons) {
			if (middle_button.getText().equals("Login")) {
				loginButton = middle_button;
			}
		}

		return loginButton;
	}

	private WebElement getActiveWorldButton(int pWorld) {
		WebElement worldButton = null;
		List<WebElement> middle_buttons = findElements(By
				.className("world_button_active"));
		for (WebElement middle_button : middle_buttons) {
			if (middle_button.getText().equals("World " + pWorld)) {
				worldButton = middle_button;
			}
		}

		return worldButton;
	}

	public void login() {
		WebElement username = findElement(By.id("user"));
		username.sendKeys(config().getUsername());
		WebElement password = findElement(By.id("password"));
		password.sendKeys(config().getPassword());

		getLoginButton().click();
		getActiveWorldButton(config().getWorld()).click();
	}
}
