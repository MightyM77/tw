package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import main.procedure.Procedure;
import main.procedure.farming.FarmEntryValidator;
import main.procedure.farming.FarmassistantFarming;
import main.procedure.farming.FarmassistantFarming_n;
import main.procedure.farming.FarmassistantFarming_n2;

import org.apache.commons.configuration.ConfigurationException;

import tool.Tribalwars;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.ReportStatus;
import config.TwConfiguration;

public class Launcher {

	public static void main(String[] args) {
		TwConfiguration.LOGGER.debug("STARTE PROGRAMM");

		String confFilePath = "antigibb.properties";
		TwConfiguration config = null;
		try {
			config = new TwConfiguration(confFilePath);
		} catch (ConfigurationException e) {
			TwConfiguration.LOGGER.error("Konfigurations Datei '" + confFilePath + "' konnte nicht gefunden werden");
			System.exit(0);
		}
		
		Farmassistant farmassistant = new Farmassistant(config);
		Tribalwars tribalwars = new Tribalwars(config);
		
		FarmTemplate[] farmTemplatesToClick = new FarmTemplate[] { FarmTemplate.B };
		ReportStatus[] onlyThoseReportStatus = new ReportStatus[] { ReportStatus.NO_LOSSES };
		FarmassistantFarming ff = new FarmassistantFarming(config, farmassistant, Calendar.getInstance(), onlyThoseReportStatus, farmTemplatesToClick);
		
		FarmEntryValidator farmEntryValidator1 = new FarmEntryValidator.Builder(farmassistant, FarmButton.C).lessDistanceThan(10.1).notGettingAttacked().name("C-Farmin < 10").build();
		FarmEntryValidator farmEntryValidator2 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES).biggerDistanceThan(10).notGettingAttacked().name("1-Durchlauf B-Farmin").build();
		FarmEntryValidator farmEntryValidator3 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES).biggerDistanceThan(10).validOnSecondRun().name("2-Durchlauf B-farmin").build();
		
		FarmEntryValidator[] farmEntryValidators = new FarmEntryValidator[] {
				farmEntryValidator1,
				farmEntryValidator2
		};
		
		FarmEntryValidator[] secondRunFarmEntryValidators = new FarmEntryValidator[] {
				farmEntryValidator3
		};
		
		FarmassistantFarming_n ff_n = new FarmassistantFarming_n(Calendar.getInstance(), config, farmassistant, farmEntryValidators, 726, 2502, 3640);
		
		FarmassistantFarming_n2 ff_n2 = new FarmassistantFarming_n2.Builder(Calendar.getInstance(), config, farmassistant, farmEntryValidator1, 726).addFarmEntryValidators(farmEntryValidator2, farmEntryValidator3).addVillageIds(634, 2502, 3640).build();
		List<Procedure> procedures = new ArrayList<Procedure>();
		procedures.add(ff_n2);
		
		Ablauf ablauf = new Ablauf(config, tribalwars, procedures);
		ablauf.start();
	}
}
