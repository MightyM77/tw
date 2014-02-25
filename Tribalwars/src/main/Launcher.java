package main;

import java.awt.Point;
import java.awt.Toolkit;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import main.procedure.IncomingAttacksWatcher;
import main.procedure.KeepRecruitUnit;
import main.procedure.Procedure;
import main.procedure.attacking.KeepAttackingVillage;
import main.procedure.farming.FarmassistantFarming;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import tool.Tribalwars;
import tool.farmassistant.FarmTemplate;
import tool.farmassistant.Farmassistant;
import utile.Building;
import utile.GoogleMail;
import utile.ReportStatus;
import utile.Troop;
import utile.TroopTemplate;
import config.Configuration;

public class Launcher {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration.LOGGER.debug("STARTE PROGRAMM");

		WebDriver driver = Configuration.DRIVER;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Tribalwars tw = Tribalwars.getInstance();
		tw.goToSite();
		Configuration.LOGGER.debug("Logge ein");
		tw.login();

		Calendar time1 = Calendar.getInstance();
		Calendar time2 = Calendar.getInstance();
		Calendar time3 = Calendar.getInstance();
		Calendar time4 = Calendar.getInstance();
		Calendar time5 = Calendar.getInstance();
		Calendar time6 = Calendar.getInstance();
		Calendar time7 = Calendar.getInstance();
		Calendar time8 = Calendar.getInstance();
		Calendar time9 = Calendar.getInstance();
		time1.add(Calendar.MINUTE, 15);
		time2.add(Calendar.MINUTE, 20);
		time3.add(Calendar.MINUTE, 35);
		time4.add(Calendar.MINUTE, 40);
		time5.add(Calendar.MINUTE, 55);
		time6.add(Calendar.MINUTE, 65);
		time7.add(Calendar.MINUTE, 75);
		time8.add(Calendar.MINUTE, 85);
		time9.add(Calendar.MINUTE, 95);
		
		FarmTemplate[] farmTemplatesToClick = new FarmTemplate[] { FarmTemplate.B };
		ReportStatus[] onlyThoseReportStatus = new ReportStatus[] { ReportStatus.NO_LOSSES };

		
		List<Procedure> procedures = new ArrayList<Procedure>();
//		procedures.add(new IncomingAttacksWatcher(Calendar.getInstance(Configuration.TIME_ZONE)));
		
		procedures.add(new FarmassistantFarming(Calendar.getInstance(Configuration.LOCALE), onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time1, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time2, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time3, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time4, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time5, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time6, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time7, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time8, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new FarmassistantFarming(time9, onlyThoseReportStatus, farmTemplatesToClick));
//		procedures.add(new KeepRecruitUnit(Calendar.getInstance(Configuration.LOCALE), Building.STABLE, Troop.LIGHT));
		
		
		
		// Map<Troop, Integer> troopsAmount = new HashMap<Troop, Integer>();
		// troopsAmount.put(Troop.LIGHT, 1);
		// Point coords = new Point(493,498);
		// procedures.add(new KeepAttackingVillage(driver,
		// Calendar.getInstance(), troopsAmount, coords));

		Ablauf ablauf = new Ablauf(procedures);
		ablauf.start();
	}
}
