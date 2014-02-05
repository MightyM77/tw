package main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;

import config.Configuration;

public abstract class Procedure{

	private final Calendar activationTime;
	
	public Procedure(Calendar pActivationTime) {
		this.activationTime = pActivationTime;
	}
	
	public abstract List<Procedure> doAction();
	
	protected WebDriver driver() {
		return Configuration.DRIVER;
	}
	
	public Calendar getActivationTime() {
		return this.activationTime;
	}
}
