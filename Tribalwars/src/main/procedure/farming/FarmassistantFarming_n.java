package main.procedure.farming;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.procedure.Procedure;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmEntry;
import tool.farmassistant.Farmassistant;
import config.TwConfiguration;

public class FarmassistantFarming_n extends Procedure {
	public static int gefarmteDoerfer = 0;

	// Private constants
	private final static int BASE_TIMEOUT_AFTER_FARMBTN_CLICK = 50;
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 50;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int MAX_DURCHGAENGE = 6;

	private final Farmassistant fa;
	private final FarmEntryValidator[] farmEntryValidators;
	private final int[] villageIds;
	private final FarmButton[] usedFarmbuttons;
	private final List<FarmButton> notEnoughTroops = new ArrayList<FarmButton>();

	public FarmassistantFarming_n(Calendar pActivationTime, TwConfiguration pConfig, Farmassistant pFarmassistant, FarmEntryValidator[] pFarmEntryValidators,
			int... pVillageIds) {
		super(pConfig, pActivationTime.getTimeInMillis());
		this.fa = pFarmassistant;
		this.farmEntryValidators = pFarmEntryValidators;
		this.villageIds = pVillageIds;
		this.usedFarmbuttons = getUsedFarmButtons(farmEntryValidators);
	}

	private static FarmButton[] getUsedFarmButtons(FarmEntryValidator... pFarmEntryValidators) {
		List<FarmButton> farmButtons = new ArrayList<FarmButton>();
		for (FarmEntryValidator farmEntryValidator : pFarmEntryValidators) {
			if (!farmButtons.contains(farmEntryValidator.getButtonToClick())) {
				farmButtons.add(farmEntryValidator.getButtonToClick());
			}
		}
		FarmButton[] finalFarmButtons = new FarmButton[farmButtons.size()];
		return farmButtons.toArray(finalFarmButtons);
	}

	private boolean enoughTroops() {
		return notEnoughTroops.size() < usedFarmbuttons.length;
	}

	private void updateNotEnoughTroopsList() {
		for (FarmButton farmButton : usedFarmbuttons) {
			if (!notEnoughTroops.contains(farmButton) && !fa.enoughTroops(farmButton)) {
				TwConfiguration.LOGGER.info("Für den {}-Button sind nicht mehr genügend Truppen vorhanden", farmButton.toString());
				notEnoughTroops.add(farmButton);
			}
		}
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		if (farmEntryValidators.length == 0) {
			TwConfiguration.LOGGER.warn("Breche Farmassistent Durchlauf ab, da keine Validationen/FarmButtons die geklickt werden sollen mitgegeben wurden");
			return triggeringProcedures;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		triggeringProcedures.add(new FarmassistantFarming_n(cal, getTwConfig(), fa, farmEntryValidators, villageIds));

		for (int villageId : villageIds) {
			startFarmDurchgang(villageId);
			notEnoughTroops.clear();
			TwConfiguration.LOGGER.info("Farmdurchgang mit Dorf-ID {} fertig", villageId);
			TwConfiguration.LOGGER.info("Gefarmte Dörfer total (seit Programmstart): {}", FarmassistantFarming_n.gefarmteDoerfer);
		}

		return triggeringProcedures;
	}

	private int startFarmDurchgang(int villageId) {
		TwConfiguration.LOGGER.info("Starte Farmassistent Durchlauf mit Dorf-Id {}", villageId);
		fa.goToSite(villageId);
		fa.goToPage(1);

		updateNotEnoughTroopsList();
		List<FarmEntryValidator> currentFarmEntryValidator = new ArrayList<FarmEntryValidator>();
		for (FarmEntryValidator fev : farmEntryValidators) {
			currentFarmEntryValidator.add(fev);
		}

		int farmDurchgaenge = 1;
		int farmedVillagesInThisDurchgang = 0;
		if (enoughTroops()) {
			while (farmDurchgaenge <= MAX_DURCHGAENGE) {
				int farmEntriesCount = fa.getFarmEntriesCount();
				for (int i = 0; i < farmEntriesCount; i++) {
					FarmEntry fe = fa.getFarmEntry(i);
					Iterator<FarmEntryValidator> fevIter = currentFarmEntryValidator.iterator();
					while (fevIter.hasNext()) {
						FarmEntryValidator fev = fevIter.next();
						FarmButton farmButtonToClick = fev.getButtonToClick();
						if (!notEnoughTroops.contains(farmButtonToClick)) {
							if (fev.isValid(fe, farmDurchgaenge)) {
								farmedVillagesInThisDurchgang++;
								fe.clickFarmButton(farmButtonToClick);
								TwConfiguration.LOGGER
										.info("Barbarendorf mit {}-Button gefarmt: ({}|{})", farmButtonToClick.toString(), (int) fe.getOrFindDestCoords().getX(), (int) fe.getOrFindDestCoords().getY());
								FarmassistantFarming_n.gefarmteDoerfer++;
								updateNotEnoughTroopsList();
								if (!enoughTroops()) {
									TwConfiguration.LOGGER.info("Breche Farmassistent Durchgang beim {}. von {}. Farmeinträgen ab, da keine der angegebenen Farmbuttons mehr enabled ist", i,
											farmEntriesCount);
									return farmDurchgaenge;
								}
								// try {
								// Thread.sleep(BASE_TIMEOUT_AFTER_FARMBTN_CLICK
								// +
								// RANDOM_GENERATOR.nextInt(RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK));
								// } catch (InterruptedException e) {
								// e.printStackTrace();
								// }
								break;

							}
						} else {
							fevIter.remove();
						}
					}
				}
				if (fa.hasNextPage()) {
					TwConfiguration.LOGGER.info("Gehe zur nächsten Farmassistent Seite");
					fa.nextPage();
				} else if (farmedVillagesInThisDurchgang > 0) {
					TwConfiguration.LOGGER.info("Gehe zur ersten Farmassistent Seite");
					fa.goToPage(1);
					farmDurchgaenge++;
					farmedVillagesInThisDurchgang = 0;
				} else {
					TwConfiguration.LOGGER.info("Farmassistent Durchlauf beendet, da in diesem Durchgang kein Farmbutton gedrückt wurde");
				}
			}
		}

		return farmDurchgaenge;
	}
}