package main.procedure;

import java.awt.Point;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tool.Place;
import utile.Troop;
import utile.TroopTemplate;

public class KeepAttackingVillage extends Procedure {
	
	private static final Place PLACE = Place.getInstance();
	
	private final Point targetCoords;
	private final int delayUntilAttackingAgainInSeconds;
	private final boolean attackAgainWhenTroopsAreBack;
	private final TroopTemplate troopTemplate;
	
	public KeepAttackingVillage(Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords, int pDelayUntilAttackingAgainInSeconds, boolean pAttackAgainWhenTroopsAreBack ) {
		super(pActivationTime);
		this.delayUntilAttackingAgainInSeconds = pDelayUntilAttackingAgainInSeconds;
		this.targetCoords = pTargetCoords;
		this.attackAgainWhenTroopsAreBack = pAttackAgainWhenTroopsAreBack;
		this.troopTemplate = pTroopTemplate;
	}
	
	public KeepAttackingVillage(Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords, int pDelayUntilAttackingAgainInSeconds) {
		this(pActivationTime, pTroopTemplate, pTargetCoords, pDelayUntilAttackingAgainInSeconds, false);
	}
	
	public KeepAttackingVillage(Calendar pActivationTime, TroopTemplate pTroopTemplate, Point pTargetCoords) {
		this(pActivationTime, pTroopTemplate, pTargetCoords, 0, true);
	}

	private Map<Troop, Integer> troopsAmount() {
		return this.troopTemplate.getAllTroops();
	}
	
	@Override
	public List<Procedure> doAction() throws ParseException {
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		PLACE.goToSite();
		
		boolean enoughTroops = true;
		Iterator<Entry<Troop, Integer>> iterator = troopsAmount().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Troop, Integer> pairs = (Map.Entry<Troop, Integer>) iterator.next();
			if (pairs.getKey() != Troop.ARCHER && pairs.getKey() != Troop.MARCHER && PLACE.getTroopAmount(pairs.getKey()) < pairs.getValue()) {
				enoughTroops = false;
			}
		}
		
		Calendar attackAgainTime = Calendar.getInstance();
		if (enoughTroops) {
			PLACE.attack(troopsAmount(), targetCoords);
			if (this.attackAgainWhenTroopsAreBack) {
				attackAgainTime.add(Calendar.SECOND, this.troopTemplate.getSlowestTroop().getWalkingDurationSeconds(PLACE.getCoords(), this.targetCoords));
				attackAgainTime.add(Calendar.SECOND, 30);
			} else {
				attackAgainTime.add(Calendar.SECOND, this.delayUntilAttackingAgainInSeconds);
			}
		} else {
			attackAgainTime.add(Calendar.MINUTE, 10);
		}
		
		procedures.add(new KeepAttackingVillage(attackAgainTime, this.troopTemplate, this.targetCoords, this.delayUntilAttackingAgainInSeconds, this.attackAgainWhenTroopsAreBack));
		
		return procedures;
	}

}
