package main.procedure.farming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;

import main.procedure.Procedure;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmEntry;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.TroopTemplate;

import com.google.common.collect.ImmutableList;

import config.TwConfiguration;

public class FarmassistantFarming_n2 extends Procedure {
	public static int gefarmteDoerfer = 0;

	// Private constants
	private final static int BASE_TIMEOUT_AFTER_FARMBTN_CLICK = 50;
	private final static int RANDOM_RANGE_TIMOUT_AFTER_FARMBTN_CLICK = 50;
	private final static Random RANDOM_GENERATOR = new Random();
	private final static int MAX_DURCHGAENGE = 6;

	private final Farmassistant fa;
	private final ImmutableList<FarmEntryValidator> farmEntryValidators;
	private final ImmutableList<Integer> villageIds;
	private final ImmutableList<FarmButton> usedFarmbuttons;
	private final List<FarmButton> notEnoughTroops = new ArrayList<FarmButton>();
	private final Builder builderOfThisInstance;
	
	public static class Builder {
		// Mandatory fields
		private final long activationTimeInMillis;
		private final TwConfiguration twConfig;
		private final Farmassistant fa;
		private final FarmEntryValidator fev;
		private final Integer villageId;
		
		// Optional fields
		private final List<FarmEntryValidator> additionalFarmEntryValidators = new ArrayList<FarmEntryValidator>();
		private final List<Integer> additionalVillageIds = new ArrayList<Integer>();
		
		public Builder(Calendar pActivationTimeInMillis, TwConfiguration pTwConfig, Farmassistant pFarmassistant, FarmEntryValidator pFarmEntryValidator, Integer pVillageId) {
			this.activationTimeInMillis = pActivationTimeInMillis.getTimeInMillis();
			this.twConfig = pTwConfig;
			this.fa = pFarmassistant;
			this.fev = pFarmEntryValidator;
			this.villageId = pVillageId;
		}
		
		public Builder addFarmEntryValidators(FarmEntryValidator... farmEntryValidators) {
			additionalFarmEntryValidators.addAll(Arrays.asList(farmEntryValidators));
			return (this);
		}
		
		public Builder addFarmEntryValidators(List<FarmEntryValidator> farmEntryValidators) {
			additionalFarmEntryValidators.addAll(farmEntryValidators);
			return (this);
		}
		
		public Builder addVillageIds(Integer... villageIds) {
			additionalVillageIds.addAll(Arrays.asList(villageIds));
			return (this);
		}
		
		public Builder addVillageIds(List<Integer> villageIds) {
			additionalVillageIds.addAll(villageIds);
			return (this);
		}
		
		public Builder setActivationTimeInMillis(Calendar pActivationTimeInMillis) {
			return new Builder(pActivationTimeInMillis, this.twConfig, this.fa, this.fev, this.villageId).addFarmEntryValidators(this.additionalFarmEntryValidators).addVillageIds(additionalVillageIds);
		}
		
		public FarmassistantFarming_n2 build() {
			return new FarmassistantFarming_n2(this);
		}
	}
	
	public FarmassistantFarming_n2(Builder builder) {
		super(builder.twConfig, builder.activationTimeInMillis);
		this.builderOfThisInstance = builder;
		this.fa = builder.fa;
		builder.additionalFarmEntryValidators.add(builder.fev);
		builder.additionalVillageIds.add(builder.villageId);
		this.farmEntryValidators = ImmutableList.copyOf(builder.additionalFarmEntryValidators);
		this.villageIds = ImmutableList.copyOf(builder.additionalVillageIds);
		this.usedFarmbuttons = getUsedFarmButtons(this.farmEntryValidators);
	}

	private static ImmutableList<FarmButton> getUsedFarmButtons(List<FarmEntryValidator> pFarmEntryValidators) {
		List<FarmButton> farmButtons = new ArrayList<FarmButton>();
		
		for (FarmEntryValidator farmEntryValidator : pFarmEntryValidators) {
			if (!farmButtons.contains(farmEntryValidator.getButtonToClick())) {
				farmButtons.add(farmEntryValidator.getButtonToClick());
			}
		}

		return ImmutableList.copyOf(farmButtons);
	}

	private boolean enoughTroops() {
		return notEnoughTroops.size() < usedFarmbuttons.size();
	}

