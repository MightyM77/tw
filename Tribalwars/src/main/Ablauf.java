package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import tool.Tribalwars;
import tool.farmassistant.FarmEntry;
import tool.farmassistant.Farmassistant;
import tool.recruitment.Barracks;
import utile.Building;
import utile.Troop;
import config.Configuration;

public class Ablauf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriver driver = Configuration.DRIVER;
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Tribalwars tw = Tribalwars.getInstance();
		tw.goToSite();
		tw.login();

		Calendar time1 = Calendar.getInstance();
		Calendar time2 = Calendar.getInstance();
		Calendar time3 = Calendar.getInstance();
		Calendar time4 = Calendar.getInstance();
		
		time1.add(Calendar.MINUTE, 15);
		time2.add(Calendar.MINUTE, 30);
		time3.add(Calendar.MINUTE, 45);
		time4.add(Calendar.MINUTE, 60);
		
		List<Procedure> procedures = new ArrayList<Procedure>();
		procedures.add(new FarmassistantABFarming(Calendar.getInstance(Configuration.LOCALE)));
		procedures.add(new FarmassistantABFarming(time1));
		procedures.add(new FarmassistantABFarming(time2));
		procedures.add(new FarmassistantABFarming(time3));
		procedures.add(new FarmassistantABFarming(time4));
		procedures.add(new MaxRecruitment(Calendar.getInstance(Configuration.LOCALE), Building.STABLE, Troop.LIGHT));
		
//		Map<Troop, Integer> troopsAmount = new HashMap<Troop, Integer>();
//		troopsAmount.put(Troop.LIGHT, 1);
//		Point coords = new Point(493,498);
//		procedures.add(new KeepAttackingVillage(driver, Calendar.getInstance(), troopsAmount, coords));
		
		while (true) {
			for (int i = 0; i < procedures.size(); i++) {
				if (procedures.get(i).getActivationTime().compareTo(Calendar.getInstance(Configuration.LOCALE)) <= 0) {
					procedures.addAll(procedures.get(i).doAction());
					procedures.remove(i);
					i--;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		Barracks barracks = new Barracks(driver);
//		Farmassistant fa = new Farmassistant(driver);
//
//		Map<Troop, Integer> aTemplateTroops;
//		Map<Troop, Integer> bTemplateTroops;
//		
//		Random randomGenerator = new Random();
//		int baseTimeout = 30 * 1000 * 60;
//		int timeout;
//		int counter = 1;
//
//		while (true) {
//			System.out.println("-------------------- Durchlauf " + counter + " --------------------");
//
//			System.out.println("Gehe zur Kaserne");
//			barracks.goToSite();
//			try {
//				System.out.println("Probiere maximale Äxte zu rekrutieren");
//				barracks.recruitMax(Troop.AXE);
//				System.out.println("Habe maximale Äxte rekrutiert");
//			} catch (AssertionError e) {
//				System.out.println("Konnte keine Äxte produzieren");
//				// Nichts machen, Truppe kann einfach nicht rekrutiert werden.
//			}
//
//			System.out.println("Gehe zum Farmassistent");
//			fa.goToSite();
//			aTemplateTroops = fa.getAtemplateTroops();
//			bTemplateTroops = fa.getBtemplateTroops();
//			Map<Troop, Integer> availableTroops;
//			System.out.println("Lese Farmeinträge aus");
//			FarmEntry[] fes = fa.getFarmEntries();
//
//			System.out.println("Iteriere durch Farmeinträge");
//			for (FarmEntry fe : fes) {
//				availableTroops = fa.getAvailableTroops();
//				if (fe.getbButton().enabled() && availableTroops.get(Troop.SPEAR) >= bTemplateTroops.get(Troop.SPEAR) && availableTroops.get(Troop.SWORD) >= bTemplateTroops.get(Troop.SWORD)
//						&& availableTroops.get(Troop.AXE) >= bTemplateTroops.get(Troop.AXE.getId()) && availableTroops.get(Troop.PALADIN) >= bTemplateTroops.get(Troop.PALADIN)) {
//					System.out.println("Klicke B-Button");
//					fe.getbButton().click();
//				} else if (fe.getaButton().enabled() && availableTroops.get(Troop.SPEAR) >= aTemplateTroops.get(Troop.SPEAR.getId()) && availableTroops.get(Troop.SWORD.getId()) >= aTemplateTroops.get(Troop.SWORD.getId())
//						&& availableTroops.get(Troop.AXE) >= aTemplateTroops.get(Troop.AXE.getId()) && availableTroops.get(Troop.PALADIN.getId()) >= aTemplateTroops.get(Troop.PALADIN.getId())) {
//					System.out.println("Klicke A-Button");
//					fe.getaButton().click();
//				} else {
//					System.out.println("Stoppe farmen da keine Truppen mehr vorhanden sind");
//					break;
//				}
//
//				try {
//					Thread.sleep(500 + randomGenerator.nextInt(1500));
//				} catch (InterruptedException e) {
//					System.out.println("Sleep failure (beim farmen)");
//					e.printStackTrace();
//				}
//			}
//			counter++;
//
//			try {
//				timeout = baseTimeout + 1000 * 60 * randomGenerator.nextInt(10);
//				System.out.println("Sleeping for " + timeout / 1000 / 60 + " minutes");
//				Thread.sleep(timeout);
//			} catch (InterruptedException e) {
//				System.out.println("Sleep failure");
//				e.printStackTrace();
//			}
//		}
		// Tester tester = new Tester(driver);
		// tester.headquartersTest();
	}
}
