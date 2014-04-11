package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import main.procedure.KeepRecruitUnit;
import main.procedure.Procedure;
import main.procedure.attacking.AttackVillage;
import main.procedure.attacking.IncomingRenamer;
import main.procedure.farming.FarmEntryValidator;
import main.procedure.farming.FarmassistantFarming;
import main.procedure.farming.FarmassistantFarming_n;
import main.procedure.farming.FarmassistantFarming_n2;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import tool.Place;
import tool.Tribalwars;
import tool.farmassistant.FarmButton;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import tool.overview_villages.incoming.IncomingAttack;
import tool.recruitment.Barracks;
import tool.recruitment.RecruitmentBuilding;
import tool.recruitment.Stable;
import utile.Building;
import utile.ReportStatus;
import utile.Troop;
import config.TwConfiguration;

public class Launcher {

	public static void main(String[] args) {
		TwConfiguration.LOGGER.debug("STARTE PROGRAMM");

		
		System.setProperty("webdriver.chrome.driver", "Z:\\chromedriver.exe");
		WebDriver chromeDriver = new ChromeDriver();
		
		String confFilePath = "antigibb.properties";
		TwConfiguration config = null;
		try {
			config = new TwConfiguration(chromeDriver, confFilePath);
		} catch (ConfigurationException e) {
			TwConfiguration.LOGGER.error("Konfigurations Datei '" + confFilePath + "' konnte nicht gefunden werden");
			System.exit(0);
		}
		
		Farmassistant farmassistant = new Farmassistant(config);
		Place place = new Place(config);
		IncomingAttack incomingAttack = new IncomingAttack(config);
		Tribalwars tribalwars = new Tribalwars(config);
		
		
		FarmEntryValidator farmEntryValidator1 = new FarmEntryValidator.Builder(farmassistant, FarmButton.C).lessDistanceThan(20.1).notGettingAttacked().name("C-Farmin < 20.1").build();
		FarmEntryValidator farmEntryValidator2 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES, ReportStatus.SOME_LOSSES).biggerDistanceThan(20).notGettingAttacked().name("1-Durchlauf B-Farmin").build();
		FarmEntryValidator farmEntryValidator3 = new FarmEntryValidator.Builder(farmassistant, FarmButton.B).onlyThoseReportStatus(ReportStatus.NO_LOSSES, ReportStatus.SOME_LOSSES).biggerDistanceThan(20).validOnSecondRun().name("3-Durchlauf B-farmin").build();
		FarmEntryValidator farmEntryValidator4 = new FarmEntryValidator.Builder(farmassistant, FarmButton.A).onlyThoseReportStatus(ReportStatus.NO_LOSSES, ReportStatus.SOME_LOSSES).lessDistanceThan(20.1).notGettingAttacked().name("2-Durchlauf A-farmin").build();
		
				
//		Stable barrack = new Stable(config);
//		KeepRecruitUnit keepRecruitLcs = new KeepRecruitUnit(config, barrack, Calendar.getInstance(), Building.STABLE, Troop.LIGHT);
		
		
		
		// 2539, 2520, 855, 726, 3451, 3871, 1926, 1489, 930, 2502, 3640
		
		// 2502, 726, 3640, 634
		FarmassistantFarming_n2 ff_n2 = new FarmassistantFarming_n2.Builder(Calendar.getInstance(), config, farmassistant, farmEntryValidator1, 2539).addFarmEntryValidators(farmEntryValidator4, farmEntryValidator2, farmEntryValidator3).addVillageIds(2520, 855, 726, 3451, 3871, 1926, 1489, 930, 2502, 3640, 725).build();
		
		
		
		
		
		
		Calendar activationTime = Calendar.getInstance();
		activationTime.set(Calendar.MINUTE, 46);
		AttackVillage rausstellen = new AttackVillage(config, place, activationTime, 930, new Point(534,525));
		
		Calendar activationTime1 = Calendar.getInstance();
		activationTime1.set(Calendar.HOUR, 19);
		activationTime1.set(Calendar.MINUTE, 50);
		AttackVillage rausstellen1 = new AttackVillage(config, place, activationTime, 3871, new Point(559,516));
		
		Calendar activationTime2 = Calendar.getInstance();
		activationTime2.set(Calendar.HOUR, 22);
		activationTime2.set(Calendar.MINUTE, 20);
		AttackVillage rausstellen2 = new AttackVillage(config, place, activationTime, 3871, new Point(559,516));
		 
		Calendar activationTime3 = Calendar.getInstance();
		activationTime3.set(Calendar.DATE, 11);
		activationTime3.set(Calendar.HOUR, 0);
		activationTime3.set(Calendar.MINUTE, 43);
		AttackVillage rausstellen3 = new AttackVillage(config, place, activationTime, 3871, new Point(559,516));

		Calendar activationTime19 = Calendar.getInstance();
		activationTime19.set(Calendar.DATE, 11);
		activationTime19.set(Calendar.HOUR, 1);
		activationTime19.set(Calendar.MINUTE, 40);
		AttackVillage rausstellen19 = new AttackVillage(config, place, activationTime, 3871, new Point(559,516));
		
