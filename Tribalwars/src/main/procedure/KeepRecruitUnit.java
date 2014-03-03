package main.procedure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;

import tool.recruitment.RecruitmentBuilding;
import utile.Building;
import utile.Troop;
import config.TwConfiguration;

public class KeepRecruitUnit extends Procedure {

	// Anzahl Minuten nach denen nochmal versucht werden soll, die Einheit zu
	// rekrutieren, falls Einheit momentan nicht rekrutiert werdenn kann
	private final static int TRY_RECRUIT_AGAIN = 10;

	private final Building building;
	private final Troop troop;
	private final RecruitmentBuilding recruitmentBuilding;

	public KeepRecruitUnit(TwConfiguration pConfig, RecruitmentBuilding pRecruitmentBuilding, Calendar pActivationTime, Building pBuilding, Troop pTroop) {
		super(pConfig, pActivationTime);
		this.building = pBuilding;
		this.troop = pTroop;
		this.recruitmentBuilding = pRecruitmentBuilding;
	}

	private Building getBuilding() {
		return this.building;
	}

	private Troop getTroop() {
		return this.troop;
	}

	@Override
	public List<Procedure> doAction() {
		TwConfiguration.LOGGER.debug("Starte Dauerhaft-Rekrutieren-Prozedur");
		List<Procedure> procedures = new ArrayList<Procedure>();

		recruitmentBuilding.goToSite();
		Calendar recruitmentTime = Calendar.getInstance();

		if (recruitmentBuilding.isUnitAvailable(troop)) {
			if (recruitmentBuilding.isMaxLinkBtnEnabled(getTroop())) {
				int maxRecruitmentSeconds = recruitmentBuilding.getMaxRecruit(getTroop()) * recruitmentBuilding.getRecruitmentSeconds(getTroop());
				recruitmentTime.add(Calendar.SECOND, maxRecruitmentSeconds);
				recruitmentTime.add(Calendar.MINUTE, 1);
				KeepRecruitUnit newKeepRecruitUnit = new KeepRecruitUnit(config(), recruitmentBuilding, recruitmentTime, getBuilding(), getTroop());
				procedures.add(newKeepRecruitUnit);
				recruitmentBuilding.getMaxRecruit(troop);
				int maxRecruit = recruitmentBuilding.getMaxRecruit(troop);
				recruitmentBuilding.recruitMax(getTroop());
				TwConfiguration.LOGGER.info("{} {} rekrutiert. Rekrutierung voraussichtlich am {} abgeschlossen (Aktivierungszeit um wieder zu rekrutieren)", maxRecruit, troop.getId(), newKeepRecruitUnit.getActivationTime().getTime());
			} else {
				TwConfiguration.LOGGER.warn("Einheit {} kann momentan nicht rekrutiert werden, da zu wenig Ressourcen vorhanden sind. Versuche in {} Minuten noch einmal", getTroop().getId(), KeepRecruitUnit.TRY_RECRUIT_AGAIN);
				recruitmentTime.add(Calendar.MINUTE, KeepRecruitUnit.TRY_RECRUIT_AGAIN);
				procedures.add(new KeepRecruitUnit(config(), recruitmentBuilding, recruitmentTime, getBuilding(), getTroop()));
			}
		} else {
			TwConfiguration.LOGGER.warn("Die Einheit {} kann zur Zeit nicht rekrutiert werden (wahrscheinlich noch nicht erforscht)", troop.getId());
		}

		return procedures;
	}

}
