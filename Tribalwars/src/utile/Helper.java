package utile;

import java.util.Calendar;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import config.Configuration;

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
	
	public String getClassName(Class<?> clazz) {
		Class<?> enclosingClass = clazz.getEnclosingClass();
		if (enclosingClass != null) {
			return enclosingClass.getName();
		} else {
		  return clazz.getSimpleName();
		}
	}
	
	public Calendar getCalendar() {
		return Calendar.getInstance(Configuration.TIME_ZONE);
	}
}
