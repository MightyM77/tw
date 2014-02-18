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

import main.procedure.FarmassistantFarming;
import main.procedure.IncomingAttacksWatcher;
import main.procedure.KeepAttackingVillage;
import main.procedure.KeepRecruitUnit;
import main.procedure.Procedure;

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

public class Ablauf {
	
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
//		TroopTemplate template1 = new TroopTemplate();
//		template1.addTroopAmount(Troop.LIGHT, 1);
//		procedures.add(new KeepAttackingVillage(Calendar.getInstance(), template1, new Point(484,496)));
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

	private static final Logger MEMORY_LOGGER = LogManager.getLogger(Ablauf.class);
	private List<Procedure> procedures = new ArrayList<Procedure>();

	public Ablauf(List<Procedure> procedures) {
		this.procedures = procedures;
	}

	public Ablauf(Procedure... procedures) {
		this(Arrays.asList(procedures));
	}

	public void start() {
		try {
			while (true) {
				executeReadyProcedure();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception | Error e) {
			Configuration.LOGGER.error("Programm ist gefailed, in der procedures Liste befanden sich {} Objekte. Fehlermeldung: {}", procedures.size(), e.getMessage());
			
			if (Configuration.EMAIL_ON_EXCEPTION) {
				try {
					GoogleMail.Send(Configuration.SENDER_EMAIL, Configuration.SENDER_EMAIL_PW, Configuration.RECIPIENT_EMAIL, "FAILURE", e.getMessage());
				} catch (AddressException e2) {
					Configuration.LOGGER.error("E-Mail senden fehlgeschlagen, Fehlermeldung: {}", e2.getMessage());
				} catch (MessagingException e3) {
					Configuration.LOGGER.error("E-Mail senden fehlgeschlagen, Fehlermeldung: {}", e3.getMessage());
				}
			}
			
			
			
			if (Configuration.BING_ON_EXCEPTION) {
				while (true) {
					Toolkit.getDefaultToolkit().beep();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	public void executeReadyProcedure() throws ParseException {
		for (int i = 0; i < procedures.size(); i++) {
			if (procedures.get(i).getActivationTime().compareTo(Calendar.getInstance(Configuration.LOCALE)) <= 0) {
				procedures.addAll(procedures.get(i).doAction());
				procedures.remove(i);
				i--;
				MEMORY_LOGGER.info("Memory used: " + Runtime.getRuntime().totalMemory());
				MEMORY_LOGGER.info("Free Memory: " + Runtime.getRuntime().freeMemory());
			}
		}
	}
}
