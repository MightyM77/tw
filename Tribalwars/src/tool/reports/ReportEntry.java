package tool.reports;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utile.Helper;
import utile.ReportStatus;
import utile.ResourceBundleUtil;

public class ReportEntry {

	private static final SimpleDateFormat SOME_DATE_FORMAT = new SimpleDateFormat();
	
	private final WebDriver driver;
	private int id;
	
	public ReportEntry(WebDriver pDriver, int pId) {
		this.driver = pDriver;
		this.id = pId;
	}
	
	private WebElement getTr() {
		List<WebElement> checkbox = driver.findElements(By.xpath(".//input[@type='checkbox' and @name='id_" + String.valueOf(this.id) + "']"));
		assertThat("Report mit der ID '" + String.valueOf(id) + "' nicht vorhanden", checkbox.toArray(), arrayWithSize(1));
		return checkbox.get(0).findElement(By.xpath("..//.."));
	}
	
	private WebElement getCheckBox() {
		return getTr().findElement(By.xpath(".//input[@type='checkbox' and @name='id_" + String.valueOf(this.id) + "']"));
	}
	
	private WebElement getRenameBtn() {
		return getTr().findElement(By.xpath(".//span[@id='label_" + String.valueOf(id) + "']")).findElement(By.xpath(".//a[@class='small']"));
	}
	
	private WebElement getEditInput() {
		return getTr().findElement(By.id("editInput_" + String.valueOf(id)));
	}
	
	private WebElement getEditOkBtn() {
		return getTr().findElement(By.xpath(".//input[@type='button' and @value='OK']"));
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setSelectState(boolean state) {
		Helper.getInstance().setCheckboxState(getCheckBox(), state);
	}
	
	public void rename(String newName) {
		getRenameBtn().click();
		assertThat(getEditInput().findElement(By.xpath("..")).getCssValue("display"), not("none"));
		getEditInput().sendKeys(Keys.chord(Keys.CONTROL, "a"), newName);
		getEditOkBtn().click();
	}
	
	/**
	 * Gibt den Reportstatus eines Berichts zurück. 
	 * Wenn der Bericht keinen Reportstatus besitzt (handel, awards, sonstiges etc.) wird null zurückgegeben
	 * @return Reportstatus (wenn nicht vorhanden null)
	 */
	public ReportStatus getReportStatus() {
		ReportStatus reportStatus = null;
		List<WebElement> reportStatusElement = getTr().findElements(By.xpath(".//img[@class='' and @alt='' and @title='']"));
		if (reportStatusElement.size() == 1) {
			String imgSrc = reportStatusElement.get(0).getAttribute("src");
			reportStatus = ReportStatus.stringContainsReportStatusColor(imgSrc);
		}
		return reportStatus;
	}
	
	public String getName() {
		WebElement nameElement = getTr().findElement(By.id("labelText_" + String.valueOf(id)));
		return nameElement.getText();
	}
	
	public Date getReceivedDate() {
		Date received = null;
		String dateFormat = ResourceBundleUtil.getReportsBundleString("reportEntryDateFormat");
		SOME_DATE_FORMAT.applyPattern(dateFormat);
		String dateString = getTr().findElements(By.tagName("td")).get(1).getText();
		try {
			received = SOME_DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			System.out.println("'" + dateString + "' konnte nicht in ein Datum mit dem Format " + dateFormat + " umgewandelt werden");
			e.printStackTrace();
		}
		return received;
	}
	
	public boolean isNew() {
		return getTr().findElements(By.tagName("td")).get(0).getText().contains(ResourceBundleUtil.getReportsBundleString("new"));
	}
}
