package test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tool.Site;
import utile.Highlighter;

public class Google extends Site {

	WebElement test = null;
	
	public Google() {
		super("", "");
	}
	
	@Override
	public void goToSite() {
		String url = "http://www.google.ch";
		driver().get(url);
	}

	public WebElement getSearchBtn() {
		return findElement(By.id("gbqfba"));
	}

	public WebElement getSearchTxt() {
		return findElement(By.id("gs_htif0"));
	}
	
	public WebElement getAnmeldenBtn() {
		return findElement(By.id("gb_70"));
	}
	
	public List<WebElement> getSome() {
		return findElements(By.id("asdfhsfdagadfhns"));
	}

	public WebElement getNull() {
		return this.test;
	}
	


}
