package tool.reports;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import tool.Site;
import utile.Helper;
import utile.ReportStatus;
import utile.ResourceBundleUtil;
import config.TwConfiguration;

public class Reports extends Site {
	
	public static final int FOLDER_ALL = -1;
	public static final int FOLDER_NEW = 0;

	private Reports(TwConfiguration pConfig) {
		super(pConfig, "/game.php", "report");
	}
	
	public void goToSubmenu(Submenu submenu) {
		String submenuName = submenu.getSubmenuName();
		if (Submenu.contains(submenuName)) {
			if (!this.urlParameters.containsKey("mode") || !this.urlParameters.get("mode").equals(submenuName)) {
				this.urlParameters.put("mode", submenuName);
				goToSite();
			}
			this.urlParameters.put("mode", submenuName);
		} else {
			System.out.println("'" + submenuName + "' ist ein ungültiges Submenü");
		}
	}

	public void setFilter(ReportStatus filter) {
		findElement(By.id(filter.getFilterId())).click();
	}

	public void setFilterToAllTypes() {
		findElement(By.id("filter_dots_none")).click();
	}

	public void resetAllFilters() {
		String resetFiltersName = ResourceBundleUtil.getReportsBundleString("resetFilters", config().getLocale());
		findElement(By.xpath("//input[@value='" + resetFiltersName + "']")).click();
	}

	public void filterByReportName(String name) {
		WebElement filterInput = findElement(By.id("filter_subject"));
		filterInput.click();
		filterInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), name);
		filterInput.findElement(By.xpath("..//input[@value='OK']")).click();
	}

	public void setOnlyReportsForThisVill(boolean state) {
		WebElement checkbox = findElement(By.id("filter_current_village"));
		Helper.getInstance().setCheckboxState(checkbox, state);
	}

	public void setOnlyFullHauls(boolean state) {
		WebElement checkbox = findElement(By.id("filter_max_loot"));
		Helper.getInstance().setCheckboxState(checkbox, state);
	}

	public void setOnlyOwnReports(boolean state) {
		WebElement checkbox = findElement(By.id("filter_own_reports"));
		Helper.getInstance().setCheckboxState(checkbox, state);
	}

	public void setReportsPerPage(int value) {
		WebElement pagesInput = findElement(By.xpath("//input[@type='text' and @name='page_size']"));
		pagesInput.click();
		pagesInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(value));
		pagesInput.findElement(By.xpath("..//..//input[@class='btn' and @value='OK']")).click();
	}

	public void clickDeleteBtn() {
		String value = ResourceBundleUtil.getReportsBundleString("delete", config().getLocale());
		findElement(By.xpath("//input[@value='" + value + "']")).click();
	}

	public void forwardSelectedReports(String recipient) {
		String value = ResourceBundleUtil.getReportsBundleString("forward", config().getLocale());
		findElement(By.xpath("//input[@value='" + value + "']")).click();
		WebElement to = findElement(By.id("to"));
		to.click();
		to.sendKeys(recipient);
		findElement(By.xpath("//input[@type='submit' and @value='" + ResourceBundleUtil.getReportsBundleString("forward", config().getLocale()) +"']")).click();
	}

	private List<WebElement> getReportEntryTrs() {
		List<WebElement> trElements = findElement(By.id("report_list")).findElements(By.tagName("tr"));
		trElements.remove(0);
		trElements.remove(trElements.size()-1);
		return trElements;
	}
	
	private ReportEntry createReportFromId(int id) {
		List<WebElement> checkbox = findElements(By.xpath(".//input[@type='checkbox' and @name='id_" + String.valueOf(id) + "']"));
		assertThat("Report mit der ID '" + String.valueOf(id) + "' nicht vorhanden", checkbox.toArray(), arrayWithSize(1));
		return createReportFromTrElement(checkbox.get(0).findElement(By.xpath("..//..")));
	}
	
	private ReportEntry createReportFromTrElement(WebElement trElement) {
		assertThat("Das Webelement sollte vom Tagtyp <tr> sein", trElement.getTagName(), equalTo("tr"));
		List<WebElement> tdElements = trElement.findElements(By.tagName("td"));
		assertThat("Eine Berichtszeile sollte 2 Spalten haben", tdElements.toArray(), arrayWithSize(2));

		// CHECKBOX
		WebElement checkbox = tdElements.get(0).findElement(By.xpath(".//input[@type='checkbox']"));
		// ID
		int id = Integer.valueOf(checkbox.getAttribute("name").replaceAll("[^0-9]", ""));
		
		ReportEntry reportEntry = new ReportEntry(config(), id);
		return reportEntry;
	}

	public int[] getReportIds() {
		List<WebElement> trElements = getReportEntryTrs();
		int[] ids = new int[trElements.size()];
		WebElement tmpCheckbox;
		int tmpId;
		for (int i = 0; i < trElements.size(); i++) {
			tmpCheckbox = trElements.get(i).findElement(By.xpath(".//input[@type='checkbox']"));
			tmpId = Integer.valueOf(tmpCheckbox.getAttribute("name").replaceAll("[^0-9]", ""));
			ids[i] = tmpId;
		}
		
		return ids;
	}
	
	public ReportEntry getReport(int reportId) {
		return createReportFromId(reportId);
	}
	
	public List<ReportEntry> getReportEntries() {
		List<WebElement> trElements = getReportEntryTrs();
		List<ReportEntry> reportEntries = new ArrayList<ReportEntry>();

		for (WebElement trElement : trElements) {
			reportEntries.add(createReportFromTrElement(trElement));
		}

		return reportEntries;
	}
}
