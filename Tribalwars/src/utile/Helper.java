package utile;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class Helper {

	private static final Helper instance = new Helper();
	
	private Helper() {
		// singleton
	}
	
	public static Helper getInstance() {
		return instance;
	}
	
	public void setCheckboxState(WebElement checkbox, boolean state) {
		if (state && !checkbox.isSelected()) {
			checkbox.click();
		} else if (!state && checkbox.isSelected()) {
			checkbox.click();
		}
	}
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
