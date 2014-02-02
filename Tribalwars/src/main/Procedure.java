package main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;

public abstract class Procedure{

	private final WebDriver driver;
	private final Calendar activationTime;
	
	public Procedure(WebDriver pDriver, Calendar pActivationTime) {
		this.driver = pDriver;
		this.activationTime = pActivationTime;
	}
	
	public abstract List<Procedure> doAction();
	
	protected WebDriver driver() {
		return this.driver;
	}
	
	public Calendar getActivationTime() {
		return this.activationTime;
	}
}
