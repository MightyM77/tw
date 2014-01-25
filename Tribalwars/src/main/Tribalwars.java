package main;
import java.util.List;
import java.util.ResourceBundle;

import main.config.Configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Tribalwars extends Site {
	
	public Tribalwars(WebDriver pDriver) {
		super("", pDriver);
	}
	
	@Override
	public void goToSite() {
		String url = "http://www." + ResourceBundleUtil.getGeneralBundleString("hostname");
		this.driver.get(url);
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
		username.sendKeys(Configuration.USERNAME);
		WebElement password = findElement(By.id("password"));
		password.sendKeys(Configuration.PASSWORD);

		getLoginButton().click();
		getActiveWorldButton(Configuration.WORLD).click();
	}
}
