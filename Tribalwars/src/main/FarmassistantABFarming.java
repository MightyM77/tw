package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tool.farmassistant.FarmEntry;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.ReportStatus;
import utile.Troop;
import utile.TroopTemplate;

public class FarmassistantABFarming extends Procedure {

	public static int farmDurchgaenge = 0;

	private final Farmassistant fa;
	private final ReportStatus[] onlyThoseReportStatus;
	private final FarmTemplate[] onlyThoseFarmTemplates;

	private final static int BASE_TIMOUT_AFTER_FARMBTN_CLICK = 100;
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 400;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK = 10;

	public FarmassistantABFarming(Calendar pActivationTime, ReportStatus[] pOnlyThoseReportStatus, FarmTemplate... pFarmTemplates) {
		super(pActivationTime);
		this.fa = Farmassistant.getInstance();
		this.onlyThoseReportStatus = pOnlyThoseReportStatus;
		this.onlyThoseFarmTemplates = pFarmTemplates;
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

	private FarmassistantABFarming createFarmassistantABFarming(FarmEntry fe, TroopTemplate troops) {
		Troop slowestTroop = troops.getSlowestTroop();
		Calendar troopsBack = fe.getBackToVillageTime(slowestTroop);
		troopsBack.add(Calendar.SECOND, ACTIVATION_DELAY_AFTER_TROOPS_ARE_BACK);
		return new FarmassistantABFarming(troopsBack, getOnlyThoseReportStatus(), getOnlyThoseFarmTemplates());
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		System.out.println("Starte FarmassistantFarming-Prozedur");
		fa.goToSite();
		fa.goToPage(1);

		TroopTemplate availableTroops;
		Map<FarmTemplate, TroopTemplate> templateTroops = new HashMap<FarmTemplate, TroopTemplate>();
		for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
			templateTroops.put(farmTemplate, fa.getTemplateTroops(farmTemplate));
		}

		boolean enoughTroops = true;
		boolean firstRun = true;
		System.out.println("Lese Farmeinträge aus");

		System.out.println("Iteriere durch Farmeinträge");
		while (enoughTroops) {
			for (int i = 0; i < fa.getFarmEntriesCount(); i++) {
				FarmEntry fe = fa.getFarmEntry(i);
				availableTroops = fa.getAvailableTroops();
				enoughTroops = false;
				if (Arrays.asList(getOnlyThoseReportStatus()).contains(fe.getFarmStatus()) && !fe.isGettingAttacked() || !firstRun) {
					for (FarmTemplate farmTemplate : getOnlyThoseFarmTemplates()) {
						if (fe.isFarmButtonEnabled(farmTemplate) && enoughTroops(templateTroops.get(farmTemplate), availableTroops)) {
							System.out.println("Klicke " + farmTemplate.name() + "-Button");
							fe.clickFarmButton(farmTemplate.convertToFarmButton());
							triggeringProcedures.add(createFarmassistantABFarming(fe, templateTroops.get(farmTemplate)));
							enoughTroops = true;
							FarmassistantABFarming.farmDurchgaenge++;
							try {
								Thread.sleep(BASE_TIMOUT_AFTER_FARMBTN_CLICK + RANDOM_GENERATOR.nextInt(RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					if (!enoughTroops) {
						break;
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
		System.out.println(Calendar.getInstance().getTime() + " Farmdurchgänge: " + FarmassistantABFarming.farmDurchgaenge);
		return triggeringProcedures;
	}
}