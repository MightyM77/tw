package main.procedure;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;

import utile.Helper;
import config.Configuration;

public abstract class Procedure{

	private final Calendar activationTime;
	
	public Procedure(Calendar pActivationTime) {
		this.activationTime = pActivationTime;
		Configuration.LOGGER.debug("Neuer " + Helper.getInstance().getClassName(getClass()) + " erstellt, Aktivierungszeit: " + getActivationTime().getTime());
	}
	
	public abstract List<Procedure> doAction() throws ParseException;
	
	protected WebDriver driver() {
		return Configuration.DRIVER;
	}
	
	public Calendar getActivationTime() {
		return this.activationTime;
	}
}
