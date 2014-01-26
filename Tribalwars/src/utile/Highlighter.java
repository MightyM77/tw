package utile;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Highlighter {

	private static Highlighter instance = null;
	private String color = "red";
	private int flashDuration = 100;
	private int flashRounds = 3;
	
	private Highlighter() {
		// singleton
	}

	public static Highlighter getInstance() {
		if (instance == null) {
			instance = new Highlighter();
		}
		return instance;
	}

	public void setColor(String colorName) {
		this.color = colorName;
	}

	private void unHighlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
				element, "");
	}
	
	private void unHighlightElements(WebDriver driver, List<WebElement> elements) {
		for (WebElement element : elements) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
					element, "");
		}
	}

	public void highlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
				element, "color: yellow; border: 2px solid " + this.color + ";");
	}

	public void highlightElementFlash(WebDriver driver, WebElement element) {
		for (int i = 0; i < this.flashRounds; i++) {
			highlightElement(driver, element);
			try {
				Thread.sleep(this.flashDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			unHighlightElement(driver, element);
			try {
				Thread.sleep(this.flashDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void highlightElements(WebDriver driver, List<WebElement> elements) {
		for (WebElement element : elements) {
			highlightElement(driver, element);
		}
	}

	public void highlightElementsFlash(WebDriver driver,
			List<WebElement> elements) {
		for (int i = 0; i < this.flashRounds; i++) {
			highlightElements(driver, elements);
			try {
				Thread.sleep(this.flashDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			unHighlightElements(driver, elements);
			try {
				Thread.sleep(this.flashDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
