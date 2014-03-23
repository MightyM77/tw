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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import tool.Tribalwars;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.ReportStatus;
import config.TwConfiguration;

public class Launcher {

	public static void main(String[] args) {
		TwConfiguration.LOGGER.debug("STARTE PROGRAMM");

		
//		System.setProperty("webdriver.chrome.driver", "Z:\\chromedriver.exe");
//		WebDriver chromeDriver = new ChromeDriver();
		
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
		
		FarmEntryValidator farmEntryValidator1 = new FarmEntryValidator.Builder(farmassistant, FarmButton.C).lessDistanceThan(20.1).notGettingAttacked().name("C-Farmin < 20.1").build();
		FarmEntryValidator farmEntryValidator2 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES).biggerDistanceThan(20).notGettingAttacked().name("1-Durchlauf B-Farmin").build();
		FarmEntryValidator farmEntryValidator3 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES).biggerDistanceThan(20).validOnSecondRun().name("3-Durchlauf B-farmin").build();
		FarmEntryValidator farmEntryValidator4 = new FarmEntryValidator.Builder(farmassistant, FarmButton.A).onlyThoseReportStatus(ReportStatus.NO_LOSSES).lessDistanceThan(20.1).notGettingAttacked().name("2-Durchlauf A-farmin").build();
		
		FarmEntryValidator[] farmEntryValidators = new FarmEntryValidator[] {
				farmEntryValidator1,
				farmEntryValidator2
		};
		
		FarmEntryValidator[] secondRunFarmEntryValidators = new FarmEntryValidator[] {
				farmEntryValidator3
		};
		
		FarmassistantFarming_n ff_n = new FarmassistantFarming_n(Calendar.getInstance(), config, farmassistant, farmEntryValidators, 726, 2502, 3640);
					
		// 726, 2502
		FarmassistantFarming_n2 ff_n2 = new FarmassistantFarming_n2.Builder(Calendar.getInstance(), config, farmassistant, farmEntryValidator1, 3640).addFarmEntryValidators(farmEntryValidator4, farmEntryValidator2, farmEntryValidator3).addVillageIds(3052, 778, 634, 725, 2734, 1938, 1489).build();
		List<Procedure> procedures = new ArrayList<Procedure>();
		procedures.add(ff_n2);
		
		Ablauf ablauf = new Ablauf(config, tribalwars, procedures);
		ablauf.start();
	}
}