		Calendar activationTime4 = Calendar.getInstance();
		activationTime4.set(Calendar.HOUR, 19);
		activationTime4.set(Calendar.MINUTE, 52);
		AttackVillage rausstellen4 = new AttackVillage(config, place, activationTime, 930, new Point(534,525));
		
		Calendar activationTime5 = Calendar.getInstance();
		activationTime5.set(Calendar.DATE, 11);
		activationTime5.set(Calendar.HOUR, 3);
		activationTime5.set(Calendar.MINUTE, 10);
		AttackVillage rausstellen5 = new AttackVillage(config, place, activationTime, 930, new Point(534,525));
		
		Calendar activationTime6 = Calendar.getInstance();
		activationTime6.set(Calendar.HOUR, 20);
		activationTime6.set(Calendar.MINUTE, 6);
		AttackVillage rausstellen6 = new AttackVillage(config, place, activationTime, 3052, new Point(554,507));

		Calendar activationTime7 = Calendar.getInstance();
		activationTime7.set(Calendar.HOUR, 23);
		activationTime7.set(Calendar.MINUTE, 55);
		AttackVillage rausstellen7 = new AttackVillage(config, place, activationTime, 3052, new Point(554,507));
		
		Calendar activationTime8 = Calendar.getInstance();
		activationTime8.set(Calendar.HOUR, 20);
		activationTime8.set(Calendar.MINUTE, 22);
		AttackVillage rausstellen8 = new AttackVillage(config, place, activationTime, 855, new Point(539,518));
		
		Calendar activationTime9 = Calendar.getInstance();
		activationTime9.set(Calendar.DATE, 11);
		activationTime9.set(Calendar.HOUR, 0);
		activationTime9.set(Calendar.MINUTE, 50);
		AttackVillage rausstellen9 = new AttackVillage(config, place, activationTime, 855, new Point(539,518));

		Calendar activationTime10 = Calendar.getInstance();
		activationTime10.set(Calendar.HOUR, 20);
		activationTime10.set(Calendar.MINUTE, 25);
		AttackVillage rausstellen10 = new AttackVillage(config, place, activationTime, 778, new Point(539,518));

		Calendar activationTime11 = Calendar.getInstance();
		activationTime11.set(Calendar.DATE, 11);
		activationTime11.set(Calendar.HOUR, 0);
		activationTime11.set(Calendar.MINUTE, 10);
		AttackVillage rausstellen11 = new AttackVillage(config, place, activationTime, 778, new Point(539,518));
		
		Calendar activationTime12 = Calendar.getInstance();
		activationTime12.set(Calendar.HOUR, 20);
		activationTime12.set(Calendar.MINUTE, 50);
		AttackVillage rausstellen12 = new AttackVillage(config, place, activationTime, 1489, new Point(540,506));

		Calendar activationTime13 = Calendar.getInstance();
		activationTime13.set(Calendar.DATE, 11);
		activationTime13.set(Calendar.HOUR, 1);
		activationTime13.set(Calendar.MINUTE, 13);
		AttackVillage rausstellen13 = new AttackVillage(config, place, activationTime, 1489, new Point(540,506));
		
		Calendar activationTime14 = Calendar.getInstance();
		activationTime14.set(Calendar.HOUR, 20);
		activationTime14.set(Calendar.MINUTE, 55);
		AttackVillage rausstellen14 = new AttackVillage(config, place, activationTime, 2502, new Point(550,508));
		
		Calendar activationTime15 = Calendar.getInstance();
		activationTime15.set(Calendar.HOUR, 21);
		activationTime15.set(Calendar.MINUTE, 10);
		AttackVillage rausstellen15 = new AttackVillage(config, place, activationTime, 2734, new Point(551,505));
		
		Calendar activationTime16 = Calendar.getInstance();
		activationTime16.set(Calendar.DATE, 11);
		activationTime16.set(Calendar.HOUR, 0);
		activationTime16.set(Calendar.MINUTE, 15);
		AttackVillage rausstellen16 = new AttackVillage(config, place, activationTime, 2734, new Point(551,505));

		Calendar activationTime17 = Calendar.getInstance();
		activationTime17.set(Calendar.HOUR, 23);
		activationTime17.set(Calendar.MINUTE, 55);
		AttackVillage rausstellen17 = new AttackVillage(config, place, activationTime, 3640, new Point(559,509));
		
		
		
		
		
		
		
		
		
		
		
		
		
		IncomingRenamer renamer = new IncomingRenamer(config, incomingAttack, Calendar.getInstance());
		
		List<Procedure> procedures = new ArrayList<Procedure>();
		procedures.add(rausstellen2);
		procedures.add(rausstellen3);
		procedures.add(rausstellen5);
		procedures.add(rausstellen6);
		procedures.add(rausstellen7);
		procedures.add(rausstellen8);
		procedures.add(rausstellen9);
		procedures.add(rausstellen10);
		procedures.add(rausstellen11);
		procedures.add(rausstellen12);
		procedures.add(rausstellen13);
		procedures.add(rausstellen14);
		procedures.add(rausstellen15);
		procedures.add(rausstellen16);
		procedures.add(rausstellen17);
		procedures.add(rausstellen19);
		procedures.add(renamer);
		
		Ablauf ablauf = new Ablauf(config, tribalwars, procedures);
		ablauf.start();
	}
}
