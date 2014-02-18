package main.procedure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;

import tool.recruitment.RecruitmentBuilding;
import utile.Building;
import utile.Troop;
import config.Configuration;

public class KeepRecruitUnit extends Procedure {

	// Anzahl Minuten nach denen nochmal versucht werden soll, die Einheit zu
	// rekrutieren, falls Einheit momentan nicht rekrutiert werdenn kann
	private final static int TRY_RECRUIT_AGAIN = 10;

	private final Building building;
	private final Troop troop;
	private final RecruitmentBuilding rb;

	public KeepRecruitUnit(Calendar pActivationTime, Building pBuilding, Troop pTroop) {
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
		Configuration.LOGGER.debug("Starte Dauerhaft-Rekrutieren-Prozedur");
		List<Procedure> procedures = new ArrayList<Procedure>();

		rb.goToSite();
		Calendar recruitmentTime = Calendar.getInstance(Configuration.LOCALE);

		if (rb.isUnitAvailable(troop)) {
			if (rb.isMaxLinkBtnEnabled(getTroop())) {
				int maxRecruitmentSeconds = rb.getMaxRecruit(getTroop()) * rb.getRecruitmentSeconds(getTroop());
				recruitmentTime.add(Calendar.SECOND, maxRecruitmentSeconds);
				recruitmentTime.add(Calendar.MINUTE, 1);
				KeepRecruitUnit newKeepRecruitUnit = new KeepRecruitUnit(recruitmentTime, getBuilding(), getTroop());
				procedures.add(newKeepRecruitUnit);
				rb.getMaxRecruit(troop);
				int maxRecruit = rb.getMaxRecruit(troop);
				rb.recruitMax(getTroop());
				Configuration.LOGGER.info("{} {} rekrutiert. Rekrutierung voraussichtlich am {} abgeschlossen (Aktivierungszeit um wieder zu rekrutieren)", maxRecruit, troop.getId(), newKeepRecruitUnit.getActivationTime().getTime());
			} else {
				Configuration.LOGGER.warn("Einheit {} kann momentan nicht rekrutiert werden, da zu wenig Ressourcen vorhanden sind. Versuche in {} Minuten noch einmal", getTroop().getId(), KeepRecruitUnit.TRY_RECRUIT_AGAIN);
				recruitmentTime.add(Calendar.MINUTE, KeepRecruitUnit.TRY_RECRUIT_AGAIN);
				procedures.add(new KeepRecruitUnit(recruitmentTime, getBuilding(), getTroop()));
			}
		} else {
			Configuration.LOGGER.warn("Die Einheit {} kann zur Zeit nicht rekrutiert werden (wahrscheinlich noch nicht erforscht)", troop.getId());
		}

		return procedures;
	}

}
