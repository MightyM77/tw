package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;

import tool.farmassistant.FarmEntry;
import tool.farmassistant.Farmassistant;
import utile.Troop;

public class FarmassistantABFarming extends Procedure {

	private final Farmassistant fa;
	private final static int BASE_TIMOUT_AFTER_FARMBTN_CLICK = 200;
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 700;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK = 5;

	public FarmassistantABFarming(WebDriver pDriver, Calendar pActivationTime) {
		super(pDriver, pActivationTime);
		this.fa = new Farmassistant(driver());
	}

	/**
	 * 
	 * @param troops
	 * @return die langsamste Einheit der gegebenen Einheiten von welchen mehr
	 *         als 0 vorhanden sind
	 */
	private Troop getSlowestTroop(Map<Troop, Integer> troops) {
		List<Troop> troopsToCompare = new ArrayList<Troop>();
		for (Troop troop : troops.keySet()) {
			if (troops.get(troop) > 0) {
				troopsToCompare.add(troop);
			}
		}
		Troop[] troopsToCompareArray = new Troop[troopsToCompare.size()];
		return Troop.getSlowestTroop(troopsToCompare.toArray(troopsToCompareArray));
	}

	private boolean enoughTroops(Map<Troop, Integer> bTemplateTroops, Map<Troop, Integer> availableTroops) {
		for (Troop troopKey : bTemplateTroops.keySet()) {
			if (availableTroops.get(troopKey) < bTemplateTroops.get(troopKey)) {
				return false;
			}
		}
		return true;
	}

	private FarmassistantABFarming createFarmassistantABFarming(FarmEntry fe, Map<Troop, Integer> troops) {
		Troop slowestTroop = getSlowestTroop(troops);
		Calendar troopsBack = fe.getBackToVillageTime(slowestTroop);
		troopsBack.add(Calendar.SECOND, ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK);
		return new FarmassistantABFarming(driver(), troopsBack);
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		System.out.println("Starte FarmassistantFarming-Prozedur");
		fa.goToSite();
		fa.goToPage(1);

		Map<Troop, Integer> aTemplateTroops = fa.getAtemplateTroops();
		Map<Troop, Integer> bTemplateTroops = fa.getBtemplateTroops();
		Map<Troop, Integer> availableTroops;

		boolean enoughTroops = true;
		boolean firstRun = true;
		System.out.println("Lese Farmeinträge aus");
		FarmEntry[] fes;

		System.out.println("Iteriere durch Farmeinträge");
		while (enoughTroops) {
			fes = fa.getFarmEntries();
			for (FarmEntry fe : fes) {
				availableTroops = fa.getAvailableTroops();
				// Wenn das BB entweder noch nicht angegriffen wird oder wenn es
				// der 2te durchlauf ist
				if (fe.getAttacks() == 0 || !firstRun) {
					if (fe.getbButton().enabled() && enoughTroops(bTemplateTroops, availableTroops)) {
						System.out.println("Klicke B-Button");
						triggeringProcedures.add(createFarmassistantABFarming(fe, bTemplateTroops));
						fe.getbButton().click();

					} else if (fe.getaButton().enabled() && enoughTroops(aTemplateTroops, availableTroops)) {
						System.out.println("Klicke A-Button");
						triggeringProcedures.add(createFarmassistantABFarming(fe, aTemplateTroops));
						fe.getaButton().click();
					} else {
						System.out.println("Stoppe farmen da keine Truppen mehr vorhanden sind");
						enoughTroops = false;
						break;
					}
					try {
						Thread.sleep(BASE_TIMOUT_AFTER_FARMBTN_CLICK + RANDOM_GENERATOR.nextInt(RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK));
					} catch (InterruptedException e) {
						System.out.println("Sleep failure (bei der Farmassitent Prozedur)");
						e.printStackTrace();
					}
				}
			}
			if (fa.hasNextPage()) {
				fa.nextPage();
			} else {
				fa.goToPage(1);
				firstRun = false;
			}
		}

		return triggeringProcedures;
	}
}