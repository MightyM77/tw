package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import config.Configuration;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import tool.headquarters.Building;
import tool.headquarters.Headquarters;
import tool.reports.ReportEntry;
import tool.reports.Reports;
import tool.reports.Submenu;
import utile.Helper;
import utile.ReportStatus;
import utile.ResourceBundleUtil;
import utile.Troop;
import utile.TroopTemplate;

public class Tester {

	private SimpleDateFormat someDateFormat = new SimpleDateFormat();
	
	public Tester() {
	}
	
	private WebDriver driver() {
		return Configuration.DRIVER;
	}
	
	public void headquartersTest() {
		Headquarters hq = new Headquarters();
		hq.goToSite();
		
		List<Building> buildings = hq.getAvailableBuildings();
		
		for (Building building : buildings) {
			System.out.println("------------------ " + building.getName() + " --------------------");
			if (building.getLevel() == 0) {
				System.out.println("Level: noch nicht gebaut");
			} else {
				System.out.println("Level: " + building.getLevel());
			}
			
			if (building.isFullyConstructed()) {
				System.out.println("GEBÄUDE VOLLSTÄNDIG AUSGEBAUT");
			} else {
				System.out.println("Holz für nächste Stufe: " + building.getWoodForNextLevel());
				System.out.println("Lehm für nächste Stufe: " + building.getClayForNextLevel());
				System.out.println("Eisen für nächste Stufe: " + building.getIronForNextLevel());
				
				int available = building.getBuildingDurationSeconds();
				
				long stunden = TimeUnit.SECONDS.toHours(available);
				long minuten = TimeUnit.SECONDS.toMinutes(available)-TimeUnit.HOURS.toMinutes(stunden);
				long sekunden = available - TimeUnit.HOURS.toSeconds(stunden) - TimeUnit.MINUTES.toSeconds(minuten);
				String buildingTimeOutput = String.format("Bauzeit nächste Stufe: %02d:%02d:%02d", stunden, minuten, sekunden);
				System.out.println(buildingTimeOutput);
				System.out.println("Benötigte Bevölkerung nächste Stufe: " + building.getPopulationForNextLevel());
				if (building.getLevelUp() != null) {
					System.out.println("Ressourcen für Levelup verfügbar!");
				} else {
					someDateFormat.applyPattern("dd.MM. hh:mm:ss");
					System.out.println("Ressourcen für Levelup am " + someDateFormat.format(building.getResourcesAvailable()) + " verfügbar");
				}
			}
			
			hq.renameVillage("00000001");
			
			
		}
	}
	
//	public void farmAssistantTest() {
//		Farmassistant fm = Farmassistant.getInstance();
//		fm.goToSite();
//
//// Mögliche Beute der Templates
//		System.out.println("---------------- FARMASSISTENT INFORMATIONEN ----------------");
//		System.out.println("A-Template mögliche Beute: " + fm.getAtemplateHaulCapacity());
//		System.out.println("B-Template mögliche Beute: " + fm.getBtemplateHaulCapacity());
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//// Momentane Truppen des A-Templates
//		System.out.println("A-Template Truppen:");
//		TroopTemplate aTemplateTroops = fm.getTemplateTroops(FarmTemplate.A);
//		Iterator<Entry<Troop, Integer>> iterator = aTemplateTroops.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
//			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey().getId()) + ": " + pairs.getValue());
//		}
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//// Momentane Truppen des B-Templates
//		System.out.println("B-Template Truppen:");
//		Map<Troop, Integer> bTemplateTroops = fm.getBtemplateTroops();
//		iterator = bTemplateTroops.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
//			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey().getId()) + ": " + pairs.getValue());
//		}
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//// Momentan verfügbare Truppen
//		System.out.println("Momentan verfügbare Truppen:");
//		Map<Troop, Integer> availableTemplateTroops = fm.getAvailableTroops();
//		iterator = availableTemplateTroops.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
//			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey().getId()) + ": " + pairs.getValue());
//		}
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//// Alle Truppen zum Farmen verfügbar machen
//		System.out.println("Alle Truppen zum Farmen verfügbar machen...");
//		String[] availableTroops = new String[] {Troop.ARCHER.getId(), Troop.AXE.getId(), Troop.HEAVY.getId(), Troop.PALADIN.getId(), Troop.LIGHT.getId(), Troop.MARCHER.getId(), Troop.SPEAR.getId(), Troop.SPY.getId(), Troop.SWORD.getId()};
//		fm.setAvailableTroops(availableTroops);
//		System.out.println("-------------------------------------------------------------");
//
//		System.out.println("Show only... und Show reports... aktivieren");
//// Show only... und Show reports... einschalten
//		fm.setOnlyAttacksFromThisVillage(true);
//		fm.setOnlyFullLosses(true);
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("Vorherige Seite, Nächste Seite, Vorherige Seite");
//		fm.previousPage();
//		fm.nextPage();
//		fm.previousPage();
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("A-Template neu setzen");
//		fm.setAtemplateTroops(0, 10, 4, 345, 123, 23, 725, 272, 1);
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("B-Template neu setzen");
//		fm.setBtemplateTroops(2132, 10000, 124, 3345, 1213, 233, 7225, 2732, 4);
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("Nach Datum aufwärts sortieren");
//		fm.sortByDateAufwaerts();
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("Nach Datum abwärts sortieren");
//		fm.sortByDateAbwaerts();
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("Nach Distanz aufwärts sortieren");
//		fm.sortByDistanceAufwaerts();
//		System.out.println("-------------------------------------------------------------");
//		Helper.getInstance().sleep(3);
//		System.out.println("Nach Distanz abwärts sortieren");
//		fm.sortByDistanceAbwaerts();
//	}
//
//	public void reportsTester() {
//		Reports reports = Reports.getInstance();
//		reports.goToSite();
//		
//		List<ReportEntry> reportEntries = new ArrayList<ReportEntry>();
//		
//		for (int i : reports.getReportIds()) {
//			reportEntries.add(reports.getReport(i));
//		}
//		
//		for (ReportEntry re : reportEntries) {
//			System.out.println("------------------------------------");
//			System.out.println("ID: " + re.getId());
//			System.out.println("Name: " + re.getName());
//			System.out.println("Received: " + re.getReceivedDate());
//		}
//		
//		reports.goToSubmenu(Submenu.SUBMENU_ATTACKS);
//		reports.setFilter(ReportStatus.SOME_LOSSES);
//		reports.filterByReportName("barbarian");
//		reports.setOnlyFullHauls(true);
//		reports.setOnlyOwnReports(true);
//		reports.setOnlyReportsForThisVill(true);
//		reports.setReportsPerPage(50);
//		reports.clickDeleteBtn();
//		reports.getReport(11454865).setSelectState(true);
//		reports.forwardSelectedReports("Wagenheber");
//		reports.setFilterToAllTypes();
//		reports.resetAllFilters();
//	}
}
