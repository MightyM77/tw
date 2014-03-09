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
import config.TwConfiguration;
import tool.farmassistant.FarmButton;
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
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 200;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK = 30;

	public FarmassistantFarming(TwConfiguration pConfig, Farmassistant pFarmassistant, Calendar pActivationTimeInMillis, ReportStatus[] pOnlyThoseReportStatus, FarmTemplate... pFarmTemplates) {
		super(pConfig, pActivationTimeInMillis.getTimeInMillis());
		this.fa = pFarmassistant;
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

	private FarmassistantFarming createFarmassistantABFarming(FarmEntry fe, TroopTemplate troops) {
		Troop slowestTroop = troops.getSlowestTroop();
		Calendar troopsBack = slowestTroop.getBackTime(fa.getCurrentCoord(), fe.getOrFindDestCoords());
		troopsBack.add(Calendar.SECOND, ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK);
		return new FarmassistantFarming(getTwConfig(), fa, troopsBack, getOnlyThoseReportStatus(), getOnlyThoseFarmTemplates());
	}

	@Override
	public List<Procedure> doAction() {
		TwConfiguration.LOGGER.info("Starte Farmassistent Durchlauf");

		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		fa.goToSite(2502);
		fa.goToPage(1);

		TroopTemplate availableTroops;
		Map<FarmTemplate, TroopTemplate> templateTroops = new HashMap<FarmTemplate, TroopTemplate>();
		for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
			TroopTemplate tt = fa.getTroopTemplate(farmTemplate);
			templateTroops.put(farmTemplate, tt);
		}

		boolean enoughTroops = true;
		int firstRun = 1;
		
		while (enoughTroops) {
			int farmEntriesCount = fa.getFarmEntriesCount();
			for (int i = 0; i < farmEntriesCount; i++) {
				FarmEntry fe = fa.getFarmEntry(i);
				availableTroops = fa.getAvailableTroops();
				enoughTroops = false;
				boolean validFarmEntry = Arrays.asList(getOnlyThoseReportStatus()).contains(fe.getOrFindReportStatus()) && (!fe.isGettingAttacked() || firstRun > 1);
				boolean validWall = fe.getWallLevel() < 5;
				
				for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
					if (templateTroops.get(farmTemplate).lessOrEqualTroops(availableTroops)) {
						enoughTroops = true;
						if (validFarmEntry && validWall && fe.isFarmButtonEnabled(farmTemplate) && templateTroops.get(farmTemplate).lessOrEqualTroops(availableTroops)) {
							fe.clickFarmButton(FarmButton.valueOf(farmTemplate));
//							FarmassistantFarming newFarmDurchgang = createFarmassistantABFarming(fe, templateTroops.get(farmTemplate));
							TwConfiguration.LOGGER.info("Barbarendorf gefarmt: ({}|{}) Langsamste Einheit: {}", (int) fe.getOrFindDestCoords().getX(), (int) fe
									.getOrFindDestCoords().getY(), templateTroops.get(farmTemplate).getSlowestTroop().getId());
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
					TwConfiguration.LOGGER.info("Breche beim " + i + ". von " + farmEntriesCount + ". Farmeinträgen ab da nicht mehr genügend Truppen vorhanden sind");
					break;
				}
			}
			if (fa.hasNextPage() && enoughTroops) {
				TwConfiguration.LOGGER.info("Gehe zur nächsten Farmassistent Seite");
				fa.nextPage();
			} else if (enoughTroops) {
				TwConfiguration.LOGGER.info("Gehe zur ersten Farmassistent Seite");
				fa.goToPage(1);
				firstRun++;
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		triggeringProcedures.add(new FarmassistantFarming(getTwConfig(), fa, cal, onlyThoseReportStatus, onlyThoseFarmTemplates));
		TwConfiguration.LOGGER.info("Gefarmte Dörfer in diesem Durchgang: {}", triggeringProcedures.size());
		TwConfiguration.LOGGER.info("Gefarmte Dörfer total: {}", FarmassistantFarming.gefarmteDoerfer);
		
		return triggeringProcedures;
	}
}