package main.procedure;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;

import utile.Helper;
import config.TwConfiguration;

public abstract class Procedure{

	private final long activationTimeMillis;
	private final TwConfiguration config;
	
	public Procedure(TwConfiguration pConfig, long pActivationTimeMillis) {
		this.config = pConfig;
		this.activationTimeMillis = pActivationTimeMillis;
		TwConfiguration.LOGGER.debug("Neuer " + Helper.getInstance().getClassName(getClass()) + " erstellt, Aktivierungszeit: " + createCalendarOfActivationTime().getTime());
	}
	
	protected TwConfiguration getTwConfig() {
		return config;
	}
	
	public abstract List<Procedure> doAction() throws ParseException;
	
	protected WebDriver driver() {
		return getTwConfig().getDriver();
	}
	
	public long getActivationTimeInMillis() {
		return this.activationTimeMillis;
	}
	
	public Calendar createCalendarOfActivationTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.activationTimeMillis);
		return cal;
	}
	
	public boolean isReadyForActivating() {
		if (Calendar.getInstance().getTimeInMillis() >= activationTimeMillis) {
			return true;
		}
		return false;
	}
}
