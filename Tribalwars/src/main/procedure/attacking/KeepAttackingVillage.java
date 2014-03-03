package main.procedure.attacking;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.procedure.Procedure;
import tool.Place;
import utile.Troop;
import utile.TroopTemplate;
import config.TwConfiguration;

public class KeepAttackingVillage extends Procedure {
	
	private final Place place;
	
	private final Point targetCoords;
	private final int delayUntilAttackingAgainInSeconds;
	private final boolean attackAgainWhenTroopsAreBack;
	private final TroopTemplate troopTemplate;
	
	public KeepAttackingVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords, int pDelayUntilAttackingAgainInSeconds, boolean pAttackAgainWhenTroopsAreBack ) {
		super(pConfig, pActivationTime);
		this.place = pPlace;
		this.delayUntilAttackingAgainInSeconds = pDelayUntilAttackingAgainInSeconds;
		this.targetCoords = pTargetCoords;
		this.attackAgainWhenTroopsAreBack = pAttackAgainWhenTroopsAreBack;
		this.troopTemplate = pTroopTemplate;
	}
	
	public KeepAttackingVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords, int pDelayUntilAttackingAgainInSeconds) {
		this(pConfig, pPlace, pActivationTime, pTroopTemplate, pTargetCoords, pDelayUntilAttackingAgainInSeconds, false);
	}
	
	public KeepAttackingVillage(TwConfiguration pConfig, Place pPlace, Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords) {
		this(pConfig, pPlace, pActivationTime, pTroopTemplate, pTargetCoords, 0, true);
	}

	private Map<Troop, Integer> troopsAmount() {
		return this.troopTemplate.getAllTroops();
	}
	
	@Override
	public List<Procedure> doAction() throws ParseException {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		place.goToSite();
		
		boolean enoughTroops = true;
		Iterator<Entry<Troop, Integer>> iterator = troopsAmount().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
			if (pairs.getKey() != Troop.ARCHER && pairs.getKey() != Troop.MARCHER && place.getTroopAmount(pairs.getKey()) < pairs.getValue()) {
				enoughTroops = false;
			}
		}
		
		Calendar attackAgainTime = Calendar.getInstance();
		if (enoughTroops) {
			place.attack(troopsAmount(), targetCoords);
			if (this.attackAgainWhenTroopsAreBack) {
				attackAgainTime.add(Calendar.SECOND, this.troopTemplate.getSlowestTroop().getWalkingDurationSeconds(place.getCurrentCoord(), this.targetCoords));
				attackAgainTime.add(Calendar.SECOND, 30);
			} else {
				attackAgainTime.add(Calendar.SECOND, this.delayUntilAttackingAgainInSeconds);
			}
		} else {
			attackAgainTime.add(Calendar.MINUTE, 2);
		}
		
		procedures.add(new KeepAttackingVillage(config(), place, attackAgainTime, this.troopTemplate, this.targetCoords, this.delayUntilAttackingAgainInSeconds, this.attackAgainWhenTroopsAreBack));
		
		return procedures;
	}

}
