package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tool.Site;

public class Tribalwars extends Site {

	public Tribalwars() {
		super("", "");
	}

	@Override
	public void goToSite() {
		String url = "http://www.tribalwars.us";
		driver().get(url);
	}
	
	public WebElement getRememberMeInput() {
		return driver().findElement(By.id("cookie"));
	}
	
	public WebElement getUsernameInput() {
		return driver().findElement(By.id("user"));
	}
}