	private void updateNotEnoughTroopsList(TroopTemplate availableTroops) {
		for (FarmButton farmButton : usedFarmbuttons) {
			if (!notEnoughTroops.contains(farmButton) && !fa.enoughTroops(availableTroops, farmButton)) {
				TwConfiguration.LOGGER.info("Für den {}-Button sind nicht mehr genügend Truppen vorhanden", farmButton.toString());
				notEnoughTroops.add(farmButton);
			}
		}
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> triggeringProcedures = new ArrayList<Procedure>();
		if (farmEntryValidators.size() == 0) {
			TwConfiguration.LOGGER.warn("Breche Farmassistent Durchlauf ab, da keine Validationen/FarmButtons die geklickt werden sollen mitgegeben wurden");
			return triggeringProcedures;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		triggeringProcedures.add(new FarmassistantFarming_n2(this.builderOfThisInstance.setActivationTimeInMillis(cal)));

		for (Integer villageId : this.villageIds) {
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

		// Make a mutable copy of the farmEntryValdators
		List<FarmEntryValidator> copyOfFarmEntryValidators = new ArrayList<FarmEntryValidator>();
		for (FarmEntryValidator fev : farmEntryValidators) {
			copyOfFarmEntryValidators.add(fev);
		}

		TroopTemplate availableTroops = fa.getAvailableTroops();
		Map<FarmTemplate, TroopTemplate> farmTemplatesTroops = new HashMap<FarmTemplate, TroopTemplate>();
		farmTemplatesTroops.put(FarmTemplate.A, fa.getTroopTemplate(FarmTemplate.A));
		farmTemplatesTroops.put(FarmTemplate.B, fa.getTroopTemplate(FarmTemplate.B));

		List<FarmButton> notEnoughTroops = new ArrayList<FarmButton>();
		for (FarmButton farmButton : this.usedFarmbuttons) {
			if (!fa.enoughTroops(farmButton)) {
				notEnoughTroops.add(farmButton);
			}
		}
		
		Map<FarmEntryValidator, Integer> farmValidatorCounter = new HashMap<FarmEntryValidator, Integer>();
		for (FarmEntryValidator farmEntryValidator : copyOfFarmEntryValidators) {
			farmValidatorCounter.put(farmEntryValidator, 0);
		}
		
		
		int farmDurchgaenge = 1;
		int farmedVillagesInThisDurchgang = 0;
		if (notEnoughTroops.size() < usedFarmbuttons.size()) {
			while (farmDurchgaenge <= MAX_DURCHGAENGE) {
				int farmEntriesCount = fa.getFarmEntriesCount();
				for (int i = 0; i < farmEntriesCount; i++) {
					FarmEntry fe = fa.getFarmEntry(i);
					Iterator<FarmEntryValidator> fevIter = copyOfFarmEntryValidators.iterator();
					while (fevIter.hasNext()) {
						FarmEntryValidator fev = fevIter.next();
						FarmButton farmButtonToClick = fev.getButtonToClick();
						if (!notEnoughTroops.contains(farmButtonToClick)) {
							if (fev.isValid(fe, farmDurchgaenge)) {
								farmedVillagesInThisDurchgang++;
								FarmassistantFarming_n.gefarmteDoerfer++;
								farmValidatorCounter.put(fev, farmValidatorCounter.get(fev)+1);
								fe.clickFarmButton(farmButtonToClick);
								TwConfiguration.LOGGER
										.info("Barbarendorf mit {}-Button gefarmt: ({}|{})", farmButtonToClick.toString(), (int) fe.getOrFindDestCoords().getX(), (int) fe.getOrFindDestCoords().getY());

								if (farmButtonToClick == FarmButton.C) {
									availableTroops = fa.getAvailableTroops();
								} else {
									availableTroops.subtractTroopTemplate(farmTemplatesTroops.get(FarmTemplate.valueOf(farmButtonToClick)));
								}
								
								for (FarmButton farmButton : this.usedFarmbuttons) {
									if (!notEnoughTroops.contains(farmButton)) {
										if (farmButton == FarmButton.C) {
											if (!fa.enoughTroops(availableTroops, farmButton)) {
												notEnoughTroops.add(farmButton);
												farmValidatorCounter.remove(farmButton);
											}
										} else {
											if (!farmTemplatesTroops.get(FarmTemplate.valueOf(farmButton)).lessOrEqualTroops(availableTroops)) {
												notEnoughTroops.add(farmButton);
												farmValidatorCounter.remove(farmButton);
											}
										}
									}
								}
								
								
								if (notEnoughTroops.size() == usedFarmbuttons.size()) {
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
					availableTroops = fa.getAvailableTroops();
				} else if (farmedVillagesInThisDurchgang > 0) {
					TwConfiguration.LOGGER.info("Gehe zur ersten Farmassistent Seite");
					fa.goToPage(1);
					availableTroops = fa.getAvailableTroops();
					farmedVillagesInThisDurchgang = 0;
					Iterator<Entry<FarmEntryValidator, Integer>> iterator = farmValidatorCounter.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<FarmEntryValidator, Integer> entry = iterator.next();
						// Wenn nichts zum entsprechenden Validator abgeschickt wurde, 
						// und der Validator nicht erst ab dem 2 Durchlauf valid ist oder schon der dritte Durchlauf startet
						// und der FarmButton noch nicht als "nicht genügend Truppen" identifiziert wurde 
						if (entry.getValue() == 0 && !entry.getKey().isValidOnSecondRund() || farmDurchgaenge >= 2 && !notEnoughTroops.contains(entry.getKey().getButtonToClick())) {
							notEnoughTroops.add(entry.getKey().getButtonToClick());
							iterator.remove();
						} else {
							entry.setValue(0);
						}
					}
					farmDurchgaenge++;
				} else {
					TwConfiguration.LOGGER.info("Farmassistent Durchlauf beendet, da in diesem Durchgang kein Farmbutton gedrückt wurde");
					break;
				}
			}
		}

		return farmDurchgaenge;
	}
}