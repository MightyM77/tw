package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;

import tool.recruitment.RecruitmentBuilding;
import utile.Building;
import utile.Troop;
import config.Configuration;

public class MaxRecruitment extends Procedure {

	private final Building building;
	private final Troop troop;
	private final RecruitmentBuilding rb;
	
	
	public MaxRecruitment(Calendar pActivationTime, Building pBuilding, Troop pTroop) {
		super(pActivationTime);
		this.building = pBuilding;
		this.troop = pTroop;
		this.rb = new RecruitmentBuilding("/game.php", getBuilding().getId(), driver());
	}
	
	private Building getBuilding() {
		return this.building;
	}
	
	private Troop getTroop() {
		return this.troop;
	}

	@Override
	public List<Procedure> doAction() {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		rb.goToSite();
		Calendar recruitmentTime = Calendar.getInstance(Configuration.LOCALE);
		
		if (rb.isMaxLinkBtnEnabled(getTroop())) {
			int maxRecruitmentSeconds = rb.getMaxRecruit(getTroop()) * rb.getRecruitmentSeconds(getTroop());
			recruitmentTime.add(Calendar.SECOND, maxRecruitmentSeconds);
			recruitmentTime.add(Calendar.MINUTE, 1);
			procedures.add(new MaxRecruitment(recruitmentTime, getBuilding(), getTroop()));
			rb.recruitMax(getTroop());
		} else {
			recruitmentTime.add(Calendar.MINUTE, 10);
			procedures.add(new MaxRecruitment(recruitmentTime, getBuilding(), getTroop()));
		}
		
		// TODO Auto-generated method stub
		return procedures;
	}

}
