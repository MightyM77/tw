package main;

import java.awt.Toolkit;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import main.procedure.Procedure;
import main.procedure.farming.FarmassistantFarming;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tool.Tribalwars;
import tool.farmassistant.FarmTemplate;
import utile.GoogleMail;
import utile.ReportStatus;
import config.TwConfiguration;

public class Ablauf {
	private final TwConfiguration config;
	private final Tribalwars tribalwars;
	private static final Logger MEMORY_LOGGER = LogManager.getLogger(Ablauf.class);
	private final List<Procedure> procedures = new ArrayList<Procedure>();
	
	public Ablauf(TwConfiguration pConfig, Tribalwars pTribalwars, List<Procedure> pProcedures) {
		procedures.addAll(pProcedures);
		this.config = pConfig;
		this.tribalwars = pTribalwars;
	}

	private void login() {
		TwConfiguration.LOGGER.debug("Gehe auf Tribalwars Seite");
		tribalwars.goToSite();
		TwConfiguration.LOGGER.debug("Logge ein");
		tribalwars.login();
	}
	
	public void start() {
		login();
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
			TwConfiguration.LOGGER.error("Programm ist gefailed, in der procedures Liste befanden sich {} Objekte. Fehlermeldung: {}", procedures.size(), e.getMessage());
			
			if (config.isEmailOnException()) {
				try {
					for (String recipient : config.getRecipientEmails()) {
						GoogleMail.Send(config.getSenderEmail(), config.getSenderPw(), recipient, "FAILURE", e.getMessage());
					}
				} catch (AddressException e2) {
					TwConfiguration.LOGGER.error("E-Mail senden fehlgeschlagen, Fehlermeldung: {}", e2.getMessage());
				} catch (MessagingException e3) {
					TwConfiguration.LOGGER.error("E-Mail senden fehlgeschlagen, Fehlermeldung: {}", e3.getMessage());
				}
			}
			
			if (config.isBingOnException()) {
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
			if (procedures.get(i).getActivationTime().compareTo(Calendar.getInstance()) <= 0) {
				procedures.addAll(procedures.get(i).doAction());
				procedures.remove(i);
				i--;
				MEMORY_LOGGER.info("Memory used: " + Runtime.getRuntime().totalMemory());
				MEMORY_LOGGER.info("Free Memory: " + Runtime.getRuntime().freeMemory());
			}
		}
	}
}
