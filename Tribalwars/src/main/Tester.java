package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import tool.farmassistant.Farmassistant;
import tool.headquarters.Building;
import tool.headquarters.Headquarters;
import utile.Helper;
import utile.ResourceBundleUtil;
import utile.Troop;

public class Tester {

	private WebDriver driver;
	private SimpleDateFormat someDateFormat = new SimpleDateFormat();
	
	public Tester(WebDriver pDriver) {
		this.driver = pDriver;
	}
	
	public void headquartersTest() {
		Headquarters hq = new Headquarters(driver);
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
				System.out.println("GEB�UDE VOLLST�NDIG AUSGEBAUT");
			} else {
				System.out.println("Holz f�r n�chste Stufe: " + building.getWoodForNextLevel());
				System.out.println("Lehm f�r n�chste Stufe: " + building.getClayForNextLevel());
				System.out.println("Eisen f�r n�chste Stufe: " + building.getIronForNextLevel());
				
				int available = building.getBuildingDurationSeconds();
				
				long stunden = TimeUnit.SECONDS.toHours(available);
				long minuten = TimeUnit.SECONDS.toMinutes(available)-TimeUnit.HOURS.toMinutes(stunden);
				long sekunden = available - TimeUnit.HOURS.toSeconds(stunden) - TimeUnit.MINUTES.toSeconds(minuten);
				String buildingTimeOutput = String.format("Bauzeit n�chste Stufe: %02d:%02d:%02d", stunden, minuten, sekunden);
				System.out.println(buildingTimeOutput);
				System.out.println("Ben�tigte Bev�lkerung n�chste Stufe: " + building.getPopulationForNextLevel());
				if (building.getLevelUp() != null) {
					System.out.println("Ressourcen f�r Levelup verf�gbar!");
				} else {
					someDateFormat.applyPattern("dd.MM. hh:mm:ss");
					System.out.println("Ressourcen f�r Levelup am " + someDateFormat.format(building.getResourcesAvailable()) + " verf�gbar");
				}
			}
			
			
		}
	}
	
	public void farmAssistantTest() {
		Farmassistant fm = new Farmassistant(driver);
		fm.goToSite();

// M�gliche Beute der Templates
		System.out.println("---------------- FARMASSISTENT INFORMATIONEN ----------------");
		System.out.println("A-Template m�gliche Beute: " + fm.getAtemplateHaulCapacity());
		System.out.println("B-Template m�gliche Beute: " + fm.getBtemplateHaulCapacity());
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
// Momentane Truppen des A-Templates
		System.out.println("A-Template Truppen:");
		Map<String, Integer> aTemplateTroops = fm.getAtemplateTroops();
		Iterator<Entry<String, Integer>> iterator = aTemplateTroops.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey()) + ": " + pairs.getValue());
		}
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
// Momentane Truppen des B-Templates
		System.out.println("B-Template Truppen:");
		Map<String, Integer> bTemplateTroops = fm.getBtemplateTroops();
		iterator = bTemplateTroops.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey()) + ": " + pairs.getValue());
		}
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
// Momentan verf�gbare Truppen
		System.out.println("Momentan verf�gbare Truppen:");
		Map<String, Integer> availableTemplateTroops = fm.getAvailableTroops();
		iterator = availableTemplateTroops.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>) iterator.next();
			System.out.println(ResourceBundleUtil.getGeneralBundleString(pairs.getKey()) + ": " + pairs.getValue());
		}
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
// Alle Truppen zum Farmen verf�gbar machen
		System.out.println("Alle Truppen zum Farmen verf�gbar machen...");
		String[] availableTroops = new String[] {Troop.ARCHER_NAME, Troop.AXE_NAME, Troop.HEAVY_NAME, Troop.KNIGHT_NAME, Troop.LIGHT_NAME, Troop.MARCHER_NAME, Troop.SPEAR_NAME, Troop.SPY_NAME, Troop.SWORD_NAME};
		fm.setAvailableTroops(availableTroops);
		System.out.println("-------------------------------------------------------------");

		System.out.println("Show only... und Show reports... aktivieren");
// Show only... und Show reports... einschalten
		fm.setOnlyAttacksFromThisVillage(true);
		fm.setOnlyFullLosses(true);
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("Vorherige Seite, N�chste Seite, Vorherige Seite");
		fm.previousPage();
		fm.nextPage();
		fm.previousPage();
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("A-Template neu setzen");
		fm.setAtemplateTroops(0, 10, 4, 345, 123, 23, 725, 272, 1);
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("B-Template neu setzen");
		fm.setBtemplateTroops(2132, 10000, 124, 3345, 1213, 233, 7225, 2732, 4);
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("Nach Datum aufw�rts sortieren");
		fm.sortByDateAufwaerts();
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("Nach Datum abw�rts sortieren");
		fm.sortByDateAbwaerts();
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("Nach Distanz aufw�rts sortieren");
		fm.sortByDistanceAufwaerts();
		System.out.println("-------------------------------------------------------------");
		Helper.getInstance().sleep(3);
		System.out.println("Nach Distanz abw�rts sortieren");
		fm.sortByDistanceAbwaerts();
	}
}
