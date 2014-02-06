package main;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import tool.Tribalwars;
import tool.farmassistant.FarmTemplate;
import utile.Building;
import utile.ReportStatus;
import utile.Troop;
import config.Configuration;

public class Ablauf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WebDriver driver = Configuration.DRIVER;
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Tribalwars tw = Tribalwars.getInstance();
		tw.goToSite();
		tw.login();

		Calendar time1 = Calendar.getInstance();
		Calendar time2 = Calendar.getInstance();
		Calendar time3 = Calendar.getInstance();
		Calendar time4 = Calendar.getInstance();
		time1.add(Calendar.MINUTE, 15);
		time2.add(Calendar.MINUTE, 30);
		time3.add(Calendar.MINUTE, 45);
		time4.add(Calendar.MINUTE, 60);
		
		FarmTemplate[] farmTemplatesToClick = new FarmTemplate[] {FarmTemplate.A, FarmTemplate.B};
		ReportStatus[] onlyThoseReportStatus = new ReportStatus[] {ReportStatus.NO_LOSSES, ReportStatus.SCOUT_ATTACK};
		
		List<Procedure> procedures = new ArrayList<Procedure>();
		procedures.add(new FarmassistantABFarming(Calendar.getInstance(Configuration.LOCALE), onlyThoseReportStatus, farmTemplatesToClick));
		procedures.add(new FarmassistantABFarming(time1, onlyThoseReportStatus, farmTemplatesToClick));
		procedures.add(new FarmassistantABFarming(time2, onlyThoseReportStatus, farmTemplatesToClick));
		procedures.add(new FarmassistantABFarming(time3, onlyThoseReportStatus, farmTemplatesToClick));
		procedures.add(new FarmassistantABFarming(time4, onlyThoseReportStatus, farmTemplatesToClick));
		procedures.add(new MaxRecruitment(Calendar.getInstance(Configuration.LOCALE), Building.STABLE, Troop.LIGHT));
		
//		Map<Troop, Integer> troopsAmount = new HashMap<Troop, Integer>();
//		troopsAmount.put(Troop.LIGHT, 1);
//		Point coords = new Point(493,498);
//		procedures.add(new KeepAttackingVillage(driver, Calendar.getInstance(), troopsAmount, coords));
		
		try {
			while (true) {
				for (int i = 0; i < procedures.size(); i++) {
					if (procedures.get(i).getActivationTime().compareTo(Calendar.getInstance(Configuration.LOCALE)) <= 0) {
						procedures.addAll(procedures.get(i).doAction());
						procedures.remove(i);
						i--;
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
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
