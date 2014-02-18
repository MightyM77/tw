package main.procedure.farming;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.procedure.Procedure;
import config.Configuration;
import tool.farmassistant.FarmEntry;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.ReportStatus;
import utile.Troop;
import utile.TroopTemplate;

public class FarmassistantFarming extends Procedure {

	private static Map<Point, Integer> cachedAttacks = new HashMap<Point, Integer>();
	public static int gefarmteDoerfer = 0;

	private final Farmassistant fa;
	private final ReportStatus[] onlyThoseReportStatus;
	private final FarmTemplate[] onlyThoseFarmTemplates;

	private final static int BASE_TIMEOUT_AFTER_FARMBTN_CLICK = 100;
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 400;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK = 30;

	public FarmassistantFarming(Calendar pActivationTime, ReportStatus[] pOnlyThoseReportStatus, FarmTemplate... pFarmTemplates) {
		super(pActivationTime);
		this.fa = Farmassistant.getInstance();
		this.onlyThoseReportStatus = pOnlyThoseReportStatus;
		this.onlyThoseFarmTemplates = pFarmTemplates;
	}

	private void incrementCachedAttacks(Point targetCoords) {
		if (cachedAttacks.containsKey(targetCoords)) {
			cachedAttacks.put(targetCoords, cachedAttacks.get(targetCoords) + 1);
		} else {
			cachedAttacks.put(targetCoords, 1);
		}
	}
	
	private int getCachedAttacks(Point targetCoords) {
		if (cachedAttacks.containsKey(targetCoords)) {
			return cachedAttacks.get(targetCoords);
		} else {
			return 0;
		}
	}
	
	private ReportStatus[] getOnlyThoseReportStatus() {
		return this.onlyThoseReportStatus;
	}

	private FarmTemplate[] getOnlyThoseFarmTemplates() {
		return this.onlyThoseFarmTemplates;
	}

	private boolean enoughTroops(TroopTemplate bTemplateTroops, TroopTemplate availableTroops) {
		Map<Troop, Integer> availableTroopsMap = availableTroops.getAllTroops();
		Map<Troop, Integer> bTemplateTroopsMap = bTemplateTroops.getAllTroops();
		for (Troop troopKey : bTemplateTroopsMap.keySet()) {
			if (availableTroopsMap.get(troopKey) < bTemplateTroopsMap.get(troopKey)) {
				return false;
			}
		}
		return true;
	}

	private FarmassistantFarming createFarmassistantABFarming(FarmEntry fe, TroopTemplate troops) {
		Troop slowestTroop = troops.getSlowestTroop();
		Calendar troopsBack = fe.getBackToVillageTime(slowestTroop);
		troopsBack.add(Calendar.SECOND, ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK);
		return new FarmassistantFarming(troopsBack, getOnlyThoseReportStatus(), getOnlyThoseFarmTemplates());
	}

	@Override
	public List<Procedure> doAction() {
		Configuration.LOGGER.info("Starte Farmassistent Durchlauf");

		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		fa.goToSite();
		fa.goToPage(1);

		TroopTemplate availableTroops;
		Map<FarmTemplate, TroopTemplate> templateTroops = new HashMap<FarmTemplate, TroopTemplate>();
		for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
			TroopTemplate tt = fa.getTemplateTroops(farmTemplate);
			//tt.addTroopAmount(Troop.LIGHT, -10);
			templateTroops.put(farmTemplate, tt);
//			templateTroops.get(farmTemplate).addTroopAmount(Troop.LIGHT, -10);
		}

		boolean enoughTroops = true;
		boolean firstRun = true;
		
		while (enoughTroops) {
			int farmEntriesCount = fa.getFarmEntriesCount();
			for (int i = 0; i < farmEntriesCount; i++) {
				FarmEntry fe = fa.getFarmEntry(i);
				availableTroops = fa.getAvailableTroops();
				enoughTroops = false;
				boolean validFarmEntry = Arrays.asList(getOnlyThoseReportStatus()).contains(fe.getFarmStatus()) && (!fe.isGettingAttacked() || !firstRun);
				boolean validWall = fe.getWallLevel() < 5;
				
				for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
					if (enoughTroops(templateTroops.get(farmTemplate), availableTroops)) {
						enoughTroops = true;
						if (validFarmEntry && validWall && fe.isFarmButtonEnabled(farmTemplate) && enoughTroops(templateTroops.get(farmTemplate), availableTroops)) {
							fe.clickFarmButton(farmTemplate.convertToFarmButton());
//							FarmassistantFarming newFarmDurchgang = createFarmassistantABFarming(fe, templateTroops.get(farmTemplate));
							Configuration.LOGGER.info("Barbarendorf gefarmt: ({}|{}) Langsamste Einheit: {}", (int) fe.getCoord().getX(), (int) fe
									.getCoord().getY(), templateTroops.get(farmTemplate).getSlowestTroop().getId());
//							triggeringProcedures.add(newFarmDurchgang);
							FarmassistantFarming.gefarmteDoerfer++;
							try {
								Thread.sleep(BASE_TIMEOUT_AFTER_FARMBTN_CLICK + RANDOM_GENERATOR.nextInt(RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if (!enoughTroops) {
					Configuration.LOGGER.info("Breche beim " + i + ". von " + farmEntriesCount + ". Farmeintr�gen ab da nicht mehr gen�gend Truppen vorhanden sind");
					break;
				}
			}
			if (fa.hasNextPage() && enoughTroops) {
				Configuration.LOGGER.info("Gehe zur n�chsten Farmassistent Seite");
				fa.nextPage();
			} else if (enoughTroops) {
				Configuration.LOGGER.info("Gehe zur ersten Farmassistent Seite");
				fa.goToPage(1);
				firstRun = false;
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		triggeringProcedures.add(new FarmassistantFarming(cal, onlyThoseReportStatus, onlyThoseFarmTemplates));
		Configuration.LOGGER.info("Gefarmte D�rfer in diesem Durchgang: {}", triggeringProcedures.size());
		Configuration.LOGGER.info("Gefarmte D�rfer total: {}", FarmassistantFarming.gefarmteDoerfer);
		
		return triggeringProcedures;
	}
}