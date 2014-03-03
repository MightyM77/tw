package main.procedure;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;

import utile.Helper;
import config.TwConfiguration;

public abstract class Procedure{

	private final Calendar activationTime;
	private final TwConfiguration config;
	
	public Procedure(TwConfiguration pConfig, Calendar pActivationTime) {
		this.config = pConfig;
		this.activationTime = pActivationTime;
		TwConfiguration.LOGGER.debug("Neuer " + Helper.getInstance().getClassName(getClass()) + " erstellt, Aktivierungszeit: " + getActivationTime().getTime());
	}
	
	protected TwConfiguration config() {
		return config;
	}
	
	public abstract List<Procedure> doAction() throws ParseException;
	
	protected WebDriver driver() {
		return config().getDriver();
	}
	
	public Calendar getActivationTime() {
		return this.activationTime;
	}
}
