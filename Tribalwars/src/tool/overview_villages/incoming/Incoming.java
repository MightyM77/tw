package tool.overview_villages.incoming;

import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import config.Configuration;
import tool.overview_villages.OverviewVillages;
import utile.GameHelper;
import utile.Helper;
import utile.ResourceBundleUtil;


public class Incoming extends OverviewVillages {

	private static final String TIME_FORMAT = ResourceBundleUtil.getIncomingBundleString("arrivalTimeFormat");
	private static final String DATE_FORMAT = ResourceBundleUtil.getIncomingBundleString("arrivalDateFormat");
	private static final String TODAY_STRING = ResourceBundleUtil.getIncomingBundleString("today");
	private static final String TOMORROW_STRING = ResourceBundleUtil.getIncomingBundleString("tomorrow");
	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat();
	
	public Incoming(Mode mode) {
		super("incomings");
		this.urlParameters.put("subtype", mode.getModeId());
	}

	private List<WebElement> getIncomingTrs() {
		WebElement table = findElement(By.id("incomings_table"));
		List<WebElement> incomingsTr = table.findElements(By.className("tr.row_a,tr.row_b"));
		return incomingsTr;
	}
	
	private WebElement getIncomingTr(int incomingIndex) {
		return getIncomingTrs().get(incomingIndex);
	}
	
	private List<WebElement> getIncomingTds(int incomingIndex) {
		return getIncomingTr(incomingIndex).findElements(By.tagName("td"));
	}
	
	private Calendar parseArrivalStringToDate(String arrivalString) throws ParseException {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		
		if (arrivalString.contains(TODAY_STRING)) {
			SDF.applyPattern(DATE_FORMAT);
			Date today = cal.getTime();
			arrivalString = arrivalString.replace(TODAY_STRING, SDF.format(today));
		} else if (arrivalString.contains(TOMORROW_STRING)) {
			SDF.applyPattern(DATE_FORMAT);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Date tomorrow = cal.getTime();
			arrivalString = arrivalString.replace(TOMORROW_STRING, SDF.format(tomorrow));
		}
		arrivalString = arrivalString.replaceAll("[^0-9\\.\\:]", "");
		
		SDF.applyPattern(DATE_FORMAT + TIME_FORMAT);
		try {
			cal.setTime(SDF.parse(arrivalString));
			cal.set(Calendar.YEAR, year);
			return cal;
		} catch (ParseException e) {
			Configuration.LOGGER.error("Ankunftszeit konnte nicht in ein Datum umgewandelt werden. Fehlermeldung: " + e.getMessage());
			throw e;
		}
	}
	
	public int getIncomingsCount() {
		return getIncomingTrs().size();
	}
	
	public Point getDestinationCoord(int incomingIndex) {
		WebElement destination = getIncomingTds(incomingIndex).get(1);
		return GameHelper.getInstance().getCoordFromString(destination.getText());
	}
	
	public Point getOriginCoord(int incomingIndex) {
		WebElement origin = getIncomingTds(incomingIndex).get(2);
		return GameHelper.getInstance().getCoordFromString(origin.getText());
	}
	
	public String getAttackerName(int incomingIndex) {
		return getIncomingTds(incomingIndex).get(3).getText();
	}
	
	public Calendar getArrivingTime(int incomingIndex) throws ParseException {
		return parseArrivalStringToDate(getIncomingTds(incomingIndex).get(4).getText());
	}
	
}
